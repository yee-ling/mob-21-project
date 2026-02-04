package com.example.mob21project.ui.screens.admin.manage.manageUsers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mob21project.ui.utils.convertMillisToDate

@Composable
fun ManageUsersScreen(
    navController: NavController,
    viewModel: ManageUsersViewModel = hiltViewModel()
) {
    val users = viewModel.allUsers.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.getAllUsers()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column {
                    Text(
                        text = "Manage Users",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "All users",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(users) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.inversePrimary
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Text(
                                text = user.fullName,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = user.email,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = "Joined since ${convertMillisToDate(user.createdAt)}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}
