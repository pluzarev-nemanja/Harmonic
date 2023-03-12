package com.example.mymusic.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymusic.presentation.permission.PermissionDialog
import com.example.mymusic.presentation.permission.ReadStoragePermissionTextProvider
import com.example.mymusic.presentation.search.SearchViewModel
import com.example.mymusic.presentation.songs.SongsScreen
import com.example.mymusic.presentation.songs.SongsViewModel
import com.example.mymusic.ui.theme.MyMusicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionsToRequest = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMusicTheme {

                val mainViewModel = viewModel<MainViewModel>()

                val dialogQue = mainViewModel.visiblePermissionDialogQue
                var granted by remember {
                    mutableStateOf(false)
                }

                val readStoragePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        mainViewModel.onPermissionResult(
                            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
                            isGranted = isGranted
                        )
                        granted = isGranted
                    }
                )

                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        permissionsToRequest.forEach { permission ->
                            mainViewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                            granted = perms[permission] == true
                        }
                    }
                )
                SideEffect {
                    readStoragePermissionResultLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    dialogQue
                        .reversed()
                        .forEach { permission ->
                            PermissionDialog(
                                permissionTextProvider = when (permission) {
                                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                                        ReadStoragePermissionTextProvider()
                                    }
                                    else -> return@forEach
                                },
                                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                    permission
                                ),
                                onDismiss = mainViewModel::dismissDialog,
                                onOkClick = {
                                    mainViewModel.dismissDialog()
                                    multiplePermissionResultLauncher.launch(
                                        arrayOf(permission)
                                    )
                                },
                                onGoToAppSettingsClick = ::openAppSettings
                            )
                        }

                    if(granted){
                        val songsViewModel = viewModel(
                            modelClass = SongsViewModel::class.java
                        )
                        val searchViewModel = viewModel<SearchViewModel>()

                        val songList = songsViewModel.songList

                        val searchText by searchViewModel.searchText.collectAsState()
                        val songs by searchViewModel.songs.collectAsState()


                        MainScreen(
                            audioList = songList,
                            onStart = {
                                songsViewModel.playAudio(it)
                            },
                            currentPlayingAudio = songsViewModel
                                .currentPlayingAudio.value,
                            isAudioPlaying = songsViewModel.isAudioPlaying,
                            searchText = searchText,
                            songs = songs,
                            onItemClick = {
                                songsViewModel.playAudio(it)
                            }
                        )
                    }

                }
            }
        }
    }

}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}



