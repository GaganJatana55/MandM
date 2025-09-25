package org.example.mandm.dataModel

import androidx.compose.ui.semantics.Role
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val contact: String,
    val village: String,
    val role: String,
    val address: String,
    val createdOn: String,
    val updatedOn: String
)


val dummyUser = User(
    id = 0, // Room will auto-generate this
    name = "Gagandeep Singh",
    contact = "+91-9876543210",
    village = "Bhullar Kalan",
    role = "Farmer",
    address = "Street No. 5, Near Gurudwara Sahib",
    createdOn = "2025-09-19T10:30:00",
    updatedOn = "2025-09-19T10:30:00"
)
