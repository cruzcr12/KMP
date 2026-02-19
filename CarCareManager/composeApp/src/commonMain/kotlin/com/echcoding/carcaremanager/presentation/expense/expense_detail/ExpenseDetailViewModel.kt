package com.echcoding.carcaremanager.presentation.expense.expense_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.expense_deleted_error
import carcaremanager.composeapp.generated.resources.expense_saving_error
import carcaremanager.composeapp.generated.resources.unknown_error
import com.echcoding.carcaremanager.app.Route
import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.model.TypeOfService
import com.echcoding.carcaremanager.domain.repository.ExpenseRepository
import com.echcoding.carcaremanager.domain.repository.MaintenanceRepository
import com.echcoding.carcaremanager.presentation.core.utils.getCurrentLocalDate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class ExpenseDetailViewModel(
    private val expenseRepository: ExpenseRepository,
    private val maintenanceRepository: MaintenanceRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(ExpenseDetailState())
    val state = _state

    // Side Effect channel to display messages to the user
    private val _sideEffect = Channel<ExpenseDetailSideEffect>()
    val effects = _sideEffect.receiveAsFlow()

    init {
        // Get the route parameters and initialize the ViewModel accordingly
        val expenseId = savedStateHandle.toRoute<Route.ExpenseDetails>().expenseId
        val selectedVehicleId = savedStateHandle.toRoute<Route.ExpenseDetails>().selectedVehicleId
        val selectedVehicleOdometer = savedStateHandle.toRoute<Route.ExpenseDetails>().selectedVehicleOdometer
        val selectedVehicleOdometerUnit = savedStateHandle.toRoute<Route.ExpenseDetails>().selectedVehicleOdometerUnit

        if(expenseId != null && expenseId != 0L){
            loadExpense(expenseId)
        } else {
            createNewExpense(selectedVehicleId,  selectedVehicleOdometer, selectedVehicleOdometerUnit)
        }
        loadMaintenances(selectedVehicleId)
    }

    /**
     * Loads the list of maintenances from the database and updates the state
     */
    private fun loadMaintenances(vehicleId: Int) {
        viewModelScope.launch {
            //Update the state to indicate it is in the loading state
            _state.update { it.copy(isLoading = true) }
            if(vehicleId == null || vehicleId == 0) return@launch
            // Load the maintenances from the database
            maintenanceRepository.getMaintenancesForVehicle(vehicleId)
                .catch { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: getString(Res.string.unknown_error)
                        )
                    }
                }
                .collect { maintenances ->
                    _state.update { it.copy(maintenances = maintenances, isLoading = false, errorMessage = null) }
                }
        }
    }

    /**
     * Initialize a blank expense when the screen is created
     */
    private fun createNewExpense(vehicleId: Int, vehicleOdometer: Int, vehicleOdometerUnit: String) {
        _state.update{
            it.copy(
                expense = Expense(
                    id = null,
                    vehicleId = vehicleId,
                    maintenanceId = 0,
                    date = getCurrentLocalDate(),
                    mileage = vehicleOdometer,
                    mileageUnit = vehicleOdometerUnit,
                    amount = 0.0,
                    typeOfService = TypeOfService.MAINTENANCE,
                    note = ""
                ),
                isLoading = false,
                isEditing = false,
                isSaving = false
            )
        }
    }
    /**
     * Loads the expense from the database and updates the state
     */
    private fun loadExpense(expenseId: Long) {
        viewModelScope.launch {
            val expense = expenseRepository.getExpenseById(expenseId)
            _state.update {
                it.copy( expense = expense, isLoading = false, isEditing = true )
            }
        }
    }

    fun onAction(action: ExpenseDetailAction){
        when(action){
            is ExpenseDetailAction.OnSaveExpenseClick -> {
                saveExpense()
            }
            is ExpenseDetailAction.OnStateChange -> {
                _state.update { it.copy(expense = action.expense) }
            }
            is ExpenseDetailAction.OnDeleteExpenseClick -> {
                // Show confirmation dialog before deleting
                _state.update { it.copy(
                    expenseToDeleteId = action.expenseId,
                    showDeleteConfirmationDialog = true
                ) }
            }
            is ExpenseDetailAction.OnConfirmDeleteExpense -> {
                val idToDelete = _state.value.expenseToDeleteId ?: return
                executeDelete(idToDelete)
            }
            is ExpenseDetailAction.OnDismissDeleteDialog -> {
                _state.update { it.copy(showDeleteConfirmationDialog = false, expenseToDeleteId = null) }
            }
            is ExpenseDetailAction.OnShowMaintenancePicker -> {
                _state.update { it.copy(showMaintenancePicker = true) }
            }
            is ExpenseDetailAction.OnDismissMaintenancePicker -> {
                _state.update { it.copy(showMaintenancePicker = false) }
            }
            is ExpenseDetailAction.OnMaintenanceSelected -> {
                _state.update { it.copy(
                    showMaintenancePicker = false,
                    // Update the maintenanceId with the one selected by the user
                    expense = it.expense?.copy(maintenanceId = action.maintenance.id ?: 0L)
                )}
            }
            else -> Unit
        }
    }

    /**
     * Saves (insert or update) the expense to the database
     */
    private fun saveExpense() {
        val currentExpense = _state.value.expense
        if(currentExpense != null) {
            viewModelScope.launch {
                // Clear previous errors and show loading
                _state.update { it.copy(errorMessage = null, isSaving = true) }
                try {
                    expenseRepository.upsertExpense(currentExpense)
                    // if success
                    _state.update { it.copy(isSaving = false) }
                    // Navigate back to expense list
                    _sideEffect.send(ExpenseDetailSideEffect.NavigateBack)
                } catch (e: Exception) {
                    // Catch any errors and update state accordingly
                    _state.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = getString(Res.string.expense_saving_error, (e.message ?: Res.string.unknown_error))
                        )
                    }
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
                expenseRepository.deleteExpenseById(id)
                _state.update { it.copy(isLoading = false, expenseToDeleteId = null) }
                //Navigate back to expense list
                _sideEffect.send(ExpenseDetailSideEffect.NavigateBack)
            } catch (e: Exception) {
                // Catch any error and update state accordingly
                _state.update { it.copy(
                    isLoading = false,
                    errorMessage = getString(Res.string.expense_deleted_error, (e.message ?: Res.string.unknown_error))
                )}
            }
        }
    }


}
