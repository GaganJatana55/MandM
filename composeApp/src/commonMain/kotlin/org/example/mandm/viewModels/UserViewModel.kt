package org.example.mandm.viewModels


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.mandm.domain.UserRepo
import org.example.mandm.AppPreferences
import org.example.mandm.RoleType

class UserViewModel(private val userRepo: UserRepo):ViewModel(){
var state by mutableStateOf("")
    private  set

    var selectedRole by mutableStateOf<RoleType?>(null)
        private set

    init {
        viewModelScope.launch {
            userRepo.insertUser()
        userRepo.getData().collect {
            state=it.toString()
        }
        }
    }

    fun updateSelectedRole(role: RoleType){
        selectedRole = role
    }

    fun completeSignUp(name: String, contact: String, village: String, address: String){
        // Persist minimal profile in preferences and mark as logged in
        AppPreferences.setUserName(name)
        AppPreferences.setContactNumber(contact)
        AppPreferences.setVillage(village)
        AppPreferences.setAddress(address)
        // Do NOT persist role; keep it session-only in ViewModel
        AppPreferences.setIsUserLoggedIn(true)
    }
}