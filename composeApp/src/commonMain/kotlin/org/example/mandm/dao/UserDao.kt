package org.example.mandm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
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


