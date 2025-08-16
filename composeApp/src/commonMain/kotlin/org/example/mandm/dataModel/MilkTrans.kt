package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.mandm.DateTimeUtil
import org.example.mandm.TransactionTypeConstants


data class MilkTrans(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var type: String = TransactionTypeConstants.SELL,
    var userId: Int = -1,
    var userName: String = "UNKNOWN",
    var fixPrice: Boolean = true,
    var SNF: Double = -1.0,
    var SNFPrice: Double = 5.0,
    var quantity: Double = 0.0,
    var price: Double = 50.0,
    var date: String = DateTimeUtil.currentUtcDateString(), // "2025-08-01"
    var time: Long = DateTimeUtil.currentUtcMillis() // epoch millis in UTC
)

val sampleTransactions = listOf(
    MilkTrans(userId = 1, userName = "Ramesh", type = TransactionTypeConstants.BUY, quantity = 5.200),
    MilkTrans(userId = 2, userName = "Sunita", type = TransactionTypeConstants.BUY, quantity = 7.000, fixPrice = false, SNF = 7.200, SNFPrice = 10.0),
    MilkTrans(userId = 3, userName = "Sharma", type = TransactionTypeConstants.SELL, quantity = 4.750, price = 60.0),
    MilkTrans(userId = 4, userName = "Kiran", type = TransactionTypeConstants.SELL, quantity = 6.300, price = 62.0),
    MilkTrans(userId = 5, userName = "Ravi", type = TransactionTypeConstants.BUY, quantity = 8.100),
    MilkTrans(userId = 6, userName = "Neha", type = TransactionTypeConstants.BUY, quantity = 6.400, fixPrice = false, SNF = 8.000, SNFPrice = 9.0),
    MilkTrans(userId = 7, userName = "Ajay", type = TransactionTypeConstants.SELL, quantity = 3.500, price = 55.0),
    MilkTrans(userId = 8, userName = "Deepa", type = TransactionTypeConstants.SELL, quantity = 7.750, price = 58.0),
    MilkTrans(userId = 9, userName = "Bharat", type = TransactionTypeConstants.BUY, quantity = 9.250, fixPrice = false, SNF = 6.500, SNFPrice = 11.0),
    MilkTrans(userId = 10, userName = "Manju", type = TransactionTypeConstants.BUY, quantity = 4.600),
    MilkTrans(userId = 11, userName = "Kanta", type = TransactionTypeConstants.SELL, quantity = 6.050, price = 59.0),
    MilkTrans(userId = 12, userName = "Rahul", type = TransactionTypeConstants.BUY, quantity = 5.400),
    MilkTrans(userId = 13, userName = "Aarti", type = TransactionTypeConstants.BUY, quantity = 7.200, fixPrice = false, SNF = 6.800, SNFPrice = 9.5),
    MilkTrans(userId = 14, userName = "Gopal", type = TransactionTypeConstants.SELL, quantity = 3.200, price = 56.0),
    MilkTrans(userId = 15, userName = "Ritu", type = TransactionTypeConstants.SELL, quantity = 6.850, price = 61.0),
    MilkTrans(userId = 16, userName = "Vikram", type = TransactionTypeConstants.BUY, quantity = 4.000),
    MilkTrans(userId = 17, userName = "Nisha", type = TransactionTypeConstants.BUY, quantity = 5.100, fixPrice = false, SNF = 7.500, SNFPrice = 10.0),
    MilkTrans(userId = 18, userName = "Sameer", type = TransactionTypeConstants.SELL, quantity = 5.050, price = 60.0),
    MilkTrans(userId = 19, userName = "Lata", type = TransactionTypeConstants.SELL, quantity = 8.000, price = 63.0),
    MilkTrans(userId = 20, userName = "Suresh", type = TransactionTypeConstants.BUY, quantity = 6.500),
    MilkTrans(userId = 21, userName = "Pooja", type = TransactionTypeConstants.BUY, quantity = 7.050, fixPrice = false, SNF = 6.900, SNFPrice = 9.8),
    MilkTrans(userId = 22, userName = "Kamal", type = TransactionTypeConstants.SELL, quantity = 4.750, price = 57.0),
    MilkTrans(userId = 23, userName = "Meena", type = TransactionTypeConstants.SELL, quantity = 5.200, price = 58.0),
    MilkTrans(userId = 24, userName = "Harish", type = TransactionTypeConstants.BUY, quantity = 6.000),
    MilkTrans(userId = 25, userName = "Jyoti", type = TransactionTypeConstants.BUY, quantity = 7.300, fixPrice = false, SNF = 7.300, SNFPrice = 10.5)
)
