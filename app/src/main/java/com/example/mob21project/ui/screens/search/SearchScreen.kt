package com.example.mob21project.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mob21project.R
import com.example.mob21project.data.model.SearchResultUiModel
import com.example.mob21project.ui.navigation.Screen

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query = viewModel.query.collectAsStateWithLifecycle().value
    val results = viewModel.results.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query -> viewModel.onQueryChange(query) },
                placeholder = {Text("Search classes or facilities")},
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
            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text(
                    "Cancel"
                )
            }
        }
        if (results.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No results found"
                )
            }
        } else {
            LazyColumn {
                items(results) { result ->
                    when (result) {
                        is SearchResultUiModel.Class -> {
                            SearchResultCard(
                                title = result.title,
                                description = result.description,
                                imageUrl = result.imageUrl,
                                onClick = {
                                    navController.navigate(
                                        Screen.ClassSessions(result.classId)
                                    )
                                }
                            )
                        }
                        is SearchResultUiModel.Facility -> {
                            SearchResultCard(
                                title = result.title,
                                description = result.description,
                                imageUrl = result.imageUrl,
                                onClick = {
                                    navController.navigate(
                                        Screen.FacilityAvailability(result.facilityId)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = imageUrl ?: "",
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
                Text(title)
                Text(description)
            }
        }
    }
}