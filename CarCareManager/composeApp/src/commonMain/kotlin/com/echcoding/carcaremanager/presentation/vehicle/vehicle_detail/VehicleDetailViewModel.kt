package com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.echcoding.carcaremanager.app.Route
import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.getCurrentYear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleDetailViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(VehicleDetailState())
    val state = _state

    init {
        val vehicleId: Int = savedStateHandle.toRoute<Route.VehicleDetails>().vehicleId
        if (vehicleId != -1) {
            //TODO load vehicle functionality
            //loadVehicle(vehicleId)
        } else {
            // Initialize a blank vehicle for 'Add Vehicle' feature
            _state.update { it.copy(
                vehicle = Vehicle(
                    id = -1,
                    name = "",
                    maker = "",
                    model = "",
                    year = getCurrentYear(),
                    licensePlate = "",
                    fuelType = FuelType.GAS,
                    odometer = 0,
                    odometerUnit = OdometerUnit.KILOMETERS,
                    active = false
                ),
                isLoading = false,
                isSaving = false,
                isEditing = false
            )}
        }
    }

    fun onAction(action: VehicleDetailAction) {
        when(action){
            is VehicleDetailAction.OnSaveVehicleClick -> {
                val currentVehicle = _state.value.vehicle
                if (currentVehicle != null){
                    viewModelScope.launch {
                        _state.value = _state.value.copy(isSaving =  true)
                        // Call the repository to save the data
                        // vehicleRepository.saveVehicle(currentVehicle)
                        _state.value = _state.value.copy(isSaving = false)
                        // Navigate back or show success
                    }

                }
            }
            is VehicleDetailAction.OnStateChange -> {
                _state.update {
                    it.copy(vehicle = action.vehicle)
                }
            }
            else -> Unit
        }
    }


}