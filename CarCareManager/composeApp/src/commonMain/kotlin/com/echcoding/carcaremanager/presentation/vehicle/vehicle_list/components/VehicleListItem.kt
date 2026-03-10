package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.active
import carcaremanager.composeapp.generated.resources.odometer
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.extensions.formattedOdometer
import com.echcoding.carcaremanager.presentation.core.mocks.vehicles
import com.echcoding.carcaremanager.themes.customapp.CustomAppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VehicleListItem(
    vehicle: Vehicle,
    onEditClick:() -> Unit,
    onDeleteClick:() -> Unit,
    onSelectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isActive:Boolean = vehicle.active
    val backgroundColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
    val contentColor = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val secondaryContentColor = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val buttonsBackgroundColor = if (isActive) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.onTertiaryContainer
    val buttonsIconColor = if (isActive) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.tertiary


    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(if (isActive) 4.dp else 2.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(buttonsBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = buttonsIconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = vehicle.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                    Text(
                        text = "${vehicle.year} ${vehicle.maker} ${vehicle.model}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondaryContentColor
                    )
                }
                if (isActive) {
                    Surface(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.active),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left side: Odometer information
                Column {
                    Text(
                        text = stringResource(Res.string.odometer).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = secondaryContentColor,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = vehicle.formattedOdometer,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                        fontSize = 20.sp
                    )
                }
                // Right side: Action buttons
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Delete button
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(36.dp)
                            .background(buttonsBackgroundColor,
                                RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = buttonsIconColor,
                            modifier = Modifier.size(20.dp))
                    }

                    // Edit button
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(36.dp)
                            .background(buttonsBackgroundColor,RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = buttonsIconColor, modifier = Modifier.size(20.dp))
                    }

                    // Select/Active Button (Only show if NOT active)
                    if (!isActive) {
                        IconButton(
                            onClick = onSelectClick,
                            modifier = Modifier.size(36.dp)
                                .background(buttonsBackgroundColor,RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Select", tint = buttonsIconColor, modifier = Modifier.size(20.dp))
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
private fun VehicleListItemPreview() {
    CustomAppTheme {
        VehicleListItem(
            vehicle = vehicles[0],
            onDeleteClick = {},
            onEditClick = {},
            onSelectClick = {}
        )
    }
}

@Preview
@Composable
private fun VehicleListItemActivePreview() {
    CustomAppTheme {
        VehicleListItem(
            vehicle = vehicles[1],
            onDeleteClick = {},
            onEditClick = {},
            onSelectClick = {}
        )
    }
}
