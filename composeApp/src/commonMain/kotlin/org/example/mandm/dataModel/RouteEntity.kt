package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
@Serializable
@Entity(
    tableName = "routeEntity",
    indices = [Index(value = ["routeName"], unique = true)]
)
data class RouteEntity(
    @PrimaryKey(autoGenerate = true)
    val routeId: Int = 0,
    val routeName: String,
    val routeStartTime: String,
    val routeEndTime: String,
    val active: Boolean = true,
    val autoStart: Boolean = false
)