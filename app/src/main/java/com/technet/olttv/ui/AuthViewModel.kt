package com.technet.olttv.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    private val appPin = "870128"

    var enteredPin by mutableStateOf("")
        private set

    var isAuthenticated by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onDigit(digit: String) {
        if (enteredPin.length >= 6) return
        enteredPin += digit
        errorMessage = null
    }

    fun onBackspace() {
        if (enteredPin.isNotEmpty()) {
            enteredPin = enteredPin.dropLast(1)
        }
        errorMessage = null
    }

    fun clear() {
        enteredPin = ""
        errorMessage = null
    }

    fun submit() {
        if (enteredPin == appPin) {
            isAuthenticated = true
            errorMessage = null
        } else {
            errorMessage = "PIN inválido"
            enteredPin = ""
        }
    }

    fun logout() {
        isAuthenticated = false
        enteredPin = ""
        errorMessage = null
    }
}
