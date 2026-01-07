package com.echcoding.carcaremanager

import androidx.compose.ui.window.ComposeUIViewController
import com.echcoding.carcaremanager.app.App
import com.echcoding.carcaremanager.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}