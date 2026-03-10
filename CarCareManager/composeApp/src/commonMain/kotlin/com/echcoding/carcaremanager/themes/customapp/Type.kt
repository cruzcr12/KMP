package com.echcoding.carcaremanager.themes.customapp

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.lexend
import carcaremanager.composeapp.generated.resources.roboto_regular
import org.jetbrains.compose.resources.Font



@Composable
fun getTypography(): Typography {
    val customFontFamily = FontFamily(
        Font(Res.font.lexend, FontWeight.Normal)
    )
    val baseline = Typography()
    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = customFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = customFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = customFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = customFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = customFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = customFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = customFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = customFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = customFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = customFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = customFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = customFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = customFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = customFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = customFontFamily),
    )
}

