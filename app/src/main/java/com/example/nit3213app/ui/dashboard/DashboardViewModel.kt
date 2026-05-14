package com.example.nit3213app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nit3213app.data.api.models.Entity
import com.example.nit3213app.data.repository.AppRepository
import com.example.nit3213app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _entitiesState = MutableStateFlow<Resource<List<Entity>>>(Resource.Idle)
    val entitiesState: StateFlow<Resource<List<Entity>>> = _entitiesState.asStateFlow()

    fun loadDashboard(keypass: String) {
        viewModelScope.launch {
            _entitiesState.value = Resource.Loading
            try {
                val response = repository.getDashboard(keypass)
                _entitiesState.value = Resource.Success(response.entities)
            } catch (e: Exception) {
                _entitiesState.value = Resource.Error(
                    e.localizedMessage ?: "Could not load dashboard."
                )
            }
        }
    }
}
