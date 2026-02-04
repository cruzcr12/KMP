package com.echcoding.carcaremanager.presentation.core.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.back
import carcaremanager.composeapp.generated.resources.edit_vehicle
import com.echcoding.carcaremanager.presentation.core.Ebony
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * This composable is a reusable top bar for the detail screens.
 * The actions parameter allow to define a possible action like a delete button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    title: StringResource,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
){
    TopAppBar(
        title = {
            Text(
                text = stringResource(title),
                fontWeight = FontWeight.Bold,
                color = Ebony
            )
        },
        //modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back),
                    tint = Ebony
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
fun DetailTopBarPreview(){
    DetailTopBar(
        title = Res.string.edit_vehicle,
        onBackClick = {},
        modifier = Modifier
    )
}
