package org.example.mandm.viewModels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.mandm.AppPreferences
import org.example.mandm.DispatcherProvider
import org.example.mandm.RoleType
import org.example.mandm.dataModel.LedgerItem
import org.example.mandm.dataModel.RouteEntity
import org.example.mandm.repo.RouteRepository

class RoutesViewModel(private val routeRepo: RouteRepository, private val dispatcher: DispatcherProvider):ViewModel(){
var state by mutableStateOf("")
    private  set

    var selectedRole by mutableStateOf<RoleType?>(null)
        private set
    var routesList = routeRepo.getAllRoutes().stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000),emptyList())


    init {
        AppPreferences.isAppFirstStimeInstalled().let {
            if (it){
                viewModelScope.launch(dispatcher.io) {
                    routeRepo.ensureDefaultRoutes()
                }
                AppPreferences.setAppFirstTimeInstalled(false)
            }
        }
    }


}