package com.example.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationComposable(context: Context, navController: NavHostController) {
    val sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val isUserLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

    NavHost(navController = navController, startDestination = if (isUserLoggedIn) Home.route else Onboarding.route) {
        composable(Home.route) { Home(navController, context) }
        composable(Onboarding.route) { Onboarding(navController, context) }
        composable(Profile.route) { Profile(navController, context) }
    }
}