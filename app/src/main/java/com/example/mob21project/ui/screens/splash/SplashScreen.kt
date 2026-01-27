package com.example.mob21project.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .animateEnterExit(
                        // Slide in/out the inner box.
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    )
                    .sizeIn(minWidth = 256.dp, minHeight = 64.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    Icon(
                        modifier = Modifier.size(100.dp),
                        imageVector = Icons.Default.Hub,
                        contentDescription = ""
                    )
                    Text(
                        text = "App Name",
                        fontSize = 50.sp
                    )
                }
            }
        }
    }
}