package com.example.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationComposable(context: Context) {
    val navController = rememberNavController()
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val isUserLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

    NavHost(navController = navController, startDestination = if (isUserLoggedIn) Home.route else Onboarding.route) {
        composable(Home.route) { Home(navController, context) }
        composable(Onboarding.route) { Onboarding(navController, context) }
        composable(Profile.route) { Profile(navController, context) }
    }
}