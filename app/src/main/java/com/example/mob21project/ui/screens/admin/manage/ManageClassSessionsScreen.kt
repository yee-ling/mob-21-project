package com.example.mob21project.ui.screens.admin.manage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mob21project.R
import com.example.mob21project.data.model.ClassSession
import com.example.mob21project.ui.navigation.Screen
import com.example.mob21project.ui.screens.dialogs.CreateClassSessionDialog
import com.example.mob21project.ui.utils.convertMillisToDate
import com.example.mob21project.ui.utils.convertMinutesToTimeString

@Composable
fun ManageClassSessionsScreen(
    navController: NavController,
    viewModel: ManageClassSessionsViewModel = hiltViewModel()
) {
    val classDetails = viewModel.classDetails.collectAsStateWithLifecycle().value
    val allClassSessions = viewModel.allClassSessions.collectAsStateWithLifecycle().value
    val cancelledClassSessions = viewModel.cancelledClassSessions.collectAsStateWithLifecycle().value

    var showCreateSession by remember { mutableStateOf(false) }
    if (showCreateSession) {
        CreateClassSessionDialog(
            onDismiss = {showCreateSession = false},
            onCreate = {formData ->
                viewModel.addClassSession(formData)
                showCreateSession = false
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.success.collect {
            showCreateSession = false
            viewModel.refresh()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            color = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                )
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
            Text("Upcoming")
            if (allClassSessions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("No class sessions for this class")
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(allClassSessions) { classSession ->
                            Card(
                                modifier = Modifier.width(220.dp)
                                    .clickable {
                                        navController.navigate(Screen.ManageBookings(classSession.id))
                                    },
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                                ),
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        convertMillisToDate(classSession.date)
                                    )
                                    Text(
                                        "${convertMinutesToTimeString(classSession.startTime.toLong())} - ${convertMinutesToTimeString(classSession.endTime.toLong())}"
                                    )
                                    Text(
                                        classSession.capacity.toString()
                                    )
                                    Button(
                                        onClick = { viewModel.cancelClassSession(classSession.id) }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            CancelledClassSessions(
                classSessions = cancelledClassSessions
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                onClick = { showCreateSession = true }
            ) {
                Text("Create New Session")
            }
        }
    }
}

@Composable
fun CancelledClassSessions(
    classSessions: List<ClassSession>
) {
    Text("Cancelled")
    if (classSessions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nothing to display")
        }
    } else {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(classSessions) { classSession ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {},
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                        ),
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                convertMillisToDate(classSession.date)
                            )
                            Text(
                                "${convertMinutesToTimeString(classSession.startTime.toLong())} - ${convertMinutesToTimeString(classSession.endTime.toLong())}"

                            )
                            Text(
                                classSession.capacity.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}