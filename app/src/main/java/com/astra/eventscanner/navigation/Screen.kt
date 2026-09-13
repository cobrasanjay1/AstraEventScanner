package com.astra.eventscanner.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Events : Screen("events")
    object Scanner : Screen("scanner/{eventId}/{eventName}") {
        fun createRoute(eventId: String, eventName: String) = "scanner/$eventId/$eventName"
    }
    object Result : Screen("result")
    object Stats : Screen("stats/{eventId}/{eventName}") {
        fun createRoute(eventId: String, eventName: String) = "stats/$eventId/$eventName"
    }
    object Settings : Screen("settings")
}
