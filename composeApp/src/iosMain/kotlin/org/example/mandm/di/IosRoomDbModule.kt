package org.example.mandm.di

import org.example.mandm.AppDatabase
import org.example.mandm.DatabaseClass
import org.koin.dsl.module

actual val roomDbModule = module {
single<AppDatabase> { DatabaseClass().createDataBase() }
}