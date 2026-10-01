package com.alagamb.petcompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.activity.SystemBarStyle
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alagamb.petcompose.data.preferences.ThemeMode
import com.alagamb.petcompose.ui.MainActivityUiState
import com.alagamb.petcompose.ui.MainViewModel
import com.alagamb.petcompose.ui.nav.AppNavGraph
import com.alagamb.petcompose.ui.theme.PetComposeTheme
import com.alagamb.petcompose.util.LocalSnackbarHostState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.uiState.value is MainActivityUiState.Loading
        }

        setContent {
            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

            when (val state = uiState) {
                is MainActivityUiState.Loading -> {}
                is MainActivityUiState.Success -> {
                    val isDarkTheme = when (state.themeMode) {
                        ThemeMode.SYSTEM -> isSystemInDarkTheme()
                        ThemeMode.LIGHT -> false
                        ThemeMode.DARK -> true
                    }

                    DisposableEffect(isDarkTheme) {
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.auto(
                                android.graphics.Color.TRANSPARENT,
                                android.graphics.Color.TRANSPARENT,
                            ) { isDarkTheme },
                            navigationBarStyle = SystemBarStyle.auto(
                                android.graphics.Color.TRANSPARENT,
                                android.graphics.Color.TRANSPARENT,
                            ) { isDarkTheme },
                        )
                        onDispose {}
                    }

                    PetComposeTheme(
                        darkTheme = isDarkTheme,
                        language = state.language
                    ) {
                        val snackbarHostState = remember { SnackbarHostState() }

                        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                                snackbarHost = {
                                    SnackbarHost(
                                        hostState = snackbarHostState,
                                        modifier = Modifier
                                            .navigationBarsPadding()
                                            .imePadding()
                                    )
                                }
                            ) { innerPadding ->
                                AppNavGraph(
                                    startDestination = state.startDestination,
                                    onToggleLanguage = { mainViewModel.toggleLanguage() },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .consumeWindowInsets(innerPadding)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
