package com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echcoding.carcaremanager.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel used to share the currently active vehicle across different screens.
 * Instead of manually setting the value, it observes the repository's active vehicle flow.
 */
class SelectedVehicleViewModel(
    private val repository: VehicleRepository
) : ViewModel() {
    // The stateIn allows to automatically syncs with the database state.
    // Whenever the database changes (upsert or setActiveVehicle), the state is updated
    val selectedVehicle = repository.getActiveVehicle()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
}
