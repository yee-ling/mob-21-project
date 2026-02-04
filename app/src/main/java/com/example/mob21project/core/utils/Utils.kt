package com.example.mob21project.core.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackbarEvent (
    val msg: String
)

object SnackbarController {
    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent){
        _events.send(element = event)
    }
}

object LoadingManager {
    val isLoading = mutableStateOf(false)
    fun show() {
        isLoading.value = true
    }
    fun hide() {
        isLoading.value = false
    }
}

@Composable
fun FullScreenLoader(
    bg: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color(0x88000000)
) {
    val isLoading by LoadingManager.isLoading
    if(isLoading) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(bg),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 10.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}