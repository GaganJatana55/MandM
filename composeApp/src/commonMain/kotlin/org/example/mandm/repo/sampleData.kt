package org.example.mandm.repo

import org.example.mandm.DateTimeUtil.localDateTimeToMillis
import org.example.mandm.DateTimeUtil.toMillis
import org.example.mandm.TransactionTypeConstants
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.dataModel.CustomerRouteItem
import org.example.mandm.dataModel.CustomerRouteWithDetails
import org.example.mandm.dataModel.MilkTransactionEntity
import org.example.mandm.dataModel.RouteEntity
import org.example.mandm.dataModel.RouteMilkItem
import org.example.mandm.dataModel.RouteMilkItemWithTransaction

/* ---------------------------------------------------
   ROUTE
--------------------------------------------------- */

val sampleRoute = RouteEntity(
    routeId = 1,
    routeName = "Morning Route",
    routeStartTime = "06:00 AM",
    routeEndTime = "09:00 AM",
    active = true,
    autoStart = false
)


/* ---------------------------------------------------
   CUSTOMERS
--------------------------------------------------- */

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


/* ---------------------------------------------------
   MILK TRANSACTIONS
--------------------------------------------------- */

val sampleMilkTransactions = listOf(

    MilkTransactionEntity(
        id = 101,
        userId = 1,
        userName = "Ramesh",
        dateTimeStamp = toMillis(2025, 10, 19, 6, 30),
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
        dateTimeStamp = toMillis(2025, 10, 19, 7, 0),
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
        dateTimeStamp = toMillis(2025, 10, 19, 7, 20),
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
        dateTimeStamp = toMillis(2025, 10, 19, 7, 45),
        quantity = 6.3,
        transactionType = TransactionTypeConstants.Milk.SELL,
        fixPrice = true,
        snfValue = 0.0,
        snfPrice = 0.0,
        fixPriceValue = 61.0,
        total = 384.3
    )
)


/* ---------------------------------------------------
   ROUTE MILK ITEMS
--------------------------------------------------- */

val sampleRouteMilkItems = listOf(

    RouteMilkItem(
        id = 201,
        routeId = 1,
        dateTimeStamp = localDateTimeToMillis(2025, 10, 19, 6, 30),
        status = "Completed",
        milkTransId = 101,
        userId = 1
    ),

    RouteMilkItem(
        id = 202,
        routeId = 1,
        dateTimeStamp = localDateTimeToMillis(2025, 10, 19, 7, 0),
        status = "Completed",
        milkTransId = 102,
        userId = 2
    ),

    RouteMilkItem(
        id = 203,
        routeId = 1,
        dateTimeStamp = localDateTimeToMillis(2025, 10, 19, 7, 20),
        status = "Pending",
        milkTransId = 103,
        userId = 3
    ),

    RouteMilkItem(
        id = 204,
        routeId = 1,
        dateTimeStamp = localDateTimeToMillis(2025, 10, 19, 7, 45),
        status = "Completed",
        milkTransId = 104,
        userId = 4
    )
)


/* ---------------------------------------------------
   CUSTOMER ROUTE ITEMS
--------------------------------------------------- */

val sampleCustomerRouteItems = listOf(

    CustomerRouteItem(
        id = 301,
        customerId = 1,
        customerName = "Ramesh",
        routeId = 1,
        sequenceNumber = 1,
        routeMilkId = 201
    ),

    CustomerRouteItem(
        id = 302,
        customerId = 2,
        customerName = "Sunita",
        routeId = 1,
        sequenceNumber = 2,
        routeMilkId = 202
    ),

    CustomerRouteItem(
        id = 303,
        customerId = 3,
        customerName = "Sharma",
        routeId = 1,
        sequenceNumber = 3,
        routeMilkId = 203
    ),

    CustomerRouteItem(
        id = 304,
        customerId = 4,
        customerName = "Kiran",
        routeId = 1,
        sequenceNumber = 4,
        routeMilkId = 204
    )
)

val sampleCustomerRouteWithDetails = listOf(

    CustomerRouteWithDetails(
        routeItem = CustomerRouteItem(
            id = 301,
            customerId = 1,
            customerName = "Ramesh",
            routeId = 1,
            sequenceNumber = 1,
            routeMilkId = 201
        ),
        customer = sampleCustomers[0],
        route = sampleRoute,
        routeMilkItemWithTransaction = RouteMilkItemWithTransaction(
            routeMilkItem = sampleRouteMilkItems[0],
            milkTransaction = sampleMilkTransactions[0]
        )
    ),

    CustomerRouteWithDetails(
        routeItem = CustomerRouteItem(
            id = 302,
            customerId = 2,
            customerName = "Sunita",
            routeId = 1,
            sequenceNumber = 2,
            routeMilkId = 202
        ),
        customer = sampleCustomers[1],
        route = sampleRoute,
        routeMilkItemWithTransaction = RouteMilkItemWithTransaction(
            routeMilkItem = sampleRouteMilkItems[1],
            milkTransaction = sampleMilkTransactions[1]
        )
    ),

    CustomerRouteWithDetails(
        routeItem = CustomerRouteItem(
            id = 303,
            customerId = 3,
            customerName = "Sharma",
            routeId = 1,
            sequenceNumber = 3,
            routeMilkId = 203
        ),
        customer = sampleCustomers[2],
        route = sampleRoute,
        routeMilkItemWithTransaction  = RouteMilkItemWithTransaction(
            routeMilkItem = sampleRouteMilkItems[2],
            milkTransaction = sampleMilkTransactions[2]
        )
    ),

    CustomerRouteWithDetails(
        routeItem = CustomerRouteItem(
            id = 304,
            customerId = 4,
            customerName = "Kiran",
            routeId = 1,
            sequenceNumber = 4,
            routeMilkId = 204
        ),
        customer = sampleCustomers[3],
        route = sampleRoute,
        routeMilkItemWithTransaction = RouteMilkItemWithTransaction(
            routeMilkItem = sampleRouteMilkItems[3],
            milkTransaction = sampleMilkTransactions[3]
        )
    )
)