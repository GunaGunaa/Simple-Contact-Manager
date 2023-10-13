package com.example.jetpackcompose.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.model.User
import com.example.jetpackcompose.repository.CAContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class ContactScreenViewModel @Inject constructor(private var repository: CAContactRepository) : ViewModel() {
    var contactListUiState by mutableStateOf(ContactList())

    @SuppressLint("SuspiciousIndentation")
    fun getContactList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getContactList(contactListUiState.currentPage).collect { value ->
                when (value) {
                    is List<*> -> {
                        contactListUiState = contactListUiState.copy(
                            contactList = contactListUiState.contactList + value as List<User>,
                            success = true,
                            isLoading = false,
                            currentPage = contactListUiState.currentPage + 1,
                            refresh = false
                        )
                        if (value.isEmpty()) {
                            contactListUiState = contactListUiState.copy(isLastPage = true)
                        }
                        contactListUiState = contactListUiState.copy(searchList = contactListUiState.contactList)
                    }
                    is Int -> contactListUiState = contactListUiState.copy(error = true, isLoading = false)
                }
            }
            Log.d(TAG, "getContactList:$response ")
        }
    }

    fun getSearchList() {
        contactListUiState = contactListUiState.copy(searchState = false)
        val filteredList = contactListUiState.contactList.filter {
            it.first_name.contains(contactListUiState.searchQuery, ignoreCase = true)
        }
        contactListUiState = contactListUiState.copy(searchList = filteredList)
        if (contactListUiState.searchQuery.isEmpty()) {
            contactListUiState = contactListUiState.copy(searchState = true)
        }
    }

    fun setSearchQuery(searchQuery: String) {
        contactListUiState = contactListUiState.copy(searchQuery = searchQuery)
    }
}

data class ContactList(
    val contactList: List<User> = emptyList(),
    val error: Boolean = false,
    val success: Boolean = false,
    val isLoading: Boolean = false,
    var isInitialLoad: Boolean = true,
    val currentPage: Int = 1,
    val isLastPage: Boolean = false,
    val moreData: Boolean = false,
    val searchList: List<User> = emptyList(),
    var searchState: Boolean = true,
    var searchQuery: String = "",
    var refresh: Boolean = false,
    var refreshProgress:Boolean=true,
    var paginationProgress:Boolean=true
)