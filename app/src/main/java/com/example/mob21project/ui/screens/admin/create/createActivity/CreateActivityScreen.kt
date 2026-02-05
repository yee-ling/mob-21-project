package com.example.mob21project.ui.screens.admin.create.createActivity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mob21project.data.model.ActivityType
import com.example.mob21project.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateActivityScreen(
    navController: NavController,
    viewModel: CreateActivityViewModel = hiltViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    // List of Activity Types
    val activityTypes = viewModel.activityTypes
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.navigateEvent.collect { event ->
            when(event.type) {
                ActivityType.CLASS -> {
                    navController.navigate(Screen.CreateClassDetails(event.activityId))
                }
                ActivityType.FACILITY -> {
                    navController.navigate(Screen.CreateFacilityDetails(event.activityId))
                }
            }
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
                    text = "Activity Name",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Name") }
                )
                Text(
                    text = "Choose activity type",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedType.name,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        activityTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    viewModel.onTypeSelected(type)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    onClick = { viewModel.createActivity(name) }
                ) {
                    Text("Create")
                }
            }
        }
    }
}