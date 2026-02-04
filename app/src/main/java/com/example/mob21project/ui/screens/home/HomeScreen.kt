package com.example.mob21project.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mob21project.R
import com.example.mob21project.ui.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val classes = viewModel.availableClasses.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                    )
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_pulse),
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth()
                                .height(300.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
            item {
                Text(
                    text = "Available classes",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(16.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(classes) { classItem ->
                        Card(
                            modifier = Modifier.width(180.dp)
                                .aspectRatio(4f / 3f)
                                .clickable { navController.navigate(Screen.ClassSessions(classItem.id)) }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AsyncImage(
                                    model = classItem.imageUrl,
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(R.drawable.ic_imagesmode),
                                    error = painterResource(R.drawable.ic_imagesmode)
                                )
                                // overlay
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.6f)
                                                )
                                            )
                                        )
                                )
                                Text(
                                    modifier = Modifier.padding(16.dp)
                                        .align(Alignment.BottomStart),
                                    text = classItem.title,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
//                            Column(
//                                modifier = Modifier.fillMaxWidth().padding(16.dp),
//                                verticalArrangement = Arrangement.spacedBy(16.dp)
//                            ) {
//                                Text(
//                                    classItem.title
//                                )
////                                Text(
////                                    classItem.description
////                                )
//                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Explore our offerings",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ExploreCard(
                        modifier = Modifier.weight(1f),
                        title = "Explore our classes",
                        imageUrl = "https://marketplace.canva.com/7Yh6o/MAGYYt7Yh6o/1/s2/canva-close-up-shot-of-a-woman-using-a-reformer-pilates-machine-MAGYYt7Yh6o.jpg",
                        onClick = { navController.navigate(Screen.ClassDetails) }
                    )
                    ExploreCard(
                        modifier = Modifier.weight(1f),
                        title = "Explore our facilities",
                        imageUrl = "https://cdn.pixabay.com/photo/2022/01/15/20/02/pickleball-6940609_1280.jpg",
                        onClick = { navController.navigate(Screen.FacilityDetails) }
                    )
                }
            }
        }
    }
}
@Composable
fun ExploreCard(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(0.75f)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_imagesmode),
                error = painterResource(R.drawable.ic_imagesmode)
            )
            // overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
            )
            Text(
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.BottomStart),
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}