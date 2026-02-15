package com.echcoding.carcaremanager.presentation.core.utils

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.currentLocale

actual class AmountFormatter {
    actual fun formatAmount(amount: Double): String {
        val formatter = NSNumberFormatter()
        formatter.setLocale(NSLocale.currentLocale) // Use the current locale
        formatter.numberStyle = NSNumberFormatterCurrencyStyle // Formats with currency symbol and conventions
        return formatter.stringFromNumber(NSNumber(double = amount))!!
    }
}

actual class CurrencyFormatter {
    actual fun formatCurrency(amount: Double): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterCurrencyStyle
            locale = NSLocale.currentLocale()
        }
        return formatter.stringFromNumber(NSNumber(double = amount)) ?: ""
    }
}