package com.example.trackmydiet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trackmydiet.ui.dashboard.*
import com.example.trackmydiet.ui.history.*
import com.example.trackmydiet.ui.onboarding.OnboardingScreen
import com.example.trackmydiet.ui.onboarding.OnboardingViewModel
import com.example.trackmydiet.ui.onboarding.OnboardingViewModelFactory
import com.example.trackmydiet.ui.profile.*
import com.example.trackmydiet.ui.theme.TrackMyDietTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as TrackMyDietApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackMyDietTheme {
                val user by mainViewModel.user.collectAsState()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val showBottomBar = currentDestination?.route in listOf("dashboard", "history", "profile")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Rounded.Home, contentDescription = null) },
                                    label = { Text("Home") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "dashboard" } == true,
                                    onClick = {
                                        navController.navigate("dashboard") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Rounded.History, contentDescription = null) },
                                    label = { Text("History") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "history" } == true,
                                    onClick = {
                                        navController.navigate("history") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Rounded.AccountCircle, contentDescription = null) },
                                    label = { Text("Profile") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "profile" } == true,
                                    onClick = {
                                        navController.navigate("profile") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = if (user == null) "onboarding" else "dashboard"
                        ) {
                            composable("onboarding") {
                                val onboardingViewModel: OnboardingViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                                    factory = OnboardingViewModelFactory((application as TrackMyDietApplication).userRepository)
                                )
                                OnboardingScreen(
                                    viewModel = onboardingViewModel,
                                    onComplete = {
                                        navController.navigate("dashboard") {
                                            popUpTo("onboarding") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("dashboard") {
                                val dashboardViewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                                    factory = DashboardViewModelFactory(
                                        (application as TrackMyDietApplication).userRepository,
                                        (application as TrackMyDietApplication).mealRepository
                                    )
                                )
                                DashboardScreen(
                                    viewModel = dashboardViewModel,
                                    onAddFoodClick = { navController.navigate("add_food") }
                                )
                            }
                            composable("history") {
                                val historyViewModel: HistoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                                    factory = HistoryViewModelFactory(
                                        (application as TrackMyDietApplication).userRepository,
                                        (application as TrackMyDietApplication).mealRepository
                                    )
                                )
                                HistoryScreen(
                                    viewModel = historyViewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("profile") {
                                val profileViewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                                    factory = ProfileViewModelFactory(
                                        (application as TrackMyDietApplication).userRepository,
                                        (application as TrackMyDietApplication).weightRepository
                                    )
                                )
                                ProfileScreen(viewModel = profileViewModel)
                            }
                            composable("add_food") {
                                val dashboardViewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                                    factory = DashboardViewModelFactory(
                                        (application as TrackMyDietApplication).userRepository,
                                        (application as TrackMyDietApplication).mealRepository
                                    )
                                )
                                AddFoodScreen(
                                    viewModel = dashboardViewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
