
package org.example.mandm.di

import org.example.mandm.AppDatabase
import org.example.mandm.dao.CustomerDao
import org.example.mandm.dao.MilkDao
import org.example.mandm.dao.MoneyDao
import org.example.mandm.dao.RouteDao
import org.example.mandm.repo.CustomerRepository
import org.example.mandm.repo.MilkRepository
import org.example.mandm.repo.MoneyRepository
import org.example.mandm.repo.RouteRepository
import org.koin.dsl.module

val dataModule = module {
    single<CustomerDao> { get<AppDatabase>().customerDao() }
    single<RouteDao> { get<AppDatabase>().routeDao() }
    single<MilkDao> { get<AppDatabase>().milkDao() }
    single<MoneyDao> { get<AppDatabase>().moneyDao() }
    single { get<AppDatabase>().userDao() }

    single { CustomerRepository(get()) }
    single { RouteRepository(get()) }
    single { MilkRepository(get()) }
    single { MoneyRepository(get()) }


}