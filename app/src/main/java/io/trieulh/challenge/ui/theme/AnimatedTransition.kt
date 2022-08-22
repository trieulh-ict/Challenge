package io.trieulh.challenge.ui.theme

import android.content.res.Resources
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object AnimatedTransition : DestinationStyle.Animated {
    private val screenWidth by lazy {
        Resources.getSystem().displayMetrics.widthPixels
    }

    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return slideInHorizontally(

            initialOffsetX = { screenWidth },
            animationSpec = tween(700)
        )
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return slideOutHorizontally(
            targetOffsetX = { -screenWidth },
            animationSpec = tween(700)
        )
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {

        return slideInHorizontally(
            initialOffsetX = { -screenWidth },
            animationSpec = tween(700)
        )
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {

        return slideOutHorizontally(
            targetOffsetX = { screenWidth },
            animationSpec = tween(700)
        )
    }
}
