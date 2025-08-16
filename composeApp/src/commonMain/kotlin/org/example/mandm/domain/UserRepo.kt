package org.example.mandm.domain

import kotlinx.coroutines.flow.Flow
import org.example.mandm.dataModel.User

interface UserRepo {
    suspend fun  getData(): Flow<List<User>>
    suspend fun insertUser(): Long
}