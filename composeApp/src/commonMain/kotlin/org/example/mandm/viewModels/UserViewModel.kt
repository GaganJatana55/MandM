package org.example.mandm.viewModels


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.mandm.domain.UserRepo

class UserViewModel(private val userRepo: UserRepo):ViewModel(){
var state by mutableStateOf("")
    private  set

    init {
        viewModelScope.launch {
            userRepo.insertUser()
        userRepo.getData().collect {
            state=it.toString()
        }
        }
    }
}