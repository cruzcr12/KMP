package com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.GasMeter
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.back
import carcaremanager.composeapp.generated.resources.edit_vehicle
import carcaremanager.composeapp.generated.resources.fuel_type
import carcaremanager.composeapp.generated.resources.license_plate
import carcaremanager.composeapp.generated.resources.license_plate_placeholder
import carcaremanager.composeapp.generated.resources.make
import carcaremanager.composeapp.generated.resources.make_placeholder
import carcaremanager.composeapp.generated.resources.model
import carcaremanager.composeapp.generated.resources.model_placeholder
import carcaremanager.composeapp.generated.resources.new_vehicle
import carcaremanager.composeapp.generated.resources.odometer
import carcaremanager.composeapp.generated.resources.vehicle_name
import carcaremanager.composeapp.generated.resources.vehicle_name_placeholder
import carcaremanager.composeapp.generated.resources.year
import carcaremanager.composeapp.generated.resources.year_placeholder
import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.presentation.core.AthensGray
import com.echcoding.carcaremanager.presentation.core.Ebony
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.components.DetailField
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VehicleDetailScreenRoot(
    onBackClick: () -> Unit
){
    VehicleDetailScreen(
        state = VehicleDetailState(),
        onStateChange = {},
        onSaveClick = {},
        onBackClick = onBackClick,
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    state: VehicleDetailState,
    onStateChange: (VehicleDetailState) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isEditing) stringResource(Res.string.edit_vehicle) else stringResource(Res.string.new_vehicle),
                        fontWeight = FontWeight.Bold,
                        color = Ebony
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                            tint = Ebony
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = AthensGray
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailField(
                label = stringResource(Res.string.vehicle_name),
                value = state.vehicle?.name,
                onValueChange = { onStateChange(state.copy(vehicle = state.vehicle?.copy(name = it))) },
                placeholder = stringResource(Res.string.vehicle_name_placeholder),
                icon = Icons.Default.DirectionsCar
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DetailField(
                    label = stringResource(Res.string.year),
                    value = state.vehicle?.year?.toString(),
                    onValueChange = { onStateChange(state.copy(vehicle = state.vehicle?.copy(year = it.toIntOrNull() ?: 0))) },
                    placeholder = stringResource(Res.string.year_placeholder),
                    icon = Icons.Default.Numbers,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                DetailField(
                    label = stringResource(Res.string.license_plate),
                    value = state.vehicle?.licensePlate,
                    onValueChange = { onStateChange(state.copy(vehicle = state.vehicle?.copy(licensePlate = it))) },
                    placeholder = stringResource(Res.string.license_plate_placeholder),
                    icon = Icons.Default.Pin,
                    modifier = Modifier.weight(1.5f)
                )
            }

            DetailField(
                label = stringResource(Res.string.make),
                value = state.vehicle?.maker,
                onValueChange = { onStateChange(state.copy(vehicle = state.vehicle?.copy(maker = it))) },
                placeholder = stringResource(Res.string.make_placeholder),
                icon = Icons.Default.Settings
            )

            DetailField(
                label = stringResource(Res.string.model),
                value = state.vehicle?.model,
                onValueChange = { onStateChange(state.copy(vehicle = state.vehicle?.copy(model = it))) },
                placeholder = stringResource(Res.string.model_placeholder),
                icon = Icons.Default.DirectionsCar
            )

            Column {
                Text(
                    text = stringResource(Res.string.fuel_type),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    FuelType.entries.forEachIndexed { index, fuelType ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = FuelType.entries.size),
                            onClick = { onStateChange(state.copy(vehicle = state.vehicle?.copy(fuelType = fuelType))) },
                            selected = state.vehicle?.fuelType == fuelType,
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = Color(0xFF2563EB),
                                activeContentColor = Color.White,
                                inactiveContainerColor = Color.White
                            )
                        ) {
                            Text(fuelType.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            }

            Column {
                Text(
                    text = stringResource(Res.string.odometer),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.vehicle?.odometer?.toString() ?: "",
                        onValueChange = { newValue ->
                            // 2. Filter out non-numeric characters if necessary, or just use toIntOrNull
                            val intValue = newValue.filter { it.isDigit() }.toIntOrNull() ?: 0
                            onStateChange(state.copy(vehicle = state.vehicle?.copy(odometer = intValue)))
                        },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(Icons.Default.GasMeter, contentDescription = null, tint = Color(0xFF9CA3AF))
                        },
                        placeholder = { Text("0") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.width(140.dp)) {
                        OdometerUnit.entries.forEachIndexed { index, unit ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
                                onClick = {  },
                                selected = state.vehicle?.odometerUnit == unit,
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = Color(0xFF2563EB),
                                    activeContentColor = Color.White,
                                    inactiveContainerColor = Color.White
                                )
                            ) {
                                Text(if (unit == OdometerUnit.MILES) "mi" else "km")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                ),
                enabled = !state.isSaving
            ) {
                Text(
                    text = if (state.isSaving) "Saving..." else "Save Vehicle",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



@Preview
@Composable
fun VehicleDetailScreenPreview() {
    VehicleDetailScreen(
        state = VehicleDetailState(),
        onStateChange = {},
        onSaveClick = {},
        onBackClick = {}
    )
}
