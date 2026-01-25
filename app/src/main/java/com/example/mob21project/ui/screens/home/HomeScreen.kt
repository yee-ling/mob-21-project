package com.example.mob21project.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mob21project.ui.navigation.Screen

@Composable
fun HomeScreen(
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.height(24.dp))
                Text("Hello Home Screen")
                Spacer(Modifier.height(24.dp))
            }
            Text("Upcoming this week")
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(10) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Spacer(Modifier.height(24.dp))
                        Text("Placeholder")
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
            Text("Explore our offerings")
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.weight(1f).padding(16.dp)
                        .clickable {navController.navigate(Screen.ClassDetails)}
                ) {
                    Spacer(Modifier.height(24.dp))
                    Text("Explore our classes")
                    Spacer(Modifier.height(24.dp))
                }
                Card(
                    modifier = Modifier.weight(1f).padding(16.dp)
                        .clickable {navController.navigate(Screen.FacilityDetails)}
                ) {
                    Spacer(Modifier.height(24.dp))
                    Text("Explore our facilities")
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}