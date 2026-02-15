package com.echcoding.carcaremanager.presentation.expense.expense_list

import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.model.Vehicle

/**
 * The UI in this project will use MVI architecture.
 * This class represents the Model or the entire state of the UI for the vehicle lis screen.
 * It bundles the state with all the actions that can be made in the View
 */
data class ExpenseListState(
    val selectedVehicle: Vehicle? = null, // The selected vehicle to show the expenses for
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = false, //Determines if the list of expenses is being fetched
    val errorMessage: String? = null, // Any error that might occur while fetching the expenses

)
