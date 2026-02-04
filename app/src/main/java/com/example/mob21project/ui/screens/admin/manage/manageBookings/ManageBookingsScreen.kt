package com.example.mob21project.ui.screens.admin.manage.manageBookings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mob21project.ui.utils.convertMillisToDate

@Composable
fun ManageBookingsScreen(
    navController: NavController,
    viewModel: ManageBookingsViewModel = hiltViewModel()
) {
    val manageBookingsUi = viewModel.bookings.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            if (manageBookingsUi.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No bookings yet"
                    )
                }
            } else {
                LazyColumn {
                    items(manageBookingsUi) {item ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {
                                Text(
                                    convertMillisToDate(item.booking.date)
                                )
                                Text(
                                    "Booked on: ${convertMillisToDate(item.booking.createdAt)}"
                                )
                                Text(
                                    "Booked by: ${item.user?.fullName ?: ""}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}