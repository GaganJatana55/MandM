package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_route")
data class CustomerRouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val customerId: Long,
    val customerName: String,
    val routeId: Int,
    val sequenceNumber: Int,

)
@Entity
data class RouteMilkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val RouteId: Long,
    val Date: String,
    val Time: String,
    val Status: String,
    val MilkTransId: Long?,
    val UserId:Long
)


data class CustomerRouteItem(
    val id: Long = 0L,
    val customerId: Long,
    val customerName: String,
    val routeId: Int,
    val sequenceNumber: Int,
    val routeEntity: RouteEntity,
    val routeMilkItem: RouteMilkItem,
    val customerEntity: CustomerEntity

)



data class RouteMilkItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val RouteId: Long,
    val Date: String,
    val Time: String,
    val Status: String,
    val MilkTransId: Long?,
    val UserId:Long,
    val milkTransactionEntity: MilkTransactionEntity?
)




fun CustomerRouteEntity.toCustomerRouteItem(
    routeEntity: RouteEntity,
    routeMilkItem: RouteMilkItem,
    customerEntity: CustomerEntity
): CustomerRouteItem {
    return CustomerRouteItem(
        id = this.id,
        customerId = this.customerId,
        customerName = this.customerName,
        routeId = this.routeId,
        sequenceNumber = this.sequenceNumber,
        routeEntity = routeEntity,
        routeMilkItem = routeMilkItem,
        customerEntity = customerEntity
    )
}

fun RouteMilkEntity.toRouteMilkItem(
    milkTransactionEntity: MilkTransactionEntity? = null
): RouteMilkItem {
    return RouteMilkItem(
        id = this.id,
        RouteId = this.RouteId,
        Date = this.Date,
        Time = this.Time,
        Status = this.Status,
        MilkTransId = this.MilkTransId,
        UserId = this.UserId,
        milkTransactionEntity = milkTransactionEntity
    )
}

