package com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected

import androidx.lifecycle.ViewModel
import com.echcoding.carcaremanager.domain.model.Vehicle
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ViewModel used to share data between several views and avoid passing data through parameters
 * in navigation components.
 */
class SelectedVehicleViewModel: ViewModel() {
    private val _selectedVehicle = MutableStateFlow<Vehicle?>(null)
    val selectedVehicle = _selectedVehicle

    fun onSelectVehicle(vehicle: Vehicle?) {
        _selectedVehicle.value = vehicle
    }
}