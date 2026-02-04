package com.echcoding.carcaremanager.presentation.maintenance.maintenance_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.echcoding.carcaremanager.app.Route
import com.echcoding.carcaremanager.domain.model.ControlType
import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.domain.repository.MaintenanceRepository
import com.echcoding.carcaremanager.presentation.core.getCurrentLocalDate
import io.ktor.client.request.invoke
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class MaintenanceDetailViewModel(
    private val maintenanceRepository: MaintenanceRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(MaintenanceDetailState())
    val state = _state

    // Side Effect channel to display messages to the user
    private val _sideEffect = Channel<MaintenanceDetailSideEffect>()
    val effects = _sideEffect.receiveAsFlow()

    init {
        val maintenanceId = savedStateHandle.toRoute<Route.MaintenanceDetails>().maintenanceId
        val selectedVehicleId = savedStateHandle.toRoute<Route.MaintenanceDetails>().selectedVehicleId

        if(maintenanceId != null && maintenanceId != 0L){
            loadMaintenance(maintenanceId)
        } else {
            createNewMaintenance(selectedVehicleId)
        }
    }


    /**
     * Initialize a blank maintenance when the screen is created
     */
    private fun createNewMaintenance(vehicleId: Int){
        _state.update{
            it.copy(
                maintenance = Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = "",
                    description = "",
                    initialOdometer = 0,
                    initialDate = getCurrentLocalDate(),
                    odometerInterval = 0,
                    dateInterval = 0,
                    controlType = ControlType.BOTH
                ),
                isLoading = false,
                isEditing = false,
                isSaving = false
            )
        }
    }

    /**
     * Loads the vehicle from the database and updates the state
     */
    private fun loadMaintenance(maintenanceId: Long){
        viewModelScope.launch {
            val maintenance = maintenanceRepository.getMaintenanceById(maintenanceId)
            _state.update {
                it.copy( maintenance = maintenance, isLoading = false, isEditing = true )
            }
        }
    }

    /**
     * Allows to update the initial date in the state with a new date
     */
    fun updateSelectedDate(newDate: LocalDate){
        _state.update{
            it.copy( maintenance = it.maintenance?.copy(initialDate = newDate))
        }
    }

    fun onAction(action: MaintenanceDetailAction){
        when(action){
            is MaintenanceDetailAction.OnSaveClick -> {
                val currentMaintenance = _state.value.maintenance
                if(currentMaintenance != null){
                    viewModelScope.launch {
                        // Clear previous errors and show loading
                        _state.update { it.copy(errorMessage = null, isSaving = true) }
                        try {
                            maintenanceRepository.upsertMaintenance(currentMaintenance)
                            // if success
                            _state.update { it.copy(isSaving = false) }
                            // Navigate back to maintenance list
                            _sideEffect.send(MaintenanceDetailSideEffect.NavigateBack)
                        }catch (e: Exception){
                            // Catch any errors and update state accordingly
                            _state.update { it.copy(
                                isSaving = false,
                                errorMessage = "There was a problem saving the maintenance task: ${e.message ?: "Unknown error"}"
                            )}
                        }
                    }
                }
            }
            is MaintenanceDetailAction.OnSelectedMaintenanceChange -> {
                _state.update { it.copy(maintenance = action.maintenance) }
            }
            is MaintenanceDetailAction.OnStateChange -> {
                _state.update { it.copy(maintenance = action.maintenance) }
            }
            is MaintenanceDetailAction.OnInitialDateChange -> {
                _state.update { it.copy( maintenance = it.maintenance?.copy(initialDate = action.date)) }
            }
            else -> Unit
        }
    }

}