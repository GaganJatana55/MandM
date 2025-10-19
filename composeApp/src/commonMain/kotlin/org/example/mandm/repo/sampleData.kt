package org.example.mandm.repo

import org.example.mandm.TransactionTypeConstants
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.dataModel.CustomerRouteItem
import org.example.mandm.dataModel.MilkTransactionEntity
import org.example.mandm.dataModel.RouteEntity
import org.example.mandm.dataModel.RouteMilkItem

// Shared route for all items
val sampleRouteEntity = RouteEntity(
    routeId = 1,
    routeName = "Morning Route",
    routeStartTime = "06:00 AM",
    routeEndTime = "09:00 AM",
    active = true,
    autoStart = false
)

// Sample milk transactions
val sampleMilkTransactions = listOf(
    MilkTransactionEntity(
        id = 101,
        userId = 1,
        userName = "Ramesh",
        dateTimeStamp = "2025-10-19T06:30:00",
        quantity = 5.2,
        transactionType = TransactionTypeConstants.Milk.BUY,
        fixPrice = true,
        snfValue = 7.0,
        snfPrice = 9.5,
        fixPriceValue = 58.0,
        total = 301.6
    ),
    MilkTransactionEntity(
        id = 102,
        userId = 2,
        userName = "Sunita",
        dateTimeStamp = "2025-10-19T07:00:00",
        quantity = 7.0,
        transactionType = TransactionTypeConstants.Milk.BUY,
        fixPrice = false,
        snfValue = 7.2,
        snfPrice = 10.0,
        fixPriceValue = 0.0,
        total = 420.0
    ),
    MilkTransactionEntity(
        id = 103,
        userId = 3,
        userName = "Sharma",
        dateTimeStamp = "2025-10-19T07:20:00",
        quantity = 4.75,
        transactionType = TransactionTypeConstants.Milk.SELL,
        fixPrice = true,
        snfValue = 0.0,
        snfPrice = 0.0,
        fixPriceValue = 62.0,
        total = 294.5
    ),
    MilkTransactionEntity(
        id = 104,
        userId = 4,
        userName = "Kiran",
        dateTimeStamp = "2025-10-19T07:45:00",
        quantity = 6.3,
        transactionType = TransactionTypeConstants.Milk.SELL,
        fixPrice = true,
        snfValue = 0.0,
        snfPrice = 0.0,
        fixPriceValue = 61.0,
        total = 384.3
    )
)

// Corresponding customer entities
val sampleCustomers = listOf(
    CustomerEntity(
        userId = 1,
        userName = "Ramesh",
        nickName = "Ramu",
        phone = "9876543210",
        village = "Shivapur",
        address = "Main Road",
        customerType = "Farmer",
        sequenceNumber = 1,
        createdDate = "2025-10-01",
        createdTime = "06:15:00",
        active = true
    ),
    CustomerEntity(
        userId = 2,
        userName = "Sunita",
        nickName = null,
        phone = "9822110033",
        village = "Devgaon",
        address = "Near Market",
        customerType = "Farmer",
        sequenceNumber = 2,
        createdDate = "2025-10-02",
        createdTime = "06:25:00",
        active = true
    ),
    CustomerEntity(
        userId = 3,
        userName = "Sharma",
        nickName = "Sharmaji",
        phone = "9810099900",
        village = "Lakshmi Nagar",
        address = "Post Office Road",
        customerType = "Retail",
        sequenceNumber = 3,
        createdDate = "2025-10-03",
        createdTime = "07:00:00",
        active = true
    ),
    CustomerEntity(
        userId = 4,
        userName = "Kiran",
        nickName = null,
        phone = "9833001122",
        village = "Rampur",
        address = "Opp. Temple",
        customerType = "Farmer",
        sequenceNumber = 4,
        createdDate = "2025-10-04",
        createdTime = "07:15:00",
        active = true
    )
)

// Route milk items linked to route & transactions
val sampleRouteMilkItems = listOf(
    RouteMilkItem(
        id = 201,
        RouteId = 1,
        Date = "2025-10-19",
        Time = "06:30:00",
        Status = "Completed",
        MilkTransId = 101,
        UserId = 1,
        milkTransactionEntity = sampleMilkTransactions[0]
    ),
    RouteMilkItem(
        id = 202,
        RouteId = 1,
        Date = "2025-10-19",
        Time = "07:00:00",
        Status = "Completed",
        MilkTransId = 102,
        UserId = 2,
        milkTransactionEntity = sampleMilkTransactions[1]
    ),
    RouteMilkItem(
        id = 203,
        RouteId = 1,
        Date = "2025-10-19",
        Time = "07:20:00",
        Status = "Pending",
        MilkTransId = 103,
        UserId = 3,
        milkTransactionEntity = sampleMilkTransactions[2]
    ),
    RouteMilkItem(
        id = 204,
        RouteId = 1,
        Date = "2025-10-19",
        Time = "07:45:00",
        Status = "Completed",
        MilkTransId = 104,
        UserId = 4,
        milkTransactionEntity = sampleMilkTransactions[3]
    )
)

// Finally, combine everything into CustomerRouteItems
val sampleCustomerRouteItems = listOf(
    CustomerRouteItem(
        id = 301,
        customerId = 1,
        customerName = "Ramesh",
        routeId = 1,
        sequenceNumber = 1,
        routeEntity = sampleRouteEntity,
        routeMilkItem = sampleRouteMilkItems[0],
        customerEntity = sampleCustomers[0]
    ),
    CustomerRouteItem(
        id = 302,
        customerId = 2,
        customerName = "Sunita",
        routeId = 1,
        sequenceNumber = 2,
        routeEntity = sampleRouteEntity,
        routeMilkItem = sampleRouteMilkItems[1],
        customerEntity = sampleCustomers[1]
    ),
    CustomerRouteItem(
        id = 303,
        customerId = 3,
        customerName = "Sharma",
        routeId = 1,
        sequenceNumber = 3,
        routeEntity = sampleRouteEntity,
        routeMilkItem = sampleRouteMilkItems[2],
        customerEntity = sampleCustomers[2]
    ),
    CustomerRouteItem(
        id = 304,
        customerId = 4,
        customerName = "Kiran",
        routeId = 1,
        sequenceNumber = 4,
        routeEntity = sampleRouteEntity,
        routeMilkItem = sampleRouteMilkItems[3],
        customerEntity = sampleCustomers[3]
    )
)
