package com.example.unitrack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unitrack.screens.AssignmentsScreen
import com.example.unitrack.screens.GpaCalculatorScreen
import com.example.unitrack.screens.LoginScreen
import com.example.unitrack.screens.LostAndFoundScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // TODO: Check Firebase Auth state to decide startDestination
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // Navigate to the main screen after login, clearing the back stack
                    navController.navigate("gpa_calculator") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("gpa_calculator") {
            GpaCalculatorScreen(navController = navController)
        }

        composable("assignments") {
            AssignmentsScreen(navController = navController)
        }

        composable("lost_and_found") {
            LostAndFoundScreen(navController = navController)
        }
    }
}
