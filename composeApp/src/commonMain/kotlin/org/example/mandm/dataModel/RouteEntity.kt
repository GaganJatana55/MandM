package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routeEntity")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true)
    val routeId: Int = 0,           // Unique ID for each route
    val routeName: String,          // Name of the route
    val routeStartTime: String,     // e.g. "06:00 AM"
    val routeEndTime: String,       // e.g. "09:00 AM"
    val active: Boolean = true      // Whether the route is currently active
)
