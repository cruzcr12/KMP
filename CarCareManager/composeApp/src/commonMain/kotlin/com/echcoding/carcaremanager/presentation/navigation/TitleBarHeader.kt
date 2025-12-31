package com.echcoding.carcaremanager.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.app_name
import com.echcoding.carcaremanager.presentation.core.AthensGray
import com.echcoding.carcaremanager.presentation.core.Ebony
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TitleBarHeader(){
    // Header
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Ebony
        )
        // Profile Image placeholder (Avatar)
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AthensGray),
            contentAlignment = Alignment.Center
        ) {
            // Using a simple text icon to represent the avatar in the screenshot
            Text("👩‍🔧", fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TitleBarHeaderPreview() {
    TitleBarHeader()
}