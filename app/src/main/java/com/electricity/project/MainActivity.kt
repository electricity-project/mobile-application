package com.electricity.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.electricity.project.ui.screens.AppScreen
import com.electricity.project.ui.screens.MainView
import com.electricity.project.ui.theme.MobileapplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileapplicationTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = AppScreen.LoginScreen.route){
                    composable(route = AppScreen.LoginScreen.route){
                        LoginScreen(Modifier.fillMaxSize(), navController)
                    }
                    composable(route = AppScreen.MainView.route){
                        MainView(Modifier.fillMaxSize())
                    }
                }


            }
        }
    }
}