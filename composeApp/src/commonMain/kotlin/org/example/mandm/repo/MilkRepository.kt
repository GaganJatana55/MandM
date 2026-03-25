package org.example.mandm.repo

import org.example.mandm.dao.MilkDao
import org.example.mandm.dataModel.MilkTransactionEditLogEntity
import org.example.mandm.dataModel.MilkTransactionEntity
import kotlinx.coroutines.flow.Flow
import org.example.mandm.dataModel.MilkTransactionWithLogs

class MilkRepository(
    private val milkDao: MilkDao
) {
    suspend fun insertMilkTransaction(transaction: MilkTransactionEntity): Long =
        milkDao.insertMilkTransaction(transaction)

    suspend fun deleteMilkTransaction(transaction: MilkTransactionEntity): Int =
        milkDao.deleteMilkTransaction(transaction)

    fun getMilkTransactionById(id: Long): Flow<MilkTransactionEntity?> =
        milkDao.getMilkTransactionById(id)

    fun getMilkTransactionsForCustomer(customerId: Long): Flow<List<MilkTransactionEntity>> =
        milkDao.getMilkTransactionsForCustomer(customerId)

    fun getLatestMilkTransactionForCustomer(customerId: Long): Flow<MilkTransactionEntity?> =
        milkDao.getLatestMilkTransactionForCustomer(customerId)

    fun getMilkEditLogsForTransaction(milkTransactionId: Long): Flow<List<MilkTransactionEditLogEntity>> =
        milkDao.getMilkEditLogsForTransaction(milkTransactionId)

    fun getMilkEditLogsForCustomer(customerId: Long): Flow<List<MilkTransactionEditLogEntity>> =
        milkDao.getMilkEditLogsForCustomer(customerId)

    // High-level save API that hides edit-log handling
    suspend fun saveMilkTransaction(transaction: MilkTransactionEntity, timestamp: Long): Long {
        return if (transaction.id == 0L) {
            milkDao.insertMilkTransaction(transaction)
        } else {
            milkDao.updateMilkTransactionWithLog(transaction, timestamp)
            transaction.id
        }
    }
    fun getMilkTransactionsByRange(
        userId: Long,
        start: Long,
        end: Long
    ): Flow<List<MilkTransactionWithLogs>> {

        return milkDao.getMilkTransactionsWithLogsByRange(
            userId = userId,
            start = start,
            end = end
        )
    }
    fun getMilkMonthSummary(userId: Long, start: Long, end: Long) =
        milkDao.getMilkMonthlySummary(userId, start, end)
}


