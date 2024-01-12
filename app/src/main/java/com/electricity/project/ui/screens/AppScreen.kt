package com.electricity.project.ui.screens

sealed class AppScreen(
    val route: String
) {
    companion object {
        fun valueOf(route: String): AppScreen {
            return when (route) {
                LoginScreen.route -> LoginScreen
                MainView.route -> MainView
                else -> throw IllegalArgumentException("Incorrect value of route: $route")
            }
        }
    }

    object LoginScreen : AppScreen(
        ScreenNames.LOGIN.name
    )

    object MainView : AppScreen(
        ScreenNames.MAIN.name
    )
}

enum class ScreenNames {
    LOGIN, MAIN
}