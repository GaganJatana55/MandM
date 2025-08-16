package org.example.mandm

import androidx.room.ConstructedBy
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.mandm.dataModel.User
@Dao
interface UserDao {

    @Upsert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Int): Flow<User?>

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>
}
@Database(entities = [User::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
internal const val dbFileName = "app_room_db.db"

