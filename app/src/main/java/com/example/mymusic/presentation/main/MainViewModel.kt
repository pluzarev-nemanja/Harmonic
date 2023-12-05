package com.example.mymusic.presentation.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val visiblePermissionDialogQue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQue.removeFirst()
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && !visiblePermissionDialogQue.contains(permission)) {
            visiblePermissionDialogQue.add(permission)
        }
    }
}