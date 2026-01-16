package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.echcoding.carcaremanager.domain.repository.VehicleRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleListViewModel(
    private val repository: VehicleRepository
): ViewModel() {

    private val _state = MutableStateFlow(VehicleListState())
    val state = _state
        .onStart {
            observeVehicles()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    // This side effect channel allows to trigger the snackbar
    private val _events = Channel<VehicleListSideEffect>()
    val events = _events.receiveAsFlow()

    fun onAction(action: VehicleListAction) {
        when (action) {
            is VehicleListAction.OnSelectVehicleClick -> {
                viewModelScope.launch {
                    repository.setActiveVehicle(action.vehicleId)
                }
            }
            is VehicleListAction.OnEditVehicle -> {

            }
            is VehicleListAction.OnDeleteVehicle -> {
                // Instead of deleting immediately, show the confirmation dialog state
                _state.update { it.copy(
                    vehicleToDeleteId = action.vehicleId,
                    showDeleteConfirmationDialog = true
                )}
            }
            is VehicleListAction.OnConfirmDeleteVehicle -> {
                val idToDelete = _state.value.vehicleToDeleteId ?: return
                executeDelete(idToDelete)
            }
            is VehicleListAction.OnDismissDeleteDialog -> {
                _state.update { it.copy(showDeleteConfirmationDialog = false, vehicleToDeleteId = null) }
            }
            is VehicleListAction.OnAddVehicleClick -> { }
            is VehicleListAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }

    private fun executeDelete(id: Int){
        viewModelScope.launch {
            // Clear previous errors and reset the confirmation dialog and show loading
            _state.update { it.copy(isLoading = true, showDeleteConfirmationDialog = false, errorMessage = null) }
            try {
                repository.deleteVehicleById(id)
                _state.update { it.copy(isLoading = false, vehicleToDeleteId = null) }
                // Send one-time event to show the Toast
                _events.send(VehicleListSideEffect.ShowSnackbar("Vehicle deleted successfully"))
            } catch (e: Exception) {
                // Catch any error and update state accordingly
                _state.update { it.copy(
                    isLoading = false,
                    errorMessage = "There was a problem deleting the vehicle: ${e.message ?: "Unknown error"}"
                )}
            }
        }
    }

    // This function allows to observe the vehicles in the database and loads all the
    // existing vehicles in the list of vehicles.
    private fun observeVehicles(){
        viewModelScope.launch {
            // Update the state to indicate the loading process
            _state.update { it.copy(isLoading = true) }

            // Collect the Flow from the repository
            repository.getAllVehicles()
                .catch { error ->
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Unknown error"
                    ) }
                }
                .collect { vehicles ->
                    _state.update { it.copy(
                        vehicles = vehicles,
                        isLoading = false,
                        // Select the first vehicle if none is selected
                        selectedVehicle = it.selectedVehicle ?: vehicles.firstOrNull()
                    )}
                }
        }
    }



}