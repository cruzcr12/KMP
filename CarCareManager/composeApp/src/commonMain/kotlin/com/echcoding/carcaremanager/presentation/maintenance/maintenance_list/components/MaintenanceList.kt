package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.actions_count
import carcaremanager.composeapp.generated.resources.maintenance_tasks
import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.presentation.core.extensions.calculateStatus
import com.echcoding.carcaremanager.presentation.core.mocks.getMockMaintenances
import com.echcoding.carcaremanager.themes.customapp.CustomAppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MaintenanceList(
    maintenanceTasks: List<Maintenance>,
    currentOdometer: Int,
    onEditMaintenanceClick: (Maintenance) -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
    scrollState: LazyListState = rememberLazyListState()
){
    // Calculate the number of overdue tasks
    val overdueTasks = maintenanceTasks
        .count { it.calculateStatus(currentOdometer) == MaintenanceStatus.Overdue }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        state = scrollState
    ) {
        // First row works as a title for the maintenance tasks
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.maintenance_tasks).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text =  stringResource(Res.string.actions_count, overdueTasks),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        // List of maintenance tasks
        items(
            items = maintenanceTasks,
            key = { it.id ?: -1 }
        ){ maintenance ->
            MaintenanceTaskItem(
                maintenance = maintenance,
                currentOdometer = currentOdometer,
                onEditClick = { onEditMaintenanceClick(maintenance) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MaintenanceListPreview() {
    CustomAppTheme {
        MaintenanceList(
            maintenanceTasks = getMockMaintenances(),
            currentOdometer = 130000,
            onEditMaintenanceClick = {}
        )
    }
}