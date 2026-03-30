package org.example.mandm.repo

import kotlinx.coroutines.flow.Flow
import org.example.mandm.dao.RouteDao
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.dataModel.CustomerRouteItem
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
    suspend fun insertCustomerRoute(mapping: CustomerRouteItem): Long = routeDao.insertCustomerRoute(mapping)
    suspend fun updateCustomerRoute(mapping: CustomerRouteItem): Int = routeDao.updateCustomerRoute(mapping)
    suspend fun deleteCustomerFromRoute(customerId: Long, routeId: Int): Int {
        val id= routeDao.deleteCustomerFromRoute(customerId, routeId)

    return  id;
    }
    suspend fun updateCustomerRouteStatus(id: Long, status: String): Int = routeDao.updateCustomerRouteStatus(id, status)
    suspend fun updateCustomerRouteSequence(id: Long, sequenceNumber: Int): Int = routeDao.updateCustomerRouteSequence(id, sequenceNumber)

    suspend fun ensureDefaultRoutes() {

        val count = routeDao.countDefaultRoutes()

        if (count < 2) {
            val defaultRoutes = DefaultRouteFactory.createDefaultRoutes()
            routeDao.insertRoutes(defaultRoutes)
        }
    }
    suspend fun insertCustomerToRoute(route: RouteEntity,customerEntity: CustomerEntity): Long{
        var lastNumber=routeDao.getLastSequenceNumber(route.routeId)
        if (lastNumber==null){
           lastNumber=1
        }
        return insertCustomerRoute(CustomerRouteItem(customerId = customerEntity.userId, customerName = customerEntity.userName, routeId = route.routeId, sequenceNumber = lastNumber))
    }

    suspend fun updateCustomerRouteOrder(items: List<CustomerRouteItem>) {

        val updatedList = items.mapIndexed { index, item ->
            item.copy(sequenceNumber = index + 1)
        }

        routeDao.updateCustomerRouteItems(updatedList)
    }

}


object DefaultRouteFactory {
    val morningRoute = RouteEntity(
        routeName = "Morning",
        routeStartTime = "03:30 AM",
        routeEndTime = "03:30 PM",
        active = true,
        autoStart = true
    )

    val eveningRoute = RouteEntity(
        routeName = "Evening",
        routeStartTime = "03:30 PM",
        routeEndTime = "03:30 AM",
        active = true,
        autoStart = true
    )

    fun createDefaultRoutes(): List<RouteEntity> {



        return listOf(morningRoute, eveningRoute)
    }

    fun isMorningRoute(route: RouteEntity)= route.routeName==morningRoute.routeName
    fun isEveningRoute(route: RouteEntity)= route.routeName==eveningRoute.routeName
}