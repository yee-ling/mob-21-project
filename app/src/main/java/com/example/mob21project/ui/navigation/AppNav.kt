package com.example.mob21project.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mob21project.core.utils.SnackbarController
import com.example.mob21project.ui.screens.admin.AdminDashboardScreen
import com.example.mob21project.ui.screens.admin.create.createActivity.CreateActivityScreen
import com.example.mob21project.ui.screens.admin.create.createClassDetails.CreateClassDetailsScreen
import com.example.mob21project.ui.screens.admin.create.createFacilityDetails.CreateFacilityDetailsScreen
import com.example.mob21project.ui.screens.admin.manage.ManageClassSessionsScreen
import com.example.mob21project.ui.screens.admin.manage.manageActivity.ManageActivityScreen
import com.example.mob21project.ui.screens.auth.login.LoginScreen
import com.example.mob21project.ui.screens.auth.register.RegisterScreen
import com.example.mob21project.ui.screens.bookings.BookingsScreen
import com.example.mob21project.ui.screens.classDetails.ClassDetailsScreen
import com.example.mob21project.ui.screens.classSessions.ClassSessionsScreen
import com.example.mob21project.ui.screens.facilityAvailability.FacilityAvailabilityScreen
import com.example.mob21project.ui.screens.facilityDetails.FacilityDetailsScreen
import com.example.mob21project.ui.screens.home.HomeScreen
import com.example.mob21project.ui.screens.search.SearchScreen

@Composable
fun AppNav(
    navController: NavHostController,
    onTitleChange: (String) -> Unit
){
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        SnackbarController.events.collect {
            snackbarHostState.showSnackbar(
                message = it.msg,
                duration = SnackbarDuration.Short
            )
        }
    }

    Box {
        NavHost(
            navController = navController,
            startDestination = Screen.Login
        ) {
            composable<Screen.Home> {
                onTitleChange("Home")
                HomeScreen(navController)
            }
            composable<Screen.Login> {
                onTitleChange("Login")
                LoginScreen(navController)
            }
            composable<Screen.Register> {
                onTitleChange("Register")
                RegisterScreen(navController)
            }
            composable<Screen.Search> {
                onTitleChange("Search")
                SearchScreen(navController)
            }
            composable<Screen.ClassDetails> {
                onTitleChange("Class Details")
                ClassDetailsScreen(navController)
            }
            composable<Screen.FacilityDetails> {
                onTitleChange("Facility Details")
                FacilityDetailsScreen(navController)
            }
            composable<Screen.Bookings> {
                onTitleChange("Bookings")
                BookingsScreen(navController)
            }
            composable<Screen.ClassSessions> {
                onTitleChange("Class Sessions")
                ClassSessionsScreen(navController)
            }
            composable<Screen.FacilityAvailability> {
                onTitleChange("Facility Availability")
                FacilityAvailabilityScreen(navController)
            }
            composable<Screen.AdminDashboard> {
                onTitleChange("Admin Dashboard")
                AdminDashboardScreen(navController)
            }
            composable<Screen.CreateActivity> {
                onTitleChange("Create Activity")
                CreateActivityScreen(navController)
            }
            composable<Screen.CreateClassDetails> {
                onTitleChange("Create Class Details")
                CreateClassDetailsScreen(navController)
            }
            composable<Screen.CreateFacilityDetails> {
                onTitleChange("Create Facility Details")
                CreateFacilityDetailsScreen(navController)
            }
            composable<Screen.ManageActivity> {
                onTitleChange("Manage Activity")
                ManageActivityScreen(navController)
            }
            composable<Screen.ManageClassSessions> {
                onTitleChange("Class Session")
                ManageClassSessionsScreen(navController)
            }
        }
        SnackbarHost(
            snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}