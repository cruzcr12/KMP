package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.add_new_vehicle
import com.echcoding.carcaremanager.presentation.core.GrayChateau
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddNewVehicleButton(onClick: () -> Unit) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .drawBehind {
                drawRoundRect(
                    color = GrayChateau,
                    style = stroke,
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = GrayChateau,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                stringResource(Res.string.add_new_vehicle),
                color = GrayChateau,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddNewVehicleButtonPreview() {
    AddNewVehicleButton(onClick = {})
}