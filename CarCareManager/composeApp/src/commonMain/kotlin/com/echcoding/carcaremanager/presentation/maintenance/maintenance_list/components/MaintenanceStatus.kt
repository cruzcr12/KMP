package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.completed
import carcaremanager.composeapp.generated.resources.due_soon
import carcaremanager.composeapp.generated.resources.overdue
import carcaremanager.composeapp.generated.resources.upcoming
import com.echcoding.carcaremanager.themes.customapp.errorContainerLight
import com.echcoding.carcaremanager.themes.customapp.informativeContainerLight
import com.echcoding.carcaremanager.themes.customapp.informativeLight
import com.echcoding.carcaremanager.themes.customapp.onErrorContainerLight
import com.echcoding.carcaremanager.themes.customapp.onTertiaryContainerLight
import com.echcoding.carcaremanager.themes.customapp.tertiaryContainerLight
import org.jetbrains.compose.resources.StringResource


sealed class MaintenanceStatus(
    val label: StringResource,
    val mainColor: Color,
    val backgroundColor: Color,
    val icon: ImageVector
) {
    data object Overdue : MaintenanceStatus(
        Res.string.overdue,
        onErrorContainerLight,
        errorContainerLight,
        Icons.Default.ErrorOutline)
    data object DueSoon : MaintenanceStatus(
        Res.string.due_soon,
        informativeLight,
        informativeContainerLight,
        Icons.Default.Schedule)
    data object Completed : MaintenanceStatus(
        Res.string.completed,
        tertiaryContainerLight,
        onTertiaryContainerLight,
        Icons.Default.CheckCircleOutline)
    data object Upcoming : MaintenanceStatus(
        Res.string.upcoming,
        tertiaryContainerLight,
        onTertiaryContainerLight,
        Icons.Default.CalendarToday)
}