package io.trieulh.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.trieulh.challenge.ui.screen.orchard.update.OrchardUpdateScreen
import io.trieulh.challenge.ui.theme.ChallengeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeTheme {
                OrchardUpdateScreen()
            }
        }
    }
}
