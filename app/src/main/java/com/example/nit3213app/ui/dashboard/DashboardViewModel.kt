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
import retrofit2.HttpException
import java.io.IOException
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
                val entities = repository.getDashboard(keypass)
                _entitiesState.value = Resource.Success(entities)
            } catch (e: HttpException) {
                _entitiesState.value = Resource.Error(
                    "Could not load dashboard: HTTP ${e.code()}"
                )
            } catch (e: IOException) {
                _entitiesState.value = Resource.Error(
                    "Network error: ${e.localizedMessage ?: "no connection"}"
                )
            } catch (e: Exception) {
                _entitiesState.value = Resource.Error(
                    e.localizedMessage ?: "Could not load dashboard."
                )
            }
        }
    }
}
