package org.example.mandm.repo

import kotlinx.coroutines.flow.Flow
import org.example.mandm.dao.RouteDao
import org.example.mandm.TransactionStatus
import org.example.mandm.RouteSequenceUtil
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.dataModel.CustomerRouteEntity
import org.example.mandm.dataModel.RouteEntity

class RouteRepository(
    private val routeDao: RouteDao
) {
    // Routes
    suspend fun insertRoute(route: RouteEntity): Long = routeDao.insertRoute(route)
    suspend fun updateRoute(route: RouteEntity): Int = routeDao.updateRoute(route)
    suspend fun deleteRoute(route: RouteEntity): Int = routeDao.deleteRoute(route)
    fun getAllRoutes(): Flow<List<RouteEntity>> = routeDao.getAllRoutes()
    fun getActiveRoutes(): Flow<List<RouteEntity>> = routeDao.getActiveRoutes()
    fun getRouteById(id: Int): Flow<RouteEntity?> = routeDao.getRouteById(id)

    // Customer-Route
    suspend fun insertCustomerRoute(mapping: CustomerRouteEntity): Long = routeDao.insertCustomerRoute(mapping)
    suspend fun updateCustomerRoute(mapping: CustomerRouteEntity): Int = routeDao.updateCustomerRoute(mapping)
    suspend fun deleteCustomerFromRoute(customerId: Long, routeId: Int): Int =
        routeDao.deleteCustomerFromRoute(customerId, routeId)
    suspend fun updateCustomerRouteStatus(id: Long, status: String): Int = routeDao.updateCustomerRouteStatus(id, status)
    suspend fun updateCustomerRouteSequence(id: Long, sequenceNumber: Int): Int = routeDao.updateCustomerRouteSequence(id, sequenceNumber)
    fun getCustomersForRoute(routeId: Int): Flow<List<CustomerEntity>> = routeDao.getCustomersForRoute(routeId)

    // Helper to add a customer to a route with sensible defaults
    suspend fun addCustomerToRoute(
        customerId: Long,
        routeId: Int,
        sequenceNumber: Int = RouteSequenceUtil.nextSequence(),
        status: String = TransactionStatus.PENDING,
        date: String
    ): Long {
        return routeDao.insertCustomerRoute(
            CustomerRouteEntity(
                customerId = customerId,
                routeId = routeId,
                sequenceNumber = sequenceNumber,
                status = status,
                date = date
            )
        )
    }
}


