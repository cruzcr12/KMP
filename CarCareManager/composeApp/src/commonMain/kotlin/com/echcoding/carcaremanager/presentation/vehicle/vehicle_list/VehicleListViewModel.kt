package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VehicleListViewModel: ViewModel() {

    private val _state = MutableStateFlow(VehicleListState())
    val state = _state.asStateFlow()

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