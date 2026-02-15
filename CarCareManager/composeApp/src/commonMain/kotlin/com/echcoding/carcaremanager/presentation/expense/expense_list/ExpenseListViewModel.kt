package com.echcoding.carcaremanager.presentation.expense.expense_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.unknown_error
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class ExpenseListViewModel(
    private val repository: ExpenseRepository
): ViewModel() {

    private val _state = MutableStateFlow(ExpenseListState())
    val state = _state
        .onStart {
            observeExpenseList()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            _state.value
        )


    fun onAction(action: ExpenseListAction){
        when(action){
            is ExpenseListAction.OnEditExpenseClick -> {}
            is ExpenseListAction.OnAddExpenseClick -> {}
            is ExpenseListAction.OnActiveVehicleChanged ->{
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
            observeExpenseList()
        }
    }

    /**
     * This function will observe the expenses and update the state accordingly
     */
    private fun observeExpenseList(){
        viewModelScope.launch {
            // Update the state to indicate the loading process is starting
            _state.update { it.copy(isLoading = true) }
            // If there are not active vehicles, don't load the expenses list
            if(_state.value.selectedVehicle == null) {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }
            //Get the selected vehicle id to load the expenses list
            val vehicleId = _state.value.selectedVehicle?.id?:0
            //Collect the flow from the repository
            repository.getExpensesByVehicle(vehicleId)
                .catch { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: getString(Res.string.unknown_error)
                        )
                    }
                }
                .collect{ expenseList ->
                    _state.update { it.copy(
                        isLoading = false,
                        expenses = expenseList,
                        errorMessage = null
                    ) }
                }
        }
    }

}