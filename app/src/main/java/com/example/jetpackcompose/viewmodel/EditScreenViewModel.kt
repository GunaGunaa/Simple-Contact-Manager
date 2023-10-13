package com.example.jetpackcompose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EditScreenViewModel : ViewModel() {
    var editScreenUiState by mutableStateOf(EditScreen())
    private set
    fun setAddress(address: String) {
        editScreenUiState = editScreenUiState.copy(street = address)
    }

}

data class EditScreen(var street: String = "")