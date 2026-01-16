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
import org.jetbrains.compose.resources.StringResource


sealed class MaintenanceStatus(
    val label: StringResource,
    val mainColor: Color,
    val backgroundColor: Color,
    val icon: ImageVector
) {
    data object Overdue : MaintenanceStatus(
        Res.string.overdue,
        Color(0xFFEF4444),
        Color(0xFFFEE2E2),
        Icons.Default.ErrorOutline)
    data object DueSoon : MaintenanceStatus(
        Res.string.due_soon,
        Color(0xFFF59E0B),
        Color(0xFFFEF3C7),
        Icons.Default.Schedule)
    data object Completed : MaintenanceStatus(
        Res.string.completed,
        Color(0xFF10B981),
        Color(0xFFD1FAE5),
        Icons.Default.CheckCircleOutline)
    data object Upcoming : MaintenanceStatus(
        Res.string.upcoming,
        Color(0xFF9CA3AF),
        Color(0xFFF3F4F6),
        Icons.Default.CalendarToday)
}