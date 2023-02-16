package com.example.mymusic.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymusic.presentation.songs.HomeScreen
import com.example.mymusic.presentation.songs.SongsViewModel
import com.example.mymusic.presentation.util.isPermanentlyDenied
import com.example.mymusic.ui.theme.MyMusicTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMusicTheme {

                val permissionState = rememberPermissionState(
                    permission = Manifest.permission.READ_EXTERNAL_STORAGE
                )

                val lifecycleOwner = LocalLifecycleOwner.current

                DisposableEffect(key1 = lifecycleOwner, effect = {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            permissionState.launchPermissionRequest()
                        }

                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                })

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (permissionState.permission) {
                        Manifest.permission.READ_EXTERNAL_STORAGE -> {
                            when {
                                permissionState.hasPermission -> {

                                    val songsViewModel = viewModel(
                                        modelClass = SongsViewModel::class.java
                                    )
                                    val songList = songsViewModel.songList

                                    HomeScreen(
                                        progress = songsViewModel.currentAudioProgress.value,
                                        onProgressChange = {
                                            songsViewModel.seekTo(it)
                                        },
                                        isAudioPlaying = songsViewModel.isAudioPlaying,
                                        audioList = songList,
                                        currentPlayingAudio = songsViewModel
                                            .currentPlayingAudio.value,
                                        onStart = {
                                            songsViewModel.playAudio(it)
                                        },
                                        onItemClick = {
                                            Log.d("MainActivity","On item click from MainA")
                                            songsViewModel.playAudio(it)
                                        },
                                        onNext = {
                                            songsViewModel.skipToNext()
                                        }
                                    )
                                }
                                permissionState.shouldShowRationale -> {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = "Grant permission first to use this app")
                                    }
                                }
                                permissionState.isPermanentlyDenied() -> {
                                    Text(
                                        text = "Storage permission was permanently" +
                                                "denied. You can enable it in the app" +
                                                "settings."
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
