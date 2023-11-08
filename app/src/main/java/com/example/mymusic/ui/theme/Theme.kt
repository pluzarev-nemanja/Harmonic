package com.example.mymusic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymusic.presentation.main.MainViewModel

private val DarkColorPalette = darkColors(
    primary = darkestBlue,
    primaryVariant = lightBlue,
    secondary = softGrey,
    background = darkGrey,
    surface = darkGrey,
    onPrimary = white
)

private val LightColorPalette = lightColors(
    primary = softGrey,
    primaryVariant = lightBlue,
    secondary = darkestBlue,
    background = white,
    surface = white,
    onPrimary = darkestBlue,
)

@Composable
fun MyMusicTheme(
    theme: String,
    content: @Composable () -> Unit
) {
    val colors = when (theme) {
        "Dark" -> DarkColorPalette
        "Light" -> LightColorPalette
        else -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}