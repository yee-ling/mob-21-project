package com.example.mob21project.ui.screens.classSessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Class
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mob21project.R
import com.example.mob21project.ui.utils.convertMillisToDate
import com.example.mob21project.ui.utils.convertMinutesToTimeString
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassSessionsScreen(
    navController: NavController,
    viewModel: ClassSessionsViewModel = hiltViewModel()
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: convertMillisToDate(System.currentTimeMillis())

    val classDetails = viewModel.classDetails.collectAsStateWithLifecycle().value
    val allClassSessions = viewModel.filteredClassSessions.collectAsStateWithLifecycle().value

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            viewModel.filterClassSessionsByDate(it)
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
                    model = classDetails?.imageUrl ?: "",
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
                    Text(classDetails?.title ?: "")
                    Text(
                        classDetails?.description ?: "",
                        maxLines = 3,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
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
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(allClassSessions) { classSession ->

                LaunchedEffect(classSession.id, classSession.date) {
                    viewModel.getClassSessionBookingsBySessionId(
                        sessionId = classSession.id,
                        date = classSession.date
                    )
                }
                val bookedCountMap = viewModel.bookedCountMap.collectAsStateWithLifecycle().value
                val bookedCount = bookedCountMap[classSession.id] ?: 0

                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(75.dp),
                            imageVector = Icons.Default.Class,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                convertMillisToDate(classSession.date)
                            )
                            Text(
                                "${convertMinutesToTimeString(classSession.startTime.toLong())} - ${convertMinutesToTimeString(classSession.endTime.toLong())}"
                            )
                            Text(
                                text = if (classSession.capacity != bookedCount) {
                                    "Availability: ${classSession.capacity - bookedCount} /${classSession.capacity}"
                                } else {
                                    "Fully booked"
                                }
                            )
                        }
                    }
                    if (
                        viewModel.canBook(session = classSession, bookedCount = bookedCount)
                    ) {
                        Button(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {
                                viewModel.addBooking(
                                    sessionId = classSession.id,
                                    date = classSession.date,
                                    startTime = classSession.startTime,
                                    endTime = classSession.endTime
                                )
                            },
                            shape = RectangleShape
                        ) {
                            Text("Reserve")
                        }
                    }
                }
            }
        }
    }
}
