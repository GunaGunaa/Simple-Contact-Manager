package com.example.jetpackcompose.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.jetpackcompose.CAApplication
import com.example.jetpackcompose.model.User
import com.example.jetpackcompose.model.UserData

class DetailsScreenViewModel : ViewModel() {
    var detailsScreenUiState by mutableStateOf(DetailsScreen())

    fun setUser(user: User?) {
        detailsScreenUiState = detailsScreenUiState.copy(user = user)
        if (user == null) {
            val userData = CAApplication.sharedPreferences.getUserData()
            detailsScreenUiState =
                detailsScreenUiState.copy(userData = userData)
            Log.d(TAG, "setUser: "+userData.first_name)
        }
    }
}

data class DetailsScreen(val user: User? = null, val userData: UserData? = null)