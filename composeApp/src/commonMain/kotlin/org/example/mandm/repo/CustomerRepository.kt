package org.example.mandm.repo

import org.example.mandm.dao.CustomerDao
import org.example.mandm.dataModel.CustomerEntity
import kotlinx.coroutines.flow.Flow

class CustomerRepository(
    private val customerDao: CustomerDao
) {
    suspend fun insertCustomer(customer: CustomerEntity): Long = customerDao.insertCustomer(customer)
    suspend fun insertCustomers(customers: List<CustomerEntity>): List<Long> = customerDao.insertCustomers(customers)
    suspend fun updateCustomer(customer: CustomerEntity): Int = customerDao.updateCustomer(customer)
    suspend fun deleteCustomer(customer: CustomerEntity): Int = customerDao.deleteCustomer(customer)
    fun getCustomerById(id: Long): Flow<CustomerEntity?> = customerDao.getCustomerById(id)
    fun getAllCustomers(): Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    fun getActiveCustomers(): Flow<List<CustomerEntity>> = customerDao.getActiveCustomers()
    fun searchCustomersByNameOrPhone(query: String): Flow<List<CustomerEntity>> =
        customerDao.searchCustomersByNameOrPhone("%$query%")
}


