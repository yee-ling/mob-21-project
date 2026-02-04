package com.example.mob21project.ui.screens.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mob21project.ui.navigation.Screen
import com.example.mob21project.ui.screens.auth.composables.EmailPassAuth

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.success.collect {
            navController.navigate(Screen.Home) {
                popUpTo(Screen.Login) {
                    inclusive = true
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.linearGradient(
                0.0f to MaterialTheme.colorScheme.primary,
                0.7f to MaterialTheme.colorScheme.tertiary,
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent.copy(alpha = 0.35f)
            ),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
        ) {
            EmailPassAuth(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                title = "Login",
                actionButtonText = "Login",
                actionButton = { email, password -> viewModel.signInWithEmail(email, password) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Don't have an account yet?",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    TextButton(
                        onClick = { navController.navigate(Screen.Register) }
                    ) {
                        Text(
                            "Sign Up"
                        )
                    }
                }
            }
        }
    }
}