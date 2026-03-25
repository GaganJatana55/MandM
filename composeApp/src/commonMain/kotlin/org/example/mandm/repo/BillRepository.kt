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











}




