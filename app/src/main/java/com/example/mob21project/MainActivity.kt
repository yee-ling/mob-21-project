package com.example.mob21project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mob21project.ui.navigation.AppNav
import com.example.mob21project.ui.navigation.Screen
import com.example.mob21project.ui.theme.MOBStarterAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MOBStarterAppTheme {
                ComposeApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeApp() {
    val viewModel: AppViewModel = hiltViewModel()
    val authUser = viewModel.authUser.collectAsStateWithLifecycle().value
    val isAdmin = viewModel.isAdmin.collectAsStateWithLifecycle().value

    val navController = rememberNavController()
    var topBarTitle by remember { mutableStateOf("") }

    LaunchedEffect(authUser) {
        if (authUser == null) {
            navController.navigate(Screen.Login) {
                popUpTo(0) {inclusive=true}
            }
        } else {
            viewModel.checkIsAdmin()
        }
    }

    // drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(
        DrawerItem("Home", Screen.Home),
        DrawerItem("Bookings", Screen.Bookings),
    )
    val noTopBarScreens = listOf(
        Screen.Splash::class,
        Screen.Login::class,
        Screen.Register::class
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showTopBar = currentDestination?.hierarchy?.any { destination ->
        items.any { tab -> destination.hasRoute(tab.screen::class) }
    } == true &&
            currentDestination.hierarchy.none { destination ->
                noTopBarScreens.any { destination.hasRoute(it) }
            }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showTopBar,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Column {
                    NavigationDrawerItem(
                        icon = {
                            IconButton(
                                onClick = {scope.launch { drawerState.close() }},
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = Color.Black
                                )
                            ) { Icon(
                                Icons.Default.Close,
                                ""
                            )}
                             },
                        onClick = {scope.launch { drawerState.close() }},
                        selected = false,
                        label = { },
                    )
                    items.forEach {
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    it.title,
                                    fontSize = 24.sp
                                )
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(it.screen)
                            }
                        )
                    }
                    if (isAdmin) {
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    "Admin Dashboard",
                                    fontSize = 24.sp,
                                )
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(
                                    Screen.AdminDashboard
                                )
                            }
                        )
                    }
                    NavigationDrawerItem(
                        label = {
                            Text(
                                "Sign Out",
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            viewModel.signOut()
                        }
                    )
                }
            }
        }

    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (showTopBar) {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        title = {
                            Text(
                                text = topBarTitle,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {scope.launch { drawerState.open() }}
                            ) {
                                Icon(
                                    Icons.Default.Menu,
                                    "",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { navController.navigate(Screen.Search) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                AppNav(
                    navController = navController,
                    onTitleChange = { topBarTitle = it }
                )
            }
        }
    }
}
data class DrawerItem(
    val title: String,
    val screen: Screen
)