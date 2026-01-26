package com.example.mob21project.ui.screens.admin.manage.manageActivity

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
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mob21project.data.model.ClassDetails
import com.example.mob21project.data.model.FacilityDetails
import com.example.mob21project.ui.navigation.Screen
import com.example.mob21project.ui.utils.convertMinutesToTimeString

@Composable
fun ManageActivityScreen(
    navController: NavController,
    viewModel: ManageActivityViewModel = hiltViewModel()
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val allClassDetails = viewModel.allClassDetails.collectAsStateWithLifecycle().value
    val allFacilityDetails = viewModel.allFacilityDetails.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.getAllClassDetails()
        viewModel.getAllFacilityDetails()
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
                    text = { Text("Class") },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Facility") },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                )
            }
        )
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
        when(selectedTabIndex) {
            0 -> {
                ClassList(
                    allClassDetails = allClassDetails,
                    onClick = { classId ->
                        navController.navigate(Screen.ManageClassSessions(classId))
                    })
            }
            1 -> {
                FacilityList(
                    allFacilityDetails = allFacilityDetails,
                    onClick = { facilityId ->
                        navController.navigate(Screen.FacilityAvailability(facilityId))
                    })
            }
        }
    }
}

@Composable
fun ClassList(
    allClassDetails: List<ClassDetails>,
    onClick: (String) -> Unit
) {
    if (allClassDetails.isEmpty()) {
        EmptyListView("There are no classes to display")
    } else {
        LazyColumn(
        ) {
            items(allClassDetails) { classDetails ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                        .clickable { onClick(classDetails.id) }
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
@Composable
fun FacilityList(
    allFacilityDetails: List<FacilityDetails>,
    onClick: (String) -> Unit
) {
    if (allFacilityDetails.isEmpty()) {
        EmptyListView("There are no facilities to display")
    } else {
        LazyColumn(
        ) {
            items(allFacilityDetails) { facilityDetails ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                        .clickable { onClick(facilityDetails.id) }
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
                            Text(facilityDetails.title)
                            Text(facilityDetails.description)
                            Text(
                                "${convertMinutesToTimeString(facilityDetails.openingTime)} - ${convertMinutesToTimeString(facilityDetails.closingTime)}"
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun EmptyListView(
    displayText: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = Icons.Default.Pending,
            "",
            modifier = Modifier
                .size(80.dp)
        )
        Text(
            displayText
        )
    }
}