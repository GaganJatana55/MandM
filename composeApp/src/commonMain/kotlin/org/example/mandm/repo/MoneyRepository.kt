package org.example.mandm.repo

import org.example.mandm.dao.MoneyDao
import org.example.mandm.dataModel.MoneyTransactionEditLogEntity
import org.example.mandm.dataModel.MoneyTransactionEntity
import kotlinx.coroutines.flow.Flow
import org.example.mandm.dataModel.MoneyTransactionWithLogs

class MoneyRepository(
    private val moneyDao: MoneyDao
) {
    suspend fun insertMoneyTransaction(transaction: MoneyTransactionEntity): Long =
        moneyDao.insertMoneyTransaction(transaction)

    suspend fun deleteMoneyTransaction(transaction: MoneyTransactionEntity): Int =
        moneyDao.deleteMoneyTransaction(transaction)

    fun getMoneyTransactionById(id: Long): Flow<MoneyTransactionEntity?> =
        moneyDao.getMoneyTransactionById(id)

    fun getMoneyTransactionsForCustomer(customerId: Long): Flow<List<MoneyTransactionEntity>> =
        moneyDao.getMoneyTransactionsForCustomer(customerId)

    fun getLatestMoneyTransactionForCustomer(customerId: Long): Flow<MoneyTransactionEntity?> =
        moneyDao.getLatestMoneyTransactionForCustomer(customerId)

    fun getMoneyEditLogsForTransaction(moneyTransactionId: Long): Flow<List<MoneyTransactionEditLogEntity>> =
        moneyDao.getMoneyEditLogsForTransaction(moneyTransactionId)

    fun getMoneyEditLogsForCustomer(customerId: Long): Flow<List<MoneyTransactionEditLogEntity>> =
        moneyDao.getMoneyEditLogsForCustomer(customerId)

    // High-level save API that hides edit-log handling
    suspend fun saveMoneyTransaction(transaction: MoneyTransactionEntity, timestamp: Long): Long {
        return if (transaction.id == 0L) {
            moneyDao.insertMoneyTransaction(transaction)
        } else {
            moneyDao.updateMoneyTransactionWithLog(transaction, timestamp)
            transaction.id
        }
    }
    fun getMoneyTransactionsByRange(
        userId: Long,
        start: Long,
        end: Long
    ): Flow<List<MoneyTransactionWithLogs>> {

        return moneyDao.getMoneyTransactionsWithLogsByRange(
            userId = userId,
            start = start,
            end = end
        )
    }
    fun getMoneyMonthSummary(userId: Long, start: Long, end: Long) =
        moneyDao.getMoneyMonthlySummary(userId, start, end)
}


