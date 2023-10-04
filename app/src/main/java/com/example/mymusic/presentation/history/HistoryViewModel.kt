package com.example.mymusic.presentation.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.History
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel() {

    var history by mutableStateOf(History(emptyList(), 1))

    init {
        viewModelScope.launch {
           musicUseCases.getHistory(1).collect{
               if(it.isNotEmpty()) {
                   history = it[0]
               }
           }
        }
    }

    fun updateHistory(song: Song) {
        if (!history.songs.contains(song)){
            history.songs += song
            viewModelScope.launch {
                musicUseCases.insertHistory(history)
            }
        }
    }

}