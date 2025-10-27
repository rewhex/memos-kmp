package dev.rewhex.memos.di.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_main_queue

actual class NetworkStateService actual constructor(
  private val coroutineScope: CoroutineScope,
  private val httpClientService: HttpClientService,
  private val settingsService: SettingsService,
) {
  private val _uiState = MutableStateFlow(NetworkUiState())
  actual val uiState = _uiState.asStateFlow()

  private val monitor = nw_path_monitor_create()

  private fun checkServerAccessibility() {
    coroutineScope.launch {
      try {
        val settingsState = settingsService.state.value

        if (settingsState is SettingsState.Loaded && settingsState.settings.memosUrl != null) {
          val isServerAccessible = httpClientService.getIsServerAccessible(settingsState.settings.memosUrl)

          withContext(Dispatchers.Main) {
            _uiState.value = _uiState.value.copy(isServerAccessible = isServerAccessible)
          }
        } else {
          withContext(Dispatchers.Main) {
            _uiState.value = _uiState.value.copy(isServerAccessible = true)
          }
        }
      } catch (_: Exception) {
        withContext(Dispatchers.Main) {
          _uiState.value = _uiState.value.copy(isServerAccessible = false)
        }
      }
    }
  }

  actual fun init() {
    nw_path_monitor_set_update_handler(monitor) { path ->
      val isConnected = nw_path_get_status(path) == nw_path_status_satisfied
      _uiState.value = _uiState.value.copy(isConnected = isConnected)

      if (isConnected) {
        checkServerAccessibility()
      } else {
        _uiState.value = _uiState.value.copy(isServerAccessible = false)
      }
    }

    nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
    nw_path_monitor_start(monitor)
  }

  actual fun dispose() {
    nw_path_monitor_cancel(monitor)
  }
}
