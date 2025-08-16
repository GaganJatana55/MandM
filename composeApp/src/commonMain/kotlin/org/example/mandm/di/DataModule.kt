
package org.example.mandm.di

import org.example.mandm.AppDatabase
import org.example.mandm.AppDatabaseConstructor
import org.example.mandm.UserDao
import org.example.mandm.domain.UserRepo
import org.example.mandm.repo.UserRepoImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

//expect val platformDataModule: Module
val dataModule = module {
//    single<UserRepo> {
//        UserRepoImpl()
//    }
    singleOf(::UserRepoImpl).bind<UserRepo>()
    single<UserDao> { get<AppDatabase>().userDao() }
}