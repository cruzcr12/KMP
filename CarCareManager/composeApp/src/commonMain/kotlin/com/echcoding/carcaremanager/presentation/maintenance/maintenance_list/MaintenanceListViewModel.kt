package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.maintenance_deleted
import carcaremanager.composeapp.generated.resources.maintenance_deleted_error
import carcaremanager.composeapp.generated.resources.unknown_error
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.domain.repository.MaintenanceRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class MaintenanceListViewModel(
    private val repository: MaintenanceRepository
): ViewModel() {

    private val _state = MutableStateFlow(MaintenanceListState())
    val state = _state
        .onStart {
            observeMaintenanceList()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    // This side effect channel allows to trigger the Snackbar
    private val _events = Channel<MaintenanceListSideEffect>()
    val events = _events.receiveAsFlow()

    fun onAction(action: MaintenanceListAction){
        when(action){
            is MaintenanceListAction.OnEditMaintenanceClick -> {

            }
            is MaintenanceListAction.OnAddMaintenanceClick -> {

            }
            is MaintenanceListAction.OnActiveVehicleChanged -> {
                setActiveVehicle(action.vehicle)
            }
        }
    }

    /**
     * Set or update the active vehicle and start observing the maintenance list
     */
    private fun setActiveVehicle(vehicle: Vehicle?){
        _state.update { it.copy(selectedVehicle = vehicle) }
        if(vehicle != null){
            observeMaintenanceList()
        }
    }

    /**
     * This function will observe the maintenance list and update the state accordingly
     */
    private fun observeMaintenanceList() {
        viewModelScope.launch {
            // Update the state to indicate the loading process is starting
            _state.update { it.copy(isLoading = true) }
            // If there are not active vehicles, don't load the maintenance list
            if(_state.value.selectedVehicle == null) {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }
            // Get the selected vehicle id to load the maintenance list
            val vehicleId = _state.value.selectedVehicle?.id?:0
            // Collect the flow from the repository
            repository.getMaintenancesForVehicle(vehicleId)
                .catch { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: getString(Res.string.unknown_error)
                        )
                    }
                }
                .collect { maintenanceList ->
                    _state.update { it.copy(
                        isLoading = false,
                        tasks = maintenanceList,
                        errorMessage = null
                    ) }
                }
            }
    }

}
