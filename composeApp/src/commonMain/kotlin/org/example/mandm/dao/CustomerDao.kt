package org.example.mandm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.example.mandm.dataModel.CustomerEntity

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<CustomerEntity>): List<Long>

    @Update
    suspend fun updateCustomer(customer: CustomerEntity): Int

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity): Int

    @Query("SELECT * FROM customerEntity WHERE userId = :id")
    fun getCustomerById(id: Long): Flow<CustomerEntity?>

    @Query("SELECT * FROM customerEntity")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customerEntity WHERE active = 1")
    fun getActiveCustomers(): Flow<List<CustomerEntity>>

    @Query(
        "SELECT * FROM customerEntity WHERE userName LIKE :query OR phone LIKE :query"
    )
    fun searchCustomersByNameOrPhone(query: String): Flow<List<CustomerEntity>>
}


