package com.example.mymusic.presentation.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {


    private val _theme = MutableLiveData("Auto")
    val theme: LiveData<String> = _theme

    fun onThemeChanged(newTheme: String) {
        when (newTheme) {
            "Auto" -> _theme.value = "Auto"
            "Light" -> _theme.value = "Light"
            "Dark" -> _theme.value = "Dark"
        }
    }

    val visiblePermissionDialogQue = mutableStateListOf<String>()

    fun dismissDialog(){
        visiblePermissionDialogQue.removeFirst()
    }

    fun onPermissionResult(permission: String, isGranted: Boolean){
        if(!isGranted && !visiblePermissionDialogQue.contains(permission)){
            visiblePermissionDialogQue.add(permission)
        }
    }
}