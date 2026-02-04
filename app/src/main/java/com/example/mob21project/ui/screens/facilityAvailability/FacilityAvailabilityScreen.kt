package com.example.mob21project.ui.screens.facilityAvailability

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.setSelectedDate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mob21project.R
import com.example.mob21project.ui.utils.convertMillisToDate
import com.example.mob21project.ui.utils.minutesToTimeString
import com.example.mob21project.ui.utils.minutesToTimeStringTimeGrid
import com.example.mob21project.ui.utils.startOfDay
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilityAvailabilityScreen(
    navController: NavController,
    viewModel: FacilityAvailabilityViewModel = hiltViewModel()
) {
    val facilityDetails = viewModel.facilityDetails.collectAsStateWithLifecycle().value
    val timeSlots = viewModel.timeSlots.collectAsStateWithLifecycle().value
    val selectedSlots = viewModel.selectedSlots.collectAsStateWithLifecycle().value
    val disabledSlots = viewModel.disabledSlots.collectAsStateWithLifecycle().value

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: convertMillisToDate(System.currentTimeMillis())


    LaunchedEffect(Unit) {
        viewModel.getAllBookingsByFacilityId(
            datePickerState.selectedDateMillis ?: startOfDay(System.currentTimeMillis())
        )
    }
    // when selected date changes, it shows the bookings
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            viewModel.getAllBookingsByFacilityId(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.success.collect {
            viewModel.getAllBookingsByFacilityId(
                datePickerState.selectedDateMillis ?: startOfDay(System.currentTimeMillis())
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = facilityDetails?.imageUrl ?: "",
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
                    Text(facilityDetails?.title ?: "")
                    Text(facilityDetails?.description ?: "")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { showDatePicker = !showDatePicker }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "",
                    )
                    Text(
                        selectedDate
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TextButton(
                    onClick = {
                        datePickerState.setSelectedDate(
                            LocalDate.now()
                        )
                    }
                ) {
                    Text(
                        "Today"
                    )
                }
                TextButton(
                    onClick = {
                        datePickerState.setSelectedDate(
                            LocalDate.now().plusDays(1)
                        )
                    }
                ) {
                    Text(
                        "Tomorrow"
                    )
                }
            }
        }
        if(showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false},
                alignment = Alignment.TopStart,
                properties = PopupProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    focusable = true
                )
            ) {
                DatePicker(
                    modifier = Modifier.padding(16.dp),
                    state = datePickerState,
                    showModeToggle = false,
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth()
                    .height(260.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(timeSlots) { slot ->
                    val hour = slot/60
                    val minute = slot % 60
//                    val timeLabel = String.format("%02d:%02d", hour, minute)
                    val timeLabel = minutesToTimeStringTimeGrid(slot)
//                    val timeLabel = String.format("%d:%02d", hour, minute)

                    val isSelected = slot in selectedSlots
                    val isDisabled = slot in disabledSlots

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(timeLabel)
                        Spacer(Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.size(width = 50.dp, height = 200.dp)
                                .clickable(
                                    enabled = !isDisabled
                                ) {
                                    viewModel.onSlotClick(slot)
                                  },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                    isDisabled -> Color.Gray
                                    else -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                                }
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    if (isDisabled) {
                                        Icons.Default.Lock
                                    } else {
                                        Icons.Default.Add
                                    },
                                    "",
                                    modifier = Modifier.size(24.dp),
                                    tint = if (isDisabled) {
                                        Color.DarkGray
                                    } else {
                                        MaterialTheme.colorScheme.onSecondary
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        if(selectedSlots.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Start: ${minutesToTimeString(selectedSlots.first())}"
                    )
                    Text(
                        "End: ${minutesToTimeString(selectedSlots.last() + 30)}"
                    )
                }
                Button(
                    shape = RectangleShape,
                    onClick = { viewModel.clearSelection() }
                ) {
                    Text("Clear")
                }
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RectangleShape,
            onClick = {
                viewModel.addBooking(
                    date = datePickerState.selectedDateMillis ?: startOfDay(System.currentTimeMillis()),
                    startTime = selectedSlots.first(),
                    endTIme = (selectedSlots.last()+30)
                )
            }
        ) {
            Text(
                text = "Make Booking"
            )
        }
    }
}