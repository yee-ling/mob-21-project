package com.example.mob21project.ui.screens.admin.create.createClassDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClassDetailsScreen(
    navController: NavController,
    viewModel: CreateClassDetailsViewModel= hiltViewModel()
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var durationMinutes by rememberSaveable { mutableStateOf("") }
    var capacity by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.success.collect {

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
                    placeholder = { Text("e.g., Yoga with ...") }
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
                Text(
                    text = "Class Duration (minutes)",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TextField(
                    value = durationMinutes,
                    onValueChange = {
                        if (it.all { it.isDigit() } || it.isEmpty()) {
                            durationMinutes = it
                        } },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., 60") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Text(
                    text = "Class Capacity (pax)",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TextField(
                    value = capacity,
                    onValueChange = {
                        if (it.all { it.isDigit() } || it.isEmpty()) {
                            capacity = it
                        } },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., 15") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    onClick = { viewModel.createClassDetails(
                        title = title,
                        description = description,
                        durationMinutes = durationMinutes.toDouble(),
                        capacity = capacity.toInt()
                    ) }
                ) {
                    Text("Create Class")
                }
            }
        }
    }
}