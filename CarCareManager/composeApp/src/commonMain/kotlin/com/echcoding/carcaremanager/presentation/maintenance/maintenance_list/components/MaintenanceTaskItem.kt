package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.presentation.core.extensions.calculateStatus
import com.echcoding.carcaremanager.presentation.core.extensions.generateSubtitle
import com.echcoding.carcaremanager.presentation.core.mocks.maintenances
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.text.uppercase

@Composable
fun MaintenanceTaskItem(
    maintenance: Maintenance,
    currentOdometer: Int,
    onSelectClick:() -> Unit,
    modifier: Modifier = Modifier
) {
    val status: MaintenanceStatus = maintenance.calculateStatus(currentOdometer)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Status Icon Circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(status.backgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = status.icon,
                    contentDescription = null,
                    tint = status.mainColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = maintenance.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    // Status Badge
                    Surface(
                        color = status.backgroundColor,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = status.label.toString().uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = status.mainColor
                        )
                    }
                }

                Text(
                    text = maintenance.generateSubtitle(currentOdometer),
                    style = MaterialTheme.typography.bodyMedium,
                    color = status.mainColor,
                    fontWeight = FontWeight.Medium
                )

                if (maintenance.description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = maintenance.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MaintenanceTaskItemPreview() {
    MaintenanceTaskItem(
        maintenances[1],
        30000,
        onSelectClick = {}
    )
}