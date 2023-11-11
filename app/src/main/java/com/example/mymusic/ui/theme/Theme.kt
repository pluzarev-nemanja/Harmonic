package com.example.mymusic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
    val systemUiController = rememberSystemUiController()
    when (theme) {
        "Dark" -> systemUiController.setSystemBarsColor(
            color = Color(0xFF2D313A)
        )

        "Light" -> systemUiController.setSystemBarsColor(
            color = Color(0xFF9DB2BF)
        )

        else -> if (isSystemInDarkTheme())
            systemUiController.setSystemBarsColor(color = Color(0xFF2D313A)) else systemUiController.setSystemBarsColor(
            color = Color(0xFF9DB2BF)
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}