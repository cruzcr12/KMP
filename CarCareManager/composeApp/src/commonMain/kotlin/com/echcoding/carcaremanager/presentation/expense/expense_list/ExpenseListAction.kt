package com.echcoding.carcaremanager.presentation.expense.expense_list

import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.model.Vehicle

/**
 * This interface represents the intent in the MVI architecture.
 * The intent represents an user action or event that triggers a state change
 */
sealed interface ExpenseListAction {
    data object OnAddExpenseClick : ExpenseListAction
    data class OnEditExpenseClick(val expense: Expense) : ExpenseListAction
    data class OnActiveVehicleChanged(val vehicle: Vehicle?) : ExpenseListAction

}
