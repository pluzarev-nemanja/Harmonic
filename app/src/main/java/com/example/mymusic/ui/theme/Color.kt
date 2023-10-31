package com.example.mymusic.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val darkGrey = Color(0xFF171C26)
val darkestBlue = Color(0xFF27374D)
val lightBlue = Color(0xFF526D82)
val softGrey = Color(0xFF9DB2BF)
val white = Color(0xFFDDE6ED)

val Colors.lightBlueToWhite
    get() = if(isLight) lightBlue else white
val Colors.lightBlueToDarkestBlue
    get() = if(isLight) lightBlue else darkestBlue
val Colors.darkestBlueToWhite
    get() = if (isLight) darkestBlue else white

val Colors.whiteToDarkGrey
    get() = if (isLight) white else darkGrey
val Colors.darkGreyToSoftGrey
    get() = if (isLight) darkGrey else softGrey
val Colors.whiteToDarkestBlue
    get() = if (isLight) white else darkestBlue
