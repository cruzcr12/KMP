package com.echcoding.carcaremanager.presentation.maintenance.maintenance_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.maintenance_deleted
import carcaremanager.composeapp.generated.resources.maintenance_deleted_error
import carcaremanager.composeapp.generated.resources.unknown_error
import com.echcoding.carcaremanager.app.Route
import com.echcoding.carcaremanager.domain.model.ControlType
import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.domain.repository.MaintenanceRepository
import com.echcoding.carcaremanager.presentation.core.getCurrentLocalDate
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.MaintenanceListSideEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

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
        val selectedVehicleOdometer = savedStateHandle.toRoute<Route.MaintenanceDetails>().selectedVehicleOdometer

        if(maintenanceId != null && maintenanceId != 0L){
            loadMaintenance(maintenanceId)
        } else {
            createNewMaintenance(selectedVehicleId, selectedVehicleOdometer)
        }
    }


    /**
     * Initialize a blank maintenance when the screen is created
     */
    private fun createNewMaintenance(vehicleId: Int, vehicleOdometer: Int){
        _state.update{
            it.copy(
                maintenance = Maintenance(
                    id = null,
                    vehicleId = vehicleId,
                    name = "",
                    description = "",
                    initialOdometer = vehicleOdometer,
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

    fun onAction(action: MaintenanceDetailAction){
        when(action){
            is MaintenanceDetailAction.OnSaveMaintenanceClick -> {
                saveMaintenance()
            }
            is MaintenanceDetailAction.OnSelectedMaintenanceChange -> {
                _state.update { it.copy(maintenance = action.maintenance) }
            }
            is MaintenanceDetailAction.OnStateChange -> {
                _state.update { it.copy(maintenance = action.maintenance) }
            }
            is MaintenanceDetailAction.OnDeleteMaintenanceClick -> {
                // Show confirmation dialog before deleting
                _state.update { it.copy(
                    maintenanceToDeleteId = action.maintenanceId,
                    showDeleteConfirmationDialog = true
                ) }
            }
            is MaintenanceDetailAction.OnConfirmDeleteMaintenance -> {
                val idToDelete = _state.value.maintenanceToDeleteId ?: return
                executeDelete(idToDelete)
            }
            is MaintenanceDetailAction.OnDismissDeleteDialog -> {
                _state.update { it.copy(showDeleteConfirmationDialog = false, maintenanceToDeleteId = null) }
            }
            else -> Unit
        }
    }

    /**
     * Saves (insert or update) the maintenance to the database
     */
    private fun saveMaintenance(){
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

    private fun executeDelete(id: Long) {
        viewModelScope.launch {
            // Clear previous errors and reset the confirmation dialog and show loading
            _state.update {
                it.copy(
                    isLoading = true,
                    showDeleteConfirmationDialog = false,
                    errorMessage = null
                )
            }
            try {
                maintenanceRepository.deleteMaintenanceById(id)
                _state.update { it.copy(isLoading = false, maintenanceToDeleteId = null) }
                // Send one-time event to show the Toast
                //_sideEffect.send(MaintenanceDetailSideEffect.ShowSnackBar(getString(Res.string.maintenance_deleted)))
                // Navigate back to maintenance list
                _sideEffect.send(MaintenanceDetailSideEffect.NavigateBack)
            } catch (e: Exception) {
                // Catch any error and update state accordingly
                _state.update { it.copy(
                    isLoading = false,
                    errorMessage = getString(Res.string.maintenance_deleted_error, (e.message ?: Res.string.unknown_error))
                )}
            }
        }
    }

}