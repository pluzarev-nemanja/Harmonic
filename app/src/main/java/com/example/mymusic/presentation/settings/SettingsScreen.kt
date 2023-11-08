package com.example.mymusic.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.R
import com.example.mymusic.presentation.history.SimpleTopBar
import com.example.mymusic.presentation.main.MainViewModel
import com.example.mymusic.ui.theme.darkestBlueToWhite
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SettingsScreen(
    navController: NavController,
    userName: String,
    onThemeChange : (String) -> Unit,
    mainViewModel: MainViewModel
) {

    val theme: String by mainViewModel.theme.observeAsState("")

    Scaffold(
        topBar = {
            SimpleTopBar(navController, name = "Settings")
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()) {
            UserInfo(userName = userName)
            Spacer(modifier = Modifier.height(10.dp))
            ThemeOptions(
                onThemeChange = onThemeChange,
                theme = theme
            )
            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}


@Composable
fun UserInfo(
    userName: String
) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        GlideImage(
            modifier = Modifier
                .padding(5.dp)
                .size(170.dp)
                .clip(CircleShape)
                .clickable {

                },
            imageModel = { R.drawable.artist },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = userName, onValueChange = {

        }
        )
    }
}

@Composable
fun ThemeOptions(
    onThemeChange : (String) -> Unit,
    theme: String
) {

    Divider(color = MaterialTheme.colors.darkestBlueToWhite)

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Text(
            text = "Change your theme:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = MaterialTheme.colors.darkestBlueToWhite
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.End
        ) {
            LabeledRadioButton(
                label = "☀️",
                selected = theme == "Light",
                onClick = {
                    onThemeChange.invoke("Light")
                },
                name = "Light"
            )
            LabeledRadioButton(
                label = "🌘",
                selected = theme == "Dark",
                onClick = {
                    onThemeChange.invoke("Dark")
                },
                name = "Dark"
            )
            LabeledRadioButton(
                label = "🤖",
                selected = theme == "Auto",
                onClick = {
                    onThemeChange.invoke("Auto")
                },
                name = "Auto"
            )
        }
    }

}
@Composable
fun LabeledRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    name : String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6,
            fontSize = 15.sp,
            color = MaterialTheme.colors.darkestBlueToWhite
        )
        Text(label)
        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}