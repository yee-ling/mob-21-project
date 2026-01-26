package com.example.mob21project.ui.screens.classDetails

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mob21project.ui.navigation.Screen

@Composable
fun ClassDetailsScreen(
    navController: NavController,
    viewModel: ClassDetailsViewModel = hiltViewModel()
) {
    val allClassDetails = viewModel.allClassDetails.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        viewModel.getAllClassDetails()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = "",
            onValueChange = {},
            placeholder = {Text("Search")},
            leadingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(allClassDetails) { classDetails ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                        .clickable { navController.navigate(Screen.ClassSessions(classDetails.id)) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(100.dp),
                            imageVector = Icons.Default.Star,
                            contentDescription = "",
                        )
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(classDetails.title)
                            Text(classDetails.description)
                            Text(classDetails.capacity.toString())
                        }
                    }
                }
            }
        }
    }
}