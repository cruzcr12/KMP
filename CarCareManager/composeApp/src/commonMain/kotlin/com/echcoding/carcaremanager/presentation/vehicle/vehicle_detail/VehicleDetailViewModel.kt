package com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.battery_check
import carcaremanager.composeapp.generated.resources.battery_check_description
import carcaremanager.composeapp.generated.resources.brake_fluid_check
import carcaremanager.composeapp.generated.resources.brake_fluid_check_description
import carcaremanager.composeapp.generated.resources.brake_pad_inspection
import carcaremanager.composeapp.generated.resources.brake_pad_inspection_description
import carcaremanager.composeapp.generated.resources.cabin_air_filter
import carcaremanager.composeapp.generated.resources.cabin_air_filter_description
import carcaremanager.composeapp.generated.resources.coolant_flush
import carcaremanager.composeapp.generated.resources.coolant_flush_description
import carcaremanager.composeapp.generated.resources.engine_air_filter
import carcaremanager.composeapp.generated.resources.engine_air_filter_description
import carcaremanager.composeapp.generated.resources.oil_change
import carcaremanager.composeapp.generated.resources.oil_change_description
import carcaremanager.composeapp.generated.resources.spark_plug_change
import carcaremanager.composeapp.generated.resources.spark_plug_change_description
import carcaremanager.composeapp.generated.resources.tire_pressure_check
import carcaremanager.composeapp.generated.resources.tire_pressure_check_description
import carcaremanager.composeapp.generated.resources.tire_rotation
import carcaremanager.composeapp.generated.resources.tire_rotation_description
import carcaremanager.composeapp.generated.resources.transmission_fluid
import carcaremanager.composeapp.generated.resources.transmission_fluid_description
import carcaremanager.composeapp.generated.resources.wheel_alignment
import carcaremanager.composeapp.generated.resources.wheel_alignment_description
import carcaremanager.composeapp.generated.resources.wiper_blade_check
import carcaremanager.composeapp.generated.resources.wiper_blade_check_description
import com.echcoding.carcaremanager.app.Route
import com.echcoding.carcaremanager.domain.model.ControlType
import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.domain.repository.MaintenanceRepository
import com.echcoding.carcaremanager.domain.repository.VehicleRepository
import com.echcoding.carcaremanager.presentation.core.utils.getCurrentLocalDate
import com.echcoding.carcaremanager.presentation.core.utils.getCurrentYear
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class VehicleDetailViewModel(
    private val repository: VehicleRepository,
    private val maintenanceRepository: MaintenanceRepository,
    savedStateHandle: SavedStateHandle //Automatically injected by Koin/Nav
): ViewModel() {
    private val _state = MutableStateFlow(VehicleDetailState())
    val state = _state

    // Side Effect channel
    private val _effects = Channel<VehicleDetailSideEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        val vehicleId: Int? = savedStateHandle.toRoute<Route.VehicleDetails>().vehicleId
        if (vehicleId != null && vehicleId != 0) {
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
    private fun loadVehicle(vehicleId: Int){
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
                            // Insert the default maintenance tasks only when the car is first added
                            if(currentVehicle.id == null){
                                saveDefaultMaintenances()
                            }
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

    // Saves the default maintenance tasks for a new vehicle
    private fun saveDefaultMaintenances(){
        viewModelScope.launch {
            val latestVehicle = repository.getLastVehicleAdded()
            val vehicleId = latestVehicle?.id ?: 1
            val vehicleOdometer = latestVehicle?.odometer ?: 0

            val defaultMaintenances = listOf(
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.oil_change)  ,
                    description = getString(Res.string.oil_change_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 5000,
                    dateInterval = 180,
                    controlType = ControlType.BOTH),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.tire_pressure_check),
                    description = getString(Res.string.tire_pressure_check_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 0,
                    dateInterval = 30,
                    controlType = ControlType.TIME),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.tire_rotation),
                    description = getString(Res.string.tire_rotation_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 5000,
                    dateInterval = 180,
                    controlType = ControlType.BOTH),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.engine_air_filter) ,
                    description = getString(Res.string.engine_air_filter_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 15000,
                    dateInterval = 365,
                    controlType = ControlType.BOTH),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.cabin_air_filter),
                    description = getString(Res.string.cabin_air_filter_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 15000,
                    dateInterval = 365,
                    controlType = ControlType.BOTH),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.brake_pad_inspection),
                    description = getString(Res.string.brake_pad_inspection_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 10000,
                    dateInterval = 180,
                    controlType = ControlType.BOTH),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.brake_fluid_check),
                    description = getString(Res.string.brake_fluid_check_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 25000,
                    dateInterval = 720,
                    controlType = ControlType.BOTH),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.wiper_blade_check),
                    description = getString(Res.string.wiper_blade_check_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 10000,
                    dateInterval = 180,
                    controlType = ControlType.BOTH),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.coolant_flush),
                    description = getString(Res.string.coolant_flush_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 30000,
                    dateInterval = 720,
                    controlType = ControlType.BOTH),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.transmission_fluid),
                    description = getString(Res.string.transmission_fluid_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 30000,
                    dateInterval = 0,
                    controlType = ControlType.MILEAGE),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.battery_check),
                    description = getString(Res.string.battery_check_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 0,
                    dateInterval = 1095,
                    controlType = ControlType.TIME),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.spark_plug_change),
                    description = getString(Res.string.spark_plug_change_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 30000,
                    dateInterval = 0,
                    controlType = ControlType.MILEAGE),
                Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = getString(Res.string.wheel_alignment),
                    description = getString(Res.string.wheel_alignment_description),
                    initialOdometer = vehicleOdometer,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 0,
                    dateInterval = 360,
                    controlType = ControlType.TIME),
            )

            maintenanceRepository.insertMultipleMaintenances(defaultMaintenances)
        }
    }


}