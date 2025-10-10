package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customerEntity")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    var userId: Long=0L,               // Unique ID for each user
    val userName: String,  //name of the user
    var nickName: String?=null,// nickName of the user
    val phone: String,                   // Phone number
    val village: String,                 // Village name
    val address: String,                 // Address details
    val customerType: String,            // e.g. "Retail", "Wholesale", "Farmer", etc.
    val sequenceNumber: Int,             // Display or ordering sequence
    val createdDate: String,             // e.g. "2025-10-05"
    val createdTime: String,             // e.g. "14:35:22"
    val active: Boolean = true           // Whether user is active
)
