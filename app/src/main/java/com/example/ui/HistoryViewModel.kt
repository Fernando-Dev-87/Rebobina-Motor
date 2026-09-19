package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.RewindService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val rewindDao = db.rewindDao()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val allServices: StateFlow<List<RewindService>> = combine(
        rewindDao.getAllServices(),
        _searchQuery
    ) { services, query ->
        if (query.isBlank()) services
        else services.filter { 
            it.clientName.contains(query, ignoreCase = true) || 
            it.motorDescription.contains(query, ignoreCase = true) 
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteService(service: RewindService) {
        viewModelScope.launch {
            rewindDao.deleteService(service)
        }
    }
}
