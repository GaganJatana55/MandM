package org.example.mandm.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.example.mandm.dataModel.BillEntity

@Dao
interface BillDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillEntity): Long

    @Update
    suspend fun updateBill(bill: BillEntity): Int

    @Query("SELECT * FROM bills WHERE id = :id")
    fun getBillById(id: Long): Flow<BillEntity?>

    @Query("SELECT * FROM bills WHERE userId = :userId ORDER BY endDate DESC, id DESC")
    fun getBillsForUser(userId: Long): Flow<List<BillEntity>>

    @Query("SELECT * FROM bills WHERE userId = :userId ORDER BY endDate DESC, id DESC LIMIT 1")
    fun getLastBillForUser(userId: Long): Flow<BillEntity?>

    @Query("DELETE FROM bills WHERE id = :id")
    suspend fun deleteBillById(id: Long): Int
}




