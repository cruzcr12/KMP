package com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.echcoding.carcaremanager.app.Route
import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.domain.repository.VehicleRepository
import com.echcoding.carcaremanager.presentation.core.getCurrentYear
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleDetailViewModel(
    private val repository: VehicleRepository,
    savedStateHandle: SavedStateHandle //Automatically injected by Koin/Nav
): ViewModel() {
    private val _state = MutableStateFlow(VehicleDetailState())
    val state = _state

    // Side Effect channel
    private val _effects = Channel<VehicleDetailSideEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        val vehicleId: Long? = savedStateHandle.toRoute<Route.VehicleDetails>().vehicleId
        if (vehicleId != null && vehicleId != 0L) {
            loadVehicle(vehicleId)
        } else {
            createNewVehicle()
        }
    }

    /**
     * Initialize a blank vehicle for 'Add Vehicle' feature
     */
    private fun createNewVehicle(){
        _state.update {
            it.copy(
                vehicle = Vehicle(
                    id = null,
                    name = "",
                    maker = "",
                    model = "",
                    year = getCurrentYear(),
                    licensePlate = "",
                    fuelType = FuelType.GAS,
                    odometer = 0,
                    odometerUnit = OdometerUnit.KILOMETERS,
                    active = true
                ),
                isLoading = false,
                isSaving = false,
                isEditing = false
            )
        }
    }

    /**
     * Loads the vehicle from the repository and updates the state
     */
    private fun loadVehicle(vehicleId: Long){
        viewModelScope.launch {
            val vehicle = repository.getVehicleById(vehicleId)
            _state.update {
                it.copy(vehicle = vehicle, isEditing = true, isLoading = false)
            }
        }
    }

    fun onAction(action: VehicleDetailAction) {
        when(action){
            is VehicleDetailAction.OnSaveVehicleClick -> {
                val currentVehicle = _state.value.vehicle
                if (currentVehicle != null){
                    viewModelScope.launch {
                        // Clear previous errors and show loading
                        _state.update { it.copy(isSaving = true, errorMessage = null) }
                        try {
                            repository.upsertVehicle(currentVehicle)
                            // Success
                            _state.update { it.copy(isSaving = false) }
                            // Navigate back to the list screen
                            _effects.send(VehicleDetailSideEffect.NavigateBack)
                        } catch (e: Exception) {
                            // Catch any error and update state accordingly
                            _state.update { it.copy(
                                isSaving = false,
                                errorMessage = "There was a problem adding the vehicle: ${e.message ?: "Unknown error"}"
                            )}
                        }
                    }

                }
            }
            is VehicleDetailAction.OnSelectedVehicleChange -> {
                _state.update { it.copy(vehicle = action.vehicle )}
            }
            is VehicleDetailAction.OnStateChange -> {
                _state.update { it.copy(vehicle = action.vehicle) }
            }
            else -> Unit
        }
    }


}