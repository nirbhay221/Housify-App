package com.example.housify.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


const val CURRENT_UPDATEDSELECTEDTAB_KEY = "CURRENT_UPDATEDFIRSTNAME_KEY"

class NavigationViewModel (private val savedStateHandle: SavedStateHandle) : ViewModel() {
    var selectedTab: Int
        get() = savedStateHandle.get(CURRENT_UPDATEDSELECTEDTAB_KEY) ?: 1
        set(value) = savedStateHandle.set(CURRENT_UPDATEDSELECTEDTAB_KEY, value)

}