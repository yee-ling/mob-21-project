package com.example.mob21project.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mob21project.ui.navigation.Screen

@Composable
fun AdminDashboardScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
                    .clickable { navController.navigate(Screen.CreateActivity) }
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Create new activity"
                )
                Spacer(Modifier.height(16.dp))
            }
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
                    .clickable { navController.navigate(Screen.ManageActivity) }
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Manage activity"
                )
                Spacer(Modifier.height(16.dp))
            }
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Manage users"
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}