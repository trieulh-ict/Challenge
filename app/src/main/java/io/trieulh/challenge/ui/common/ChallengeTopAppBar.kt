package io.trieulh.challenge.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.trieulh.challenge.ui.theme.LightTaupe

@Composable
fun ChallengeTopAppBar(onNavigationClick: (() -> Unit) = {}, title: String = "") {
    TopAppBar(
        content = {
            IconButton(
                onClick = onNavigationClick,
            ) {
                Icon(
                    Icons.Filled.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Text(
                text = title,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(end = 48.dp),
                style = MaterialTheme.typography.subtitle1
            )
        },
        backgroundColor = LightTaupe,
        modifier = Modifier
            .background(LightTaupe)
            .statusBarsPadding()
    )
}

@Preview
@Composable
fun PreviewChallengeTopAppBar() {
    ChallengeTopAppBar()
}