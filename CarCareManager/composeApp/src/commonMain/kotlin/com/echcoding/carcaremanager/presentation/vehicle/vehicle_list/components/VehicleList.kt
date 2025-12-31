package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.vehicles
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VehicleList(
    vehicles: List<Vehicle>,
    onAddVehicle: () -> Unit,
    onVehicleClick: (Vehicle) -> Unit,
    padding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(
            items = vehicles,
            key = { it.id }
        ) { vehicle ->
            VehicleListItem(
                vehicle = vehicle,
                isActive = vehicle.name == "Family Hauler",
                onClick = { onVehicleClick(vehicle) }
            )
        }

        item {
            AddNewVehicleButton(onClick = onAddVehicle)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun VehicleListPreview() {
    VehicleList(
        vehicles = vehicles,
        onAddVehicle = {},
        onVehicleClick = {}
    )
}
