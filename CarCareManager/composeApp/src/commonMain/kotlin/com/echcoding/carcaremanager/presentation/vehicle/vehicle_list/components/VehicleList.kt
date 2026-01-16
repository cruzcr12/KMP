package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.mocks.vehicles
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VehicleList(
    vehicles: List<Vehicle>,
    onAddVehicleClick: () -> Unit,
    onEditVehicleClick: (Vehicle) -> Unit,
    onDeleteVehicleClick: (Int?) -> Unit,
    onSelectVehicleClick: (Int?) -> Unit,
    padding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(
            items = vehicles,
            key = { it.id ?: -1 }
        ) { vehicle ->
            VehicleListItem(
                vehicle = vehicle,
                onDeleteClick = { onDeleteVehicleClick(vehicle.id) },
                onEditClick = { onEditVehicleClick(vehicle) },
                onSelectClick = { onSelectVehicleClick(vehicle.id) }
            )
        }


        item {
            AddNewVehicleButton(onClick = onAddVehicleClick)
            Spacer(modifier = Modifier
                .height(20.dp)
            )
        }


    }
}


@Preview(showBackground = true)
@Composable
fun VehicleListPreview() {
    VehicleList(
        vehicles = vehicles,
        onAddVehicleClick = {},
        onEditVehicleClick = {},
        onDeleteVehicleClick = {},
        onSelectVehicleClick = {}
    )
}
