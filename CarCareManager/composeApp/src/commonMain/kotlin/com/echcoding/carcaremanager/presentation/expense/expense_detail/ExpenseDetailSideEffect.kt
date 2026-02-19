package com.echcoding.carcaremanager.presentation.expense.expense_detail

interface ExpenseDetailSideEffect {
    data object NavigateBack : ExpenseDetailSideEffect
    data class ShowSnackBar(val message: String) : ExpenseDetailSideEffect
}