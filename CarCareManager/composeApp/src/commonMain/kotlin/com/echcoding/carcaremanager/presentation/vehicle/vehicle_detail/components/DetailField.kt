package com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.echcoding.carcaremanager.presentation.core.GrayChateau

@Composable
fun DetailField(
    label: String,
    value: String?,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value?: "",
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(icon, contentDescription = null, tint = GrayChateau, modifier = Modifier.size(20.dp))
            },
            placeholder = { Text(placeholder, color = GrayChateau) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color(0xFFE5E7EB)
            )
        )
    }
}