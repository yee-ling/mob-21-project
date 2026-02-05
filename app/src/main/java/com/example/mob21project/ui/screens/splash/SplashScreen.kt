package com.example.mob21project.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mob21project.R
import com.example.mob21project.service.AuthService
import com.example.mob21project.ui.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavController
) {
    LaunchedEffect(Unit) {
        val nextScreen = if(AuthService.getInstance().getCurrentUser() != null) {
            Screen.Home
        } else {
            Screen.Login
        }
        navController.navigate(nextScreen) {
            popUpTo(Screen.Splash) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseSurface),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_pulse),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
                .height(330.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}