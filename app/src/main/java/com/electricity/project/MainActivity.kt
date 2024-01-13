package com.electricity.project

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.electricity.project.api.aggregated.power.production.viewmodel.PowerProductionViewModel
import com.electricity.project.api.authorization.viewmodel.AuthorizationViewModel
import com.electricity.project.api.base.ApiResponse
import com.electricity.project.api.power.station.viewmodel.PowerStationViewModel
import com.electricity.project.api.token.TokenTypes
import com.electricity.project.api.token.TokenViewModel
import com.electricity.project.ui.screens.AppScreen
import com.electricity.project.ui.screens.LoginScreen
import com.electricity.project.ui.screens.MainView
import com.electricity.project.ui.theme.MobileapplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authorizationViewModel: AuthorizationViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by viewModels()
    private val powerProductionViewModel: PowerProductionViewModel by viewModels()
    private val powerStationViewModel: PowerStationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MobileapplicationTheme {
                val navController = rememberNavController()
                AuthorizationTokenListener()

                tokenViewModel.token.observe(LocalLifecycleOwner.current) { token ->
                    val currentBackStackEntry = navController.currentBackStackEntry
                    if (!currentBackStackEntry?.destination?.route.equals(AppScreen.LoginScreen.route)) {
                        if (token == null) {
                            return@observe navController.navigate(AppScreen.LoginScreen.route)
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = AppScreen.LoginScreen.route
                ) {
                    composable(route = AppScreen.LoginScreen.route) {
                        LoginScreen(
                            Modifier.fillMaxSize(),
                            navController,
                            authorizationViewModel,
                            tokenViewModel
                        )
                    }
                    composable(route = AppScreen.MainView.route) {
                        MainView(
                            Modifier.fillMaxSize(),
                            navController,
                            powerProductionViewModel,
                            powerStationViewModel,
                            tokenViewModel
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AuthorizationTokenListener() {
        val context = LocalContext.current
        authorizationViewModel.authorizationData.observe(LocalLifecycleOwner.current) {
            when (it) {
                is ApiResponse.Success -> {
                    tokenViewModel.saveToken(
                        TokenTypes.AUTHORIZATION_BEARER_TOKEN_KEY,
                        it.data.accessToken
                    )
                    tokenViewModel.saveToken(
                        TokenTypes.REFRESH_BEARER_TOKEN_KEY,
                        it.data.refreshToken
                    )
                }

                is ApiResponse.Failure -> {
                    Log.i("MainActivity", it.errorMessage)
                    Toast.makeText(context, R.string.incorrect_login_data, Toast.LENGTH_LONG).show()
                }

                else -> Log.i("MainActivity", it.toString())
            }
        }
    }

    override fun onStop() {
        tokenViewModel.clearTokens()
        super.onStop()
    }
}