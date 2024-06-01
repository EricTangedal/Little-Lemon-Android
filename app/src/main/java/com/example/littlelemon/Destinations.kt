package com.example.littlelemon

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

interface Destinations {
    val route: String
}

object Home : Destinations {
    override val route = "home"
}

object Onboarding : Destinations {
    override val route = "onboarding"
}

object Profile : Destinations {
    override val route = "profile"
}

@Composable
fun rememberNavigationController(): NavHostController = rememberNavController()