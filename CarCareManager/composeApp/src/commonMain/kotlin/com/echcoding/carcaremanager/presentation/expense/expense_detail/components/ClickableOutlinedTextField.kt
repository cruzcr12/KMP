package com.echcoding.carcaremanager.presentation.expense.expense_detail.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.echcoding.carcaremanager.themes.customapp.CustomAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ClickableOutlinedTextField(
    label: String,
    value: String?,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onClick: () -> Unit,
    icon: ImageVector,
    placeholder: String = "",
    isError: Boolean = false,
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Trigger the onClick callback when a press interaction is detected
    LaunchedEffect(isPressed){
        if(isPressed) onClick()
    }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value?: "",
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            interactionSource = interactionSource,
            trailingIcon = {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
            },
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.surfaceDim) },
            isError = isError,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            ),
            //isError = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClickableOutlinedTextFieldPreview() {
    CustomAppTheme {
        ClickableOutlinedTextField(
            label = "Label",
            value = "Value",
            onValueChange = {},
            placeholder = "Placeholder",
            icon = Icons.Default.ArrowDownward,
            onClick = {}
        )
    }
}