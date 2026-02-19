package com.echcoding.carcaremanager.presentation.expense.expense_detail

import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.model.Maintenance

data class ExpenseDetailState(
    val expense: Expense? = null,
    val maintenances: List<Maintenance> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val errorMessage: String? = null,
    val expenseToDeleteId: Long? = null, // When this is not null, show confirmation dialog
    val showDeleteConfirmationDialog: Boolean = false, // When this is true, show confirmation dialog
    val showMaintenancePicker: Boolean = false, // When this is true, show the maintenance list
    val snackbarMessage: String? = null // To display any informative message
)
