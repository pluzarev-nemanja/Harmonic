package com.example.mymusic.presentation.main

import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

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