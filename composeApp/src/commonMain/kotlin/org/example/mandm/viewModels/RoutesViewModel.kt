package org.example.mandm.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.mandm.AppPreferences
import org.example.mandm.DispatcherProvider
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.dataModel.CustomerRouteItem
import org.example.mandm.repo.RouteRepository

class RoutesViewModel(
    private val routeRepo: RouteRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    // --- 1. EXISTING FUNCTIONALITY (Preserved) ---
    init {
        AppPreferences.isAppFirstStimeInstalled().let {
            if (it) {
                viewModelScope.launch(dispatcher.io) {
                    routeRepo.ensureDefaultRoutes()
                }
                AppPreferences.setAppFirstTimeInstalled(false)
            }
        }
    }

    // Observed for the main list of routes (Preserved)
    val routesList = routeRepo.getAllRoutes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000), emptyList())

    // --- 2. DRAFT STATE MANAGEMENT ---

    // The "Draft" list the UI interacts with
    val customerRouteItems = mutableStateListOf<CustomerRouteItem>()

    // Tracks items that were in the DB but removed by the user during this session
    private val itemsToDelete = mutableListOf<CustomerRouteItem>()

    private var currentRouteId: Int = -1

    fun loadRoute(routeId: Int) {
        if (currentRouteId == routeId) return
        currentRouteId = routeId
        viewModelScope.launch {
            // Fetch once as a snapshot (first() closes the flow)
            val items = routeRepo.getCustomerRouteItems(routeId).first()

            customerRouteItems.clear()
            customerRouteItems.addAll(items.sortedBy { it.sequenceNumber })
            itemsToDelete.clear() // Clear delete buffer for new route
        }
    }

    // --- 3. UI INTERACTIONS (Local Only) ---

    fun addCustomersAtPosition(customers: List<CustomerEntity>, index: Int) {
        val newItems = customers.map {
            CustomerRouteItem(
                customerId = it.userId,
                customerName = it.userName,
                routeId = currentRouteId,
                sequenceNumber = 0 // Fixed in reindexLocal
            )
        }

        if (index >= customerRouteItems.size) {
            customerRouteItems.addAll(newItems)
        } else {
            customerRouteItems.addAll(index, newItems)
        }
        reindexLocal()
    }

    fun onMove(fromIndex: Int, toIndex: Int) {
        if (fromIndex !in customerRouteItems.indices || toIndex !in customerRouteItems.indices) return
        val item = customerRouteItems.removeAt(fromIndex)
        customerRouteItems.add(toIndex, item)
        reindexLocal()
    }

    fun deleteItem(item: CustomerRouteItem) {
        customerRouteItems.remove(item)
        // If the item has a valid DB ID, track it for deletion on save
        if (item.id != 0L) {
            itemsToDelete.add(item)
        }
        reindexLocal()
    }

    /**
     * Updates the sequence numbers in the RAM list only.
     * This keeps the UI numbers (1, 2, 3...) correct during editing.
     */
    private fun reindexLocal() {
        val updatedList = customerRouteItems.mapIndexed { i, item ->
            item.copy(sequenceNumber = i)
        }
        customerRouteItems.clear()
        customerRouteItems.addAll(updatedList)
    }

    // --- 4. PERSISTENCE (The Save Button) ---

    /**
     * Called when the user clicks the "Save" button.
     * Flushes all changes to the database in one batch.
     */
    fun saveChanges(onComplete: () -> Unit) {
        viewModelScope.launch(dispatcher.io) {
            // 1. Physically delete items removed from the list
            itemsToDelete.forEach {
                routeRepo.deleteCustomerRouteItem(it)
            }

            // 2. Batch Update/Insert all current items
            // Converting to list because SnapshotStateList isn't thread-safe for DB
            routeRepo.upsertCustomerRouteItems(customerRouteItems.toList())

            // 3. Re-fetch to sync fresh Database IDs (for newly added users)
            val freshList = routeRepo.getCustomerRouteItems(currentRouteId).first()

            withContext(dispatcher.main) {
                customerRouteItems.clear()
                customerRouteItems.addAll(freshList.sortedBy { it.sequenceNumber })
                itemsToDelete.clear()
                onComplete()
            }
        }
    }
}