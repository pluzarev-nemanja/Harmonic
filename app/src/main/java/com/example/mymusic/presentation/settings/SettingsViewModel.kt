package com.example.mymusic.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.User
import com.example.mymusic.domain.use_cases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel() {

    var user by mutableStateOf(User(
        1,
        "User Name",
        "Auto",
        userImage = "",
        isSnowing = false
    ))

    private val _theme = MutableLiveData("Auto")
    val theme: LiveData<String> = _theme
    init {
        viewModelScope.launch {
            musicUseCases.getUser(1).collect{
                if(it.isNotEmpty()) {
                    user = it[0]
                    _theme.value = user.themeMode
                }
            }
        }
    }

    fun onThemeChanged(newTheme: String) {
        when (newTheme) {
            "Auto" -> {
                _theme.value = "Auto"
            }
            "Light" ->{
                _theme.value = "Light"
            }
            "Dark" -> {
                _theme.value = "Dark"
            }
        }
    }

    fun changeSnowing(isSnowing: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            musicUseCases.insertUser(
                user.copy(
                    isSnowing = isSnowing
                )
            )
        }
    }

    fun changeUserImage(userImage : String){
        viewModelScope.launch(Dispatchers.IO) {
            musicUseCases.insertUser(
                user.copy(
                    userImage = userImage
                )
            )
        }
    }
     fun changeTheme(theme:String){
        viewModelScope.launch(Dispatchers.IO) {
            musicUseCases.insertUser(
                user.copy(
                    themeMode = theme
                )
            )
        }
    }

    fun changeUserName(name:String){
        viewModelScope.launch(Dispatchers.IO) {
            musicUseCases.insertUser(
                user.copy(
                    userName = name
                )
            )
        }
    }
}