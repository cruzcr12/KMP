package com.echcoding.carcaremanager.presentation.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel: ViewModel() {
    private val _state = MutableStateFlow(NavigationState())
    val state = _state

    fun onTabSelected(index: Int) {
        _state.update { it.copy(selectedTabIndex = index) }
    }
}