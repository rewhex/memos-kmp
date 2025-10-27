package dev.rewhex.memos.screens.initial

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rewhex.memos.di.service.SettingsService
import dev.rewhex.memos.di.service.SettingsState
import dev.rewhex.memos.navigation.RootComponent
import dev.rewhex.memos.ui.Size
import org.koin.compose.koinInject

@Composable
fun InitialScreen(component: RootComponent) {
  val settingsService = koinInject<SettingsService>()
  val settingsState by settingsService.state.collectAsState()

  LaunchedEffect(settingsState) {
    if (settingsState is SettingsState.Loaded) {
      component.navigateToHomeScreen()
    }
  }

  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    CircularProgressIndicator(
      modifier = Modifier.size(Size.S_48),
      strokeWidth = 4.dp,
    )
  }
}
