package io.trieulh.challenge.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = LightTaupe,
    primaryVariant = CopperRose,
    secondary = OldLace
)

@Composable
fun ChallengeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        shapes = Shapes,
        content = content
    )
}
