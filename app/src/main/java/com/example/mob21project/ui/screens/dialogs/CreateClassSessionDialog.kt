package com.example.mob21project.ui.screens.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mob21project.data.model.CreateClassSessionFormData
import com.example.mob21project.ui.utils.convertMillisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClassSessionDialog(
    onDismiss: () -> Unit,
    onCreate: (CreateClassSessionFormData) -> Unit
) {
    // date
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: convertMillisToDate(System.currentTimeMillis())

    // start time
    var showStartTimePicker by remember { mutableStateOf(false) }
    val startTimePickerState = rememberTimePickerState()
    var startTime by rememberSaveable { mutableStateOf("") }

    // end time
    var showEndTimePicker by remember { mutableStateOf(false) }
    val endTimePickerState = rememberTimePickerState()
    var endTime by rememberSaveable { mutableStateOf("") }

    var durationMinutes by rememberSaveable { mutableStateOf("") }
    var capacity by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(startTimePickerState.hour, startTimePickerState.minute) {
        startTime = "%02d:%02d".format(startTimePickerState.hour, startTimePickerState.minute)
    }
    LaunchedEffect(endTimePickerState.hour, endTimePickerState.minute) {
        endTime = "%02d:%02d".format(endTimePickerState.hour, endTimePickerState.minute)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Create Session") },
                    navigationIcon = {
                        IconButton(
                            onClick = onDismiss
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {},
                        readOnly = true,
                        label = {Text("Select Date")},
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = !showDatePicker }) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = "")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    AnimatedVisibility(visible = showDatePicker) {
                        DatePicker(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            state = datePickerState,
                            showModeToggle = false,
                        )
                    }
                    OutlinedTextField(
                        value = durationMinutes,
                        onValueChange = { durationMinutes = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Duration") }
                    )
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = {},
                        readOnly = true,
                        label = {Text("Start Time")},
                        trailingIcon = {
                            IconButton(onClick = { showStartTimePicker = !showStartTimePicker }) {
                                Icon(Icons.Default.AccessTime, contentDescription = "")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    AnimatedVisibility(
                        visible = showStartTimePicker,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TimeInput(
                                modifier = Modifier
                                    .padding(top = 8.dp),
                                state = startTimePickerState,
                            )
                        }
                    }
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = {},
                        readOnly = true,
                        label = {Text("End Time")},
                        trailingIcon = {
                            IconButton(onClick = { showEndTimePicker = !showEndTimePicker }) {
                                Icon(Icons.Default.AccessTime, contentDescription = "")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    AnimatedVisibility(
                        visible = showEndTimePicker,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TimeInput(
                                modifier = Modifier
                                    .padding(top = 8.dp),
                                state = endTimePickerState,
                            )
                        }
                    }
                    OutlinedTextField(
                        value = capacity,
                        onValueChange = { capacity = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Capacity") }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton (
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RectangleShape,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Dismiss")
                        }
                        Button(
                            onClick = {
                                val formData = CreateClassSessionFormData(
                                    date = datePickerState.selectedDateMillis ?: return@Button,
                                    durationMinutes = durationMinutes.toDoubleOrNull() ?: return@Button,
                                    startTime = startTimePickerState.hour * 60 + startTimePickerState.minute,
                                    endTime = endTimePickerState.hour * 60 + startTimePickerState.minute,
                                    capacity = capacity.toIntOrNull() ?: return@Button,
                                )
                                if (formData.isValid()) {
                                    onCreate(formData)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RectangleShape
                        ) {
                            Text("Create")
                        }
                    }
                }
            }
        }
    }
}