package org.example.mandm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.example.mandm.dataModel.CustomerRouteItem
import org.example.mandm.dataModel.CustomerRouteWithDetails
import org.example.mandm.dataModel.RouteEntity

@Dao
interface RouteDao {

    // Route CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: RouteEntity): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoutes(routes: List<RouteEntity>)

    @Query("SELECT COUNT(*) FROM routeEntity WHERE routeName IN ('Morning','Evening')")
    suspend fun countDefaultRoutes(): Int
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
    suspend fun insertCustomerRoute(mapping: CustomerRouteItem): Long

    @Update
    suspend fun updateCustomerRoute(mapping: CustomerRouteItem): Int

    @Query("DELETE FROM CustomerRouteItem WHERE customerId = :customerId AND routeId = :routeId")
    suspend fun deleteCustomerFromRoute(customerId: Long, routeId: Int): Int

    @Query("UPDATE RouteMilkItem SET status = :status WHERE id = :id")
    suspend fun updateCustomerRouteStatus(id: Long, status: String): Int

    @Query("UPDATE CustomerRouteItem SET sequenceNumber = :sequenceNumber WHERE id = :id")
    suspend fun updateCustomerRouteSequence(id: Long, sequenceNumber: Int): Int
    @Query("""
        SELECT MAX(sequenceNumber) 
        FROM CustomerRouteItem 
        WHERE routeId = :routeId
    """)
    suspend fun getLastSequenceNumber(routeId: Int): Int?
    @Update
    suspend fun updateCustomerRouteItems(items: List<CustomerRouteItem>)

    @Transaction
    @Query(
        """
        SELECT * 
        FROM CustomerRouteItem
        WHERE routeId = :routeId
        ORDER BY sequenceNumber
        """
    )
    fun getCustomerRouteByRouteId(
        routeId: Int
    ): Flow<List<CustomerRouteWithDetails>>

    @Query(
        """
        SELECT * 
        FROM CustomerRouteItem
        WHERE routeId = :routeId
        ORDER BY sequenceNumber
        """
    )
    fun getCustomerRouteItems(
        routeId: Int
    ): Flow<List<CustomerRouteItem>>



    /**
     * Snapshot fetch (non-Flow) for the initial loading of the State-First list.
     */
    @Query("SELECT * FROM CustomerRouteItem WHERE routeId = :routeId ORDER BY sequenceNumber ASC")
    suspend fun getCustomerRouteItemsSnapshot(routeId: Int): List<CustomerRouteItem>

    /**
     * Upsert handles both inserting new items (id=0) and updating moved items.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCustomerRouteItems(items: List<CustomerRouteItem>)

    /**
     * Specific deletion using the Primary Key 'id' to ensure
     * only the targeted instance is removed.
     */
    @Delete
    suspend fun deleteCustomerRouteItem(item: CustomerRouteItem): Int
}


