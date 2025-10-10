package org.example.mandm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.example.mandm.TransactionStatus
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.dataModel.CustomerRouteEntity
import org.example.mandm.dataModel.RouteEntity

@Dao
interface RouteDao {

    // Route CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: RouteEntity): Long

    @Update
    suspend fun updateRoute(route: RouteEntity): Int

    @Delete
    suspend fun deleteRoute(route: RouteEntity): Int

    @Query("SELECT * FROM routeEntity")
    fun getAllRoutes(): Flow<List<RouteEntity>>

    @Query("SELECT * FROM routeEntity WHERE active = 1")
    fun getActiveRoutes(): Flow<List<RouteEntity>>

    @Query("SELECT * FROM routeEntity WHERE routeId = :id")
    fun getRouteById(id: Int): Flow<RouteEntity?>

    // CustomerRoute mapping
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerRoute(mapping: CustomerRouteEntity): Long

    @Update
    suspend fun updateCustomerRoute(mapping: CustomerRouteEntity): Int

    @Query("DELETE FROM customer_route WHERE customerId = :customerId AND routeId = :routeId")
    suspend fun deleteCustomerFromRoute(customerId: Long, routeId: Int): Int

    @Query("UPDATE customer_route SET status = :status WHERE id = :id")
    suspend fun updateCustomerRouteStatus(id: Long, status: String): Int

    @Query("UPDATE customer_route SET sequenceNumber = :sequenceNumber WHERE id = :id")
    suspend fun updateCustomerRouteSequence(id: Long, sequenceNumber: Int): Int

    // Join: customers in a route ordered by status priority and sequenceNumber
    @Query(
        """
        SELECT c.* FROM customer_route cr
        INNER JOIN customerEntity c ON c.userId = cr.customerId
        WHERE cr.routeId = :routeId
        ORDER BY 
          CASE cr.status 
            WHEN :pending THEN 1 
            WHEN :skipped THEN 2 
            WHEN :done THEN 3 
            ELSE 4 
          END,
          cr.sequenceNumber ASC
        """
    )
    fun getCustomersForRoute(
        routeId: Int,
        pending: String = TransactionStatus.PENDING,
        skipped: String = TransactionStatus.SKIPPED,
        done: String = TransactionStatus.ADDED
    ): Flow<List<CustomerEntity>>
}


