package com.example.mob21project.ui.screens.facilityDetails

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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mob21project.R
import com.example.mob21project.ui.navigation.Screen
import com.example.mob21project.ui.utils.convertMinutesToTimeString

@Composable
fun FacilityDetailsScreen(
    navController: NavController,
    viewModel: FacilityDetailsViewModel = hiltViewModel()
) {
    val allFacilityDetails = viewModel.allFacilityDetails.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        viewModel.getAllFacilityDetails()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(allFacilityDetails) { facilityDetails ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                        .clickable {
                            navController.navigate(Screen.FacilityAvailability(facilityDetails.id))
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = facilityDetails.imageUrl,
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
                            Text(facilityDetails.title)
                            Text(
                                facilityDetails.description,
                                maxLines = 3,
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                "${convertMinutesToTimeString(facilityDetails.openingTime)} - ${convertMinutesToTimeString(facilityDetails.closingTime)}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}