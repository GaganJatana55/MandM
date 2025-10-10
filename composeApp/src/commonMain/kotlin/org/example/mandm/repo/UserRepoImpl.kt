package org.example.mandm.repo
//
//import kotlinx.coroutines.flow.Flow
//import org.example.mandm.dataModel.User
//import org.example.mandm.AppDatabase
//import org.example.mandm.UserDao
//import org.example.mandm.domain.UserRepo
//
//class UserRepoImpl(val userDao: UserDao): UserRepo {
//    override suspend fun getData(): Flow<List<User>> {
//       return  userDao.getAllUsers()
//
//
//    }
//
//    override suspend fun insertUser(): Long {
////     return   userDao.insert(User(name = "Gagaaaagn"))
//        return -1
//    }
//}