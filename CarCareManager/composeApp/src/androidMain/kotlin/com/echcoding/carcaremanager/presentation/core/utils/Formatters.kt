package com.echcoding.carcaremanager.presentation.core.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

actual class AmountFormatter {
    actual fun formatAmount(amount: Double): String {
        // Use NumberFormat.getCurrencyInstance() for full currency formatting
        // or DecimalFormat for specific patterns.
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault()) as DecimalFormat
        // Customize as needed, e.g., to ensure 2 decimal places
        formatter.applyPattern("#,##0.00") // Example pattern for two fixed decimal places
        return formatter.format(amount)
    }
}

actual class CurrencyFormatter {
    actual fun formatCurrency(amount: Double): String {
        // Use the default locale for the device
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return formatter.format(amount)
    }
}