package com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.no_vehicle_selected
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.AthensGray
import com.echcoding.carcaremanager.presentation.core.Ebony
import com.echcoding.carcaremanager.presentation.core.GrayChateau
import com.echcoding.carcaremanager.presentation.core.PaleSky
import com.echcoding.carcaremanager.presentation.core.RoyalBlue
import com.echcoding.carcaremanager.presentation.core.Zumthor
import com.echcoding.carcaremanager.presentation.core.formattedOdometer
import com.echcoding.carcaremanager.presentation.core.vehicles
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VehicleSelector(
    vehicle: Vehicle?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    // Active Vehicle Selector (Dropdown style)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(50.dp),
            border = BorderStroke(1.dp, AthensGray),
            color = Color.White,
            modifier = modifier
                .clickable(onClick = onClick)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Zumthor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                if(vehicle != null && vehicle.name.isNotBlank()){
                    Column {
                        Text(
                            vehicle.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Ebony
                        )
                        Text(
                            vehicle.formattedOdometer,
                            fontSize = 11.sp,
                            color = PaleSky
                        )
                    }
                }else{
                    Column {
                        Text(
                            stringResource(Res.string.no_vehicle_selected),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Ebony
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = GrayChateau,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleSelectorPreview(){
    VehicleSelector(
        vehicle = vehicles.first(),
        onClick = {}
    )
}