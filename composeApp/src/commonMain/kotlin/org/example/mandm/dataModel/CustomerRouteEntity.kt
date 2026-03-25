package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


import androidx.room.*
import kotlinx.coroutines.flow.Flow

/* ---------------------------------------------------
   ENTITIES
--------------------------------------------------- */

@Entity(
    indices = [
        Index("routeId"),
        Index("customerId"),
        Index("routeMilkId")
    ]
)
data class CustomerRouteItem(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val customerId: Long,
    val customerName: String,
    val routeId: Int,
    val sequenceNumber: Int,

    val routeMilkId: Long? = null
)


@Entity
data class RouteMilkItem(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val routeId: Long,
    val dateTimeStamp: Long,
    val status: String,

    val milkTransId: Long?,
    val userId: Long
)








/* ---------------------------------------------------
   RELATION MODELS
--------------------------------------------------- */

data class RouteMilkItemWithTransaction(

    @Embedded
    val routeMilkItem: RouteMilkItem,

    @Relation(
        parentColumn = "milkTransId",
        entityColumn = "id"
    )
    val milkTransaction: MilkTransactionEntity?
)

data class CustomerRouteWithDetails(

    @Embedded
    val routeItem: CustomerRouteItem,

    @Relation(
        parentColumn = "customerId",
        entityColumn = "userId"
    )
    val customer: CustomerEntity,

    @Relation(
        parentColumn = "routeId",
        entityColumn = "routeId"
    )
    val route: RouteEntity,

    @Relation(
        parentColumn = "routeMilkId",
        entityColumn = "id",
        entity = RouteMilkItem::class
    )
    val routeMilkItemWithTransaction: RouteMilkItemWithTransaction?
)

