package com.bibek.uxcamsdk.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibek.coresdk.core.UxCam
import com.bibek.coresdk.core.UxCam.Events
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginInUiState())
    val uiState: StateFlow<LoginInUiState> = _uiState


    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onLoginClicked() {
        val currentState = _uiState.value

        if (currentState.email.isEmpty() || currentState.password.isEmpty()) {
            _uiState.value = currentState.copy(errorMessage = "Email and password cannot be empty.")
            return
        }

        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            UxCam.addEvent(
                Events.BUTTON_CLICK,
                mapOf("Name" to "Log In", "Email" to _uiState.value.email)
            )
            val success = callLoginAPI()
            if (success) {
                UxCam.addEvent(
                    Events.API_CALL,
                    mapOf("Name" to "Log In", "Email" to _uiState.value.email, "Status" to true)
                )
                _uiState.value = _uiState.value.copy(isSignedIn = true, isLoading = false)
            } else {
                UxCam.addEvent(
                    Events.API_CALL,
                    mapOf("Name" to "Log In", "Email" to _uiState.value.email, "Status" to false)
                )
                _uiState.value =
                    _uiState.value.copy(isLoading = false, errorMessage = "Email or Password Error")

            }
        }
    }

    fun onNavigatedToDashboard() {
        _uiState.value = LoginInUiState()
    }
}

fun callLoginAPI(): Boolean {
    return Random.nextBoolean()
}


