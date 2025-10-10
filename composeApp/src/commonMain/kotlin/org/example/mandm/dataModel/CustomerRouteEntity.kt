package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_route")
data class CustomerRouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val customerId: Long,
    val routeId: Int,
    val sequenceNumber: Int,
    val status: String
)


