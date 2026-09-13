package com.astra.eventscanner.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.astra.eventscanner.data.api.ApiClient
import com.astra.eventscanner.data.repository.AuthRepository
import com.astra.eventscanner.data.repository.EventRepository
import com.astra.eventscanner.data.repository.TicketRepository
import com.astra.eventscanner.data.session.SessionManager
import com.astra.eventscanner.ui.events.EventScreen
import com.astra.eventscanner.ui.events.EventViewModel
import com.astra.eventscanner.ui.login.LoginScreen
import com.astra.eventscanner.ui.login.LoginViewModel
import com.astra.eventscanner.ui.result.ResultScreen
import com.astra.eventscanner.ui.scanner.ScannerScreen
import com.astra.eventscanner.ui.scanner.ScannerViewModel
import com.astra.eventscanner.ui.settings.SettingsScreen
import com.astra.eventscanner.ui.splash.SplashScreen
import com.astra.eventscanner.ui.statistics.StatsScreen
import com.astra.eventscanner.ui.statistics.StatsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val apiService = ApiClient.getApiService(context)
    val sessionManager = SessionManager(context)
    
    val authRepository = AuthRepository(apiService, sessionManager)
    val eventRepository = EventRepository(apiService)
    val ticketRepository = TicketRepository(apiService)

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = viewModel(factory = GenericViewModelFactory { LoginViewModel(authRepository) })
            LoginScreen(viewModel = viewModel, onLoginSuccess = {
                navController.navigate(Screen.Events.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Events.route) {
            val viewModel: EventViewModel = viewModel(factory = GenericViewModelFactory { EventViewModel(eventRepository) })
            EventScreen(
                viewModel = viewModel,
                onEventSelected = { event ->
                    navController.navigate(Screen.Scanner.createRoute(event.id.toString(), event.title))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.Scanner.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType },
                navArgument("eventName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            val eventName = backStackEntry.arguments?.getString("eventName") ?: ""
            val viewModel: ScannerViewModel = viewModel(factory = GenericViewModelFactory { ScannerViewModel(ticketRepository) })
            
            val scanResult by viewModel.scanResult.collectAsState()
            
            Box {
                ScannerScreen(
                    selectedEventId = eventId,
                    eventName = eventName,
                    viewModel = viewModel,
                    onResultNavigate = { },
                    onStatsClick = {
                        navController.navigate(Screen.Stats.createRoute(eventId.toString(), eventName))
                    }
                )

                if (scanResult != null) {
                    ResultScreen(response = scanResult!!) {
                        viewModel.resetScanner()
                    }
                }
            }
        }

        composable(
            route = Screen.Stats.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType },
                navArgument("eventName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            val eventName = backStackEntry.arguments?.getString("eventName") ?: ""
            val viewModel: StatsViewModel = viewModel(factory = GenericViewModelFactory { StatsViewModel(eventRepository) })
            StatsScreen(eventId = eventId, eventName = eventName, viewModel = viewModel)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onChangeEvent = {
                    navController.navigate(Screen.Events.route)
                }
            )
        }
    }
}

class GenericViewModelFactory<T : ViewModel>(private val creator: () -> T) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }
}
