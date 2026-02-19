package com.echcoding.carcaremanager.presentation.expense.expense_detail

import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.model.Maintenance

sealed interface ExpenseDetailAction {
    data object OnBackClick: ExpenseDetailAction
    data object OnSaveExpenseClick: ExpenseDetailAction
    data class OnStateChange(var expense: Expense?): ExpenseDetailAction
    data class OnDeleteExpenseClick(var expenseId: Long?): ExpenseDetailAction
    data object OnConfirmDeleteExpense : ExpenseDetailAction
    data object OnDismissDeleteDialog : ExpenseDetailAction
    data object OnShowMaintenancePicker : ExpenseDetailAction
    data object OnDismissMaintenancePicker : ExpenseDetailAction
    data class OnMaintenanceSelected(val maintenance: Maintenance) : ExpenseDetailAction
}



