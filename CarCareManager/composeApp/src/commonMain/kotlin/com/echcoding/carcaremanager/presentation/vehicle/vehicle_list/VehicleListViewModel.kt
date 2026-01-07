package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echcoding.carcaremanager.domain.repository.VehicleRepository
import com.echcoding.carcaremanager.presentation.core.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
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

    // This function allows to observe the vehicles in the database and load all the
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
                        errorMessage = UiText.DynamicString(error.message ?: "Unknown error")
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

    fun onAction(action: VehicleListAction) {
        when (action) {
            is VehicleListAction.OnSelectVehicleClick -> {

            }
            is VehicleListAction.OnEditVehicle -> {

            }
            is VehicleListAction.OnDeleteVehicle -> {

            }
            is VehicleListAction.OnAddVehicleClick -> {

            }
            is VehicleListAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }



}