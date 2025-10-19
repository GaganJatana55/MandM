package org.example.mandm.repo

import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import org.example.mandm.TransactionTypeConstants
import org.example.mandm.dao.BillDao
import org.example.mandm.dao.MilkDao
import org.example.mandm.dao.MoneyDao
import org.example.mandm.dataModel.BillEntity
import org.example.mandm.dataModel.MilkTransactionEntity
import org.example.mandm.dataModel.MoneyTransactionEntity

class BillRepository(
    private val billDao: BillDao,
    private val milkDao: MilkDao,
    private val moneyDao: MoneyDao,
) {

    suspend fun getLastBillForUser(userId: Long) = billDao.getLastBillForUser(userId).first()
    fun getBillsForUser(userId: Long) = billDao.getBillsForUser(userId)

    suspend fun insert(bill: BillEntity): Long = billDao.insertBill(bill)
    suspend fun update(bill: BillEntity): Int = billDao.updateBill(bill)

    suspend fun generateBill(
        userId: Long,
        userName: String,
        endDate: String,
        createdOn: String
    ): Long {
        val lastBill = getLastBillForUser(userId)
        val startDate: String = lastBill?.let { nextStartDate(it.endDate) } ?: defaultStartDate(userId)

        // Compute milk totals within [startDate, endDate]
        val milkList = milkDao.getMilkTransactionsForCustomer(userId).first()
            .filter { inDateRange(it.dateTimeStamp, startDate, endDate) }
        val totalMilkQuantity = milkList.sumOf { safeQuantity(it) }
        val totalMilkAmount = milkList.sumOf { it.total }

        // Compute money totals within range
        val moneyList = moneyDao.getMoneyTransactionsForCustomer(userId).first()
            .filter { inDateRange(it.dateTimeStamp, startDate, endDate) }
        val moneyReceived = moneyList.filter { it.transactionType == TransactionTypeConstants.Money.RECEIVED }
            .sumOf { it.amount }
        val moneyPaid = moneyList.filter { it.transactionType == TransactionTypeConstants.Money.PAID }
            .sumOf { it.amount }

        val carryForward = lastBill?.pendingAmount ?: 0.0

        val pending = computePending(
            carryForward = carryForward,
            totalMilkAmount = totalMilkAmount,
            moneyReceivedInRange = moneyReceived,
            moneyPaidInRange = moneyPaid,
            paymentsReceived = 0.0,
            paymentsPaid = 0.0
        )
        val status = deriveStatus(pending, moneyReceived, moneyPaid, totalMilkAmount, carryForward)

        val bill = BillEntity(
            userId = userId,
            userName = userName,
            startDate = startDate,
            endDate = endDate,
            totalMilkQuantity = totalMilkQuantity,
            totalMilkAmount = totalMilkAmount,
            moneyReceivedInRange = moneyReceived,
            moneyPaidInRange = moneyPaid,
            carryForwardAmount = carryForward,
            paymentsReceived = 0.0,
            paymentsPaid = 0.0,
            pendingAmount = pending,
            status = status,
            createdOn = createdOn
        )
        return billDao.insertBill(bill)
    }

    suspend fun settleBill(
        bill: BillEntity,
        amountReceived: Double = 0.0,
        amountPaid: Double = 0.0
    ) {
        val updatedPaymentsReceived = bill.paymentsReceived + amountReceived
        val updatedPaymentsPaid = bill.paymentsPaid + amountPaid
        val newPending = computePending(
            carryForward = bill.carryForwardAmount,
            totalMilkAmount = bill.totalMilkAmount,
            moneyReceivedInRange = bill.moneyReceivedInRange,
            moneyPaidInRange = bill.moneyPaidInRange,
            paymentsReceived = updatedPaymentsReceived,
            paymentsPaid = updatedPaymentsPaid
        )
        val newStatus = deriveStatus(
            pendingAmount = newPending,
            moneyReceivedInRange = bill.moneyReceivedInRange + updatedPaymentsReceived,
            moneyPaidInRange = bill.moneyPaidInRange + updatedPaymentsPaid,
            totalMilkAmount = bill.totalMilkAmount,
            carryForward = bill.carryForwardAmount
        )
        val updated = bill.copy(
            paymentsReceived = updatedPaymentsReceived,
            paymentsPaid = updatedPaymentsPaid,
            pendingAmount = newPending,
            status = newStatus
        )
        billDao.updateBill(updated)
    }

    private fun computePending(
        carryForward: Double,
        totalMilkAmount: Double,
        moneyReceivedInRange: Double,
        moneyPaidInRange: Double,
        paymentsReceived: Double,
        paymentsPaid: Double
    ): Double {
        // Amount due = carryForward + milk amount - (received - paid) - (later settlements received - paid)
        val netMoneyInRange = moneyReceivedInRange - moneyPaidInRange
        val netSettlements = paymentsReceived - paymentsPaid
        return carryForward + totalMilkAmount - netMoneyInRange - netSettlements
    }

    private fun deriveStatus(
        pendingAmount: Double,
        moneyReceivedInRange: Double,
        moneyPaidInRange: Double,
        totalMilkAmount: Double,
        carryForward: Double
    ): String {
        val due = carryForward + totalMilkAmount
        val credit = moneyReceivedInRange - moneyPaidInRange
        return when {
            pendingAmount <= 0.0001 && credit >= due -> "Paid"
            credit in 0.0..due -> "Partial"
            else -> "Pending"
        }
    }

    private fun inDateRange(dateTime: String, startDate: String, endDate: String): Boolean {
        // dateTime expected as "yyyy-MM-dd.." prefix
        val d = runCatching { dateTime.substring(0, 10).toLocalDate() }.getOrNull() ?: return false
        val s = startDate.toLocalDate()
        val e = endDate.toLocalDate()
        return (d >= s && d <= e)
    }

    private fun safeQuantity(tx: MilkTransactionEntity): Double = tx.quantity

    private fun nextStartDate(lastEndDate: String): String {
        // keep as the same lastEndDate for inclusive ranges; alternatively add +1 day if needed
        return lastEndDate
    }

    private suspend fun defaultStartDate(userId: Long): String {
        // If no previous bill, start from earliest transaction date available
        val milk = milkDao.getMilkTransactionsForCustomer(userId).first().lastOrNull()?.dateTimeStamp
        val money = moneyDao.getMoneyTransactionsForCustomer(userId).first().lastOrNull()?.dateTimeStamp
        val candidate = listOfNotNull(milk, money).minOrNull()
        return candidate?.substring(0, 10) ?: "1970-01-01"
    }
}



