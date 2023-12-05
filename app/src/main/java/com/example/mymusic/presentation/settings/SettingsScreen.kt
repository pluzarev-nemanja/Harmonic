package com.example.mymusic.presentation.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.R
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.history.SimpleTopBar
import com.example.mymusic.ui.theme.darkestBlueToWhite
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SettingsScreen(
    navController: NavController,
    userName: String,
    onThemeChange: (String) -> Unit,
    settingsViewModel: SettingsViewModel,
    equalizer: () -> Unit,
    changeUserName: (String) -> Unit,
    changeUserImage: (String) -> Unit,
    changeSnowing: (Boolean) -> Unit,
    userImage: String,
    isSnowEnable: Boolean,
    currentPlayingAudio: Song?
) {

    val theme: String by settingsViewModel.theme.observeAsState("")
    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )

    Scaffold(
        topBar = {
            SimpleTopBar(navController, name = "Settings")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(bottom = animatedHeight)
        ) {
            UserInfo(
                userName = userName,
                changeUserName,
                changeUserImage = changeUserImage,
                userImage = userImage
            )
            Spacer(modifier = Modifier.height(10.dp))
            ThemeOptions(
                onThemeChange = onThemeChange,
                theme = theme
            )
            Spacer(modifier = Modifier.height(10.dp))
            Equalizer(
                equalizer = equalizer
            )
            Spacer(modifier = Modifier.height(10.dp))
            Snowing(changeSnowing = changeSnowing, isSnowEnable)
            Spacer(modifier = Modifier.height(10.dp))
            About()
        }
    }
}

@Composable
fun About() {
    Divider(color = MaterialTheme.colors.darkestBlueToWhite)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Application version:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = MaterialTheme.colors.darkestBlueToWhite
        )
        Text(
            text = "1.0",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = MaterialTheme.colors.darkestBlueToWhite
        )

    }
}

@Composable
fun UserInfo(
    userName: String,
    changeUserName: (String) -> Unit,
    changeUserImage: (String) -> Unit,
    userImage: String
) {

    var filledText by remember {
        mutableStateOf(userName)
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            //here update that in database user image
            changeUserImage.invoke(uri.toString())
        }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            imageModel = {
                if (userImage != "") userImage
                else R.drawable.artist
            },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                value = filledText,
                onValueChange = {
                    filledText = it
                    changeUserName.invoke(filledText)
                },
                singleLine = true,
            )
            Icon(
                imageVector = Icons.Filled.Edit, contentDescription = "edit profile",
                tint = MaterialTheme.colors.darkestBlueToWhite
            )
        }
    }
}

@Composable
fun ThemeOptions(
    onThemeChange: (String) -> Unit,
    theme: String
) {

    Divider(color = MaterialTheme.colors.darkestBlueToWhite)

    Row(
        modifier = Modifier
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
fun Equalizer(
    equalizer: () -> Unit

) {

    Divider(color = MaterialTheme.colors.darkestBlueToWhite)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Enable Equalizer:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = MaterialTheme.colors.darkestBlueToWhite
        )
        IconButton(onClick = {
            equalizer.invoke()
        }) {
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "open")
        }
    }
}

@Composable
fun LabeledRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    name: String
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

@Composable
fun Snowing(
    changeSnowing: (Boolean) -> Unit,
    isSnowEnable: Boolean
) {

    var checked by remember { mutableStateOf(isSnowEnable) }


    Divider(color = MaterialTheme.colors.darkestBlueToWhite)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Enable snow effect:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = MaterialTheme.colors.darkestBlueToWhite
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                changeSnowing.invoke(it)
            }
        )
    }
}