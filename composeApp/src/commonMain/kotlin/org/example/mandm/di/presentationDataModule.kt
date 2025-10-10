package org.example.mandm.di

import org.example.mandm.viewModels.UserViewModel
import org.example.mandm.viewModels.CustomerViewModel
import org.example.mandm.viewModels.CreateCustomerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val roomDbModule: Module
val presentationDataModule = module{
    viewModelOf(::UserViewModel)
    viewModelOf(::CustomerViewModel)
    viewModelOf(::CreateCustomerViewModel)
}