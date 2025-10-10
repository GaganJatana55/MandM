package org.example.mandm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.example.mandm.dataModel.MilkTransactionEditLogEntity
import org.example.mandm.dataModel.MilkTransactionEntity
import org.example.mandm.dataModel.toEditLog

@Dao
interface MilkDao {

    // MilkTransaction basic ops
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilkTransaction(transaction: MilkTransactionEntity): Long

    @Update
    suspend fun updateMilkTransaction(transaction: MilkTransactionEntity): Int

    @Delete
    suspend fun deleteMilkTransaction(transaction: MilkTransactionEntity): Int

    @Query("SELECT * FROM milk_transactions WHERE id = :id")
    fun getMilkTransactionById(id: Long): Flow<MilkTransactionEntity?>

    @Query("SELECT * FROM milk_transactions WHERE userId = :customerId ORDER BY dateTimeStamp DESC")
    fun getMilkTransactionsForCustomer(customerId: Long): Flow<List<MilkTransactionEntity>>

    @Query("SELECT * FROM milk_transactions WHERE userId = :customerId ORDER BY dateTimeStamp DESC LIMIT 1")
    fun getLatestMilkTransactionForCustomer(customerId: Long): Flow<MilkTransactionEntity?>

    @Query("SELECT * FROM milk_transactions ORDER BY dateTimeStamp DESC")
    fun getAllMilkTransactions(): Flow<List<MilkTransactionEntity>>

    // Logs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilkEditLog(log: MilkTransactionEditLogEntity): Long

    @Query("SELECT * FROM milk_transaction_edit_logs WHERE milkTransactionId = :milkTransactionId ORDER BY updatedDateTimeStamp DESC")
    fun getMilkEditLogsForTransaction(milkTransactionId: Long): Flow<List<MilkTransactionEditLogEntity>>

    @Query("SELECT * FROM milk_transaction_edit_logs WHERE userId = :customerId ORDER BY updatedDateTimeStamp DESC")
    fun getMilkEditLogsForCustomer(customerId: Long): Flow<List<MilkTransactionEditLogEntity>>

    // Transactional update with log
    @Transaction
    suspend fun updateMilkTransactionWithLog(updated: MilkTransactionEntity, updatedTime: String) {
        val existing = getMilkTransactionById(updated.id).firstOrNull()
        if (existing != null) {
            insertMilkEditLog(existing.toEditLog(updatedTime))
        }
        updateMilkTransaction(updated)
    }
}


