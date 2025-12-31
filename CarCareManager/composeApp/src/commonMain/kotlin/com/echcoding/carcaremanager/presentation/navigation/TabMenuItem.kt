package com.echcoding.carcaremanager.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echcoding.carcaremanager.presentation.core.PaleSky
import com.echcoding.carcaremanager.presentation.core.RoyalBlue
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TabMenuItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean) {
    val color = if (isSelected) RoyalBlue else PaleSky
    Column(
        modifier = Modifier.padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = color,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(2.dp)
                    .background(color)
            )
        } else {
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabMenuItemPreview(){
    TabMenuItem("Services", Icons.Default.Build, false)
}