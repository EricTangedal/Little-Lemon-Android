package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController, context: Context) {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(1f)
                .size(110.dp)
                .padding(vertical = 12.dp)
                .padding(top = 10.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    // Handle navigation to Profile screen
                }
        )
        Text("Home Screen")
        Button(onClick = { navController.navigate(Profile.route) }) {
            Text("Go to Profile")
        }
        Button(onClick = { navController.navigate(Onboarding.route) }) {
            Text("Go to Onboarding")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        Home(navController = NavController(LocalContext.current), context = LocalContext.current)
    }
}