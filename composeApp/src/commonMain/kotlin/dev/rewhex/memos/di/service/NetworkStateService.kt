package dev.rewhex.memos.di.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

data class NetworkUiState(
  val isConnected: Boolean = true,
  val isServerAccessible: Boolean = true,
)

expect class NetworkStateService(
  coroutineScope: CoroutineScope,
  httpClientService: HttpClientService,
  settingsService: SettingsService,
) {
  val uiState: StateFlow<NetworkUiState>

  fun init()

  fun dispose()
}
