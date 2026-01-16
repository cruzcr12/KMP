package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.echcoding.carcaremanager.presentation.core.mocks.maintenances
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.components.MaintenanceStatus
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.components.MaintenanceTaskItem
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MaintenanceScreen(modifier: Modifier = Modifier) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add action to add new maintenance items */ },
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Build, contentDescription = "Add Service")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // First row works as a title for the maintenance tasks
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MAINTENANCE TASKS",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = Color(0xFFDBEafe),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "2 Actions",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2563EB),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            // List of maintenance tasks
            // Mock
            items(
                items = maintenances,
                key = { it.id ?: -1 }
            ){ maintenance ->
                MaintenanceTaskItem(
                    maintenance = maintenance,
                    currentOdometer = 30000,
                    onSelectClick = {}
                )
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun MaintenanceScreenPreview() {
    MaintenanceScreen()
}