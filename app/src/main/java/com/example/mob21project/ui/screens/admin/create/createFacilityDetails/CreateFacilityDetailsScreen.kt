package com.example.mob21project.ui.screens.admin.create.createFacilityDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mob21project.ui.navigation.Screen
import com.example.mob21project.ui.utils.convertTimeToMinutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFacilityDetailsScreen(
    navController: NavController,
    viewModel: CreateFacilityDetailsViewModel = hiltViewModel()
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    // opening time
    var showOpeningTimePicker by remember { mutableStateOf(false) }
    val openingTimePickerState = rememberTimePickerState()
    var openingTime by rememberSaveable { mutableStateOf("") }

    // closing time
    var showClosingTimePicker by remember { mutableStateOf(false) }
    val closingTimePickerState = rememberTimePickerState()
    var closingTime by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(openingTimePickerState.hour, openingTimePickerState.minute) {
        openingTime = "%02d:%02d".format(openingTimePickerState.hour, openingTimePickerState.minute)
    }
    LaunchedEffect(closingTimePickerState.hour, closingTimePickerState.minute) {
        closingTime = "%02d:%02d".format(closingTimePickerState.hour, closingTimePickerState.minute)
    }
    LaunchedEffect(Unit) {
        viewModel.success.collect {
            navController.navigate(Screen.ManageActivity)
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
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Title",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Pickleball Court") }
                )
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Description ...") }
                )
                TextField(
                    value = openingTime,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Opening Time")},
                    trailingIcon = {
                        IconButton(onClick = { showOpeningTimePicker = !showOpeningTimePicker }) {
                            Icon(Icons.Default.AccessTime, contentDescription = "")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(
                    visible = showOpeningTimePicker,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TimeInput(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            state = openingTimePickerState,
                        )
                    }
                }
                TextField(
                    value = closingTime,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Closing Time")},
                    trailingIcon = {
                        IconButton(onClick = { showClosingTimePicker = !showClosingTimePicker }) {
                            Icon(Icons.Default.AccessTime, contentDescription = "")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(
                    visible = showClosingTimePicker,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TimeInput(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            state = closingTimePickerState,
                        )
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    onClick = { viewModel.createFacilityDetails(
                        title = title,
                        description = description,
                        openingTime = convertTimeToMinutes(openingTimePickerState),
                        closingTime = convertTimeToMinutes(closingTimePickerState)
                    ) }
                ) {
                    Text("Create Facility Resource")
                }
            }
        }
    }
}