package com.example.mob21project.ui.screens.bookings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mob21project.R
import com.example.mob21project.ui.utils.convertMillisToDate
import com.example.mob21project.ui.utils.minutesToTimeString

@Composable
fun BookingsScreen(
    navController: NavController,
    viewModel: BookingsViewModel = hiltViewModel()
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedType by rememberSaveable { mutableStateOf(BookingType.CLASS)}

    val upcomingBookings = viewModel.upcomingBookings.collectAsStateWithLifecycle().value
    val cancelledBookings = viewModel.cancelledBookings.collectAsStateWithLifecycle().value
    val historyBookings = viewModel.historyBookings.collectAsStateWithLifecycle().value

    val filteredUpcomingBookings = remember(upcomingBookings, selectedType) {
        when(selectedType) {
            BookingType.CLASS -> upcomingBookings.filter { it.type == BookingType.CLASS }
            BookingType.FACILITY -> upcomingBookings.filter { it.type == BookingType.FACILITY }
        }
    }
    val filteredCancelledBookings = remember(cancelledBookings, selectedType) {
        when(selectedType) {
            BookingType.CLASS -> cancelledBookings.filter { it.type == BookingType.CLASS }
            BookingType.FACILITY -> cancelledBookings.filter { it.type == BookingType.FACILITY }
        }
    }
    val filteredHistoryBookings = remember(historyBookings, selectedType) {
        when(selectedType) {
            BookingType.CLASS -> historyBookings.filter { it.type == BookingType.CLASS }
            BookingType.FACILITY -> historyBookings.filter { it.type == BookingType.FACILITY }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            tabs = {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Upcoming") },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Cancelled") },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                )

                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = { Text("History") },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                )
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterChip(
                modifier = Modifier.weight(1f),
                selected = selectedType == BookingType.CLASS,
                onClick = { selectedType = BookingType.CLASS },
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = BookingType.CLASS.name,
                        textAlign = TextAlign.Center
                    )
                        },
                shape = RectangleShape
            )
            FilterChip(
                modifier = Modifier.weight(1f),
                selected = selectedType == BookingType.FACILITY,
                onClick = { selectedType = BookingType.FACILITY },
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = BookingType.FACILITY.name,
                        textAlign = TextAlign.Center
                    )
                        },
                shape = RectangleShape
            )
        }
        when(selectedTabIndex) {
            0 -> {
                BookingDetails(
                    bookings = filteredUpcomingBookings
                ) { bookingId ->
                    Button(
                        modifier = Modifier.fillMaxWidth()
                            .padding(
                                bottom = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                        shape = RectangleShape,
                        onClick = { viewModel.cancelBooking(bookingId) }
                    ) {
                        Text("Cancel Booking")
                    }
                }
            }
            1 -> {
                BookingDetails(filteredCancelledBookings) {}
            }
            2 -> {
                BookingDetails(filteredHistoryBookings) {}
            }
        }
    }
}
@Composable
fun BookingDetails(
    bookings: List<BookingUiModel>,
    content: @Composable ColumnScope.(String) -> Unit
) {
    if (bookings.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Nothing to display")
        }
    } else {
        LazyColumn(
        ) {
            items(bookings) { item ->
                val booking = item.booking
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.ic_imagesmode),
                            error = painterResource(R.drawable.ic_imagesmode)
                        )
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(item.title)
                            Text(convertMillisToDate(booking.date))
                            Text("${minutesToTimeString(booking.startTime)} - ${minutesToTimeString(booking.endTime)} ")
                            Text(booking.status.name)
                        }
                    }
                    content(booking.id)
                }
            }
        }
    }
}