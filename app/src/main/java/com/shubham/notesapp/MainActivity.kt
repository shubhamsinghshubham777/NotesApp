package com.shubham.notesapp

import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.shubham.notesapp.ui.screens.landing.LandingScreen
import com.shubham.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        setupSystemBars()

        setContent {
            NotesAppTheme {
                SetupSystemUiController()
                LandingScreen()
            }
        }
    }

    private fun setupSplashScreen() {
        installSplashScreen().setOnExitAnimationListener { splashScreenViewProvider ->
            val slideUpAnimation = ObjectAnimator.ofFloat(
                splashScreenViewProvider.view,
                View.TRANSLATION_Y,
                0f,
                -splashScreenViewProvider.view.height.toFloat(),
            )
            with(slideUpAnimation) {
                duration = 500
                interpolator = AnticipateInterpolator()
                doOnEnd { splashScreenViewProvider.remove() }
                start()
            }
        }
    }

    private fun setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = resources.getColor(R.color.transparent, theme)
        window.navigationBarColor = window.statusBarColor

        val shouldShowLightIcons =
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> true
                Configuration.UI_MODE_NIGHT_YES -> false
                else -> true
            }
        with(WindowCompat.getInsetsController(window, window.decorView)) {
            isAppearanceLightStatusBars = shouldShowLightIcons
            isAppearanceLightNavigationBars = shouldShowLightIcons
        }
    }
}

@Composable
fun SetupSystemUiController() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = isSystemInDarkTheme().not()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }
}