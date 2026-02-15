package com.echcoding.carcaremanager.presentation.core.utils

expect class AmountFormatter() {
    fun formatAmount(amount: Double): String
}

expect class CurrencyFormatter() {
    fun formatCurrency(amount: Double): String
}