package io.trieulh.challenge.ui.screen.orchard.update

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import io.trieulh.challenge.R
import io.trieulh.challenge.ui.screen.orchard.update.destinations.OrchardUpdateScreenDestination
import io.trieulh.challenge.ui.theme.AnimatedTransition

@RootNavGraph
@Destination(style = AnimatedTransition::class)
@Composable
fun OrchardUpdateSuccessScreen(navigator: DestinationsNavigator) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Color.Green,
            modifier = Modifier.size(46.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.orchard_update_success_title),
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navigator.navigate(OrchardUpdateScreenDestination) {
                    launchSingleTop = true
                    popUpTo(NavGraphs.root)
                }
            }
        ) {
            Text(stringResource(id = R.string.update_again))
        }
    }
}

@Preview
@Composable
fun PreviewOrchardUpdateSuccessScreen() {
    OrchardUpdateSuccessScreen(EmptyDestinationsNavigator)
}
