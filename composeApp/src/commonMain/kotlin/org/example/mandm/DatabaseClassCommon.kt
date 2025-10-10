package org.example.mandm

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.example.mandm.dao.CustomerDao
import org.example.mandm.dao.MilkDao
import org.example.mandm.dao.MoneyDao
import org.example.mandm.dao.RouteDao
import org.example.mandm.dao.UserDao
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.dataModel.CustomerRouteEntity
import org.example.mandm.dataModel.MilkTransactionEditLogEntity
import org.example.mandm.dataModel.MilkTransactionEntity
import org.example.mandm.dataModel.MoneyTransactionEditLogEntity
import org.example.mandm.dataModel.MoneyTransactionEntity
import org.example.mandm.dataModel.RouteEntity
import org.example.mandm.dataModel.User

@Database(
    entities = [
        CustomerEntity::class,
        RouteEntity::class,
        CustomerRouteEntity::class,
        MilkTransactionEntity::class,
        MilkTransactionEditLogEntity::class,
        MoneyTransactionEntity::class,
        MoneyTransactionEditLogEntity::class,
        User::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun routeDao(): RouteDao
    abstract fun milkDao(): MilkDao
    abstract fun moneyDao(): MoneyDao
        abstract fun userDao(): UserDao
}

@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
internal const val dbFileName = "app_room_db.db"

