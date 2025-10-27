package dev.rewhex.memos.di.service

import java.net.InetSocketAddress
import java.net.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

actual class NetworkStateService actual constructor(
  private val coroutineScope: CoroutineScope,
  private val httpClientService: HttpClientService,
  private val settingsService: SettingsService,
) {
  private val _uiState = MutableStateFlow(NetworkUiState())
  actual val uiState = _uiState.asStateFlow()

  private var networkCheckJob: Job? = null

  companion object {
    private const val CHECK_INTERVAL_SECONDS = 30_000L
    private const val CONNECTION_TIMEOUT_MS = 5000
  }

  private fun checkNetworkConnectivity(): Boolean {
    return try {
      Socket().use { socket ->
        socket.connect(InetSocketAddress("8.8.8.8", 53), CONNECTION_TIMEOUT_MS)
        true
      }
    } catch (_: Exception) {
      false
    }
  }

  private suspend fun checkServerAccessibility() {
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

  private suspend fun performNetworkChecks() {
    val isConnected = checkNetworkConnectivity()

    withContext(Dispatchers.Main) {
      _uiState.value = _uiState.value.copy(isConnected = isConnected)

      if (isConnected) {
        coroutineScope.launch {
          checkServerAccessibility()
        }
      } else {
        _uiState.value = _uiState.value.copy(isServerAccessible = false)
      }
    }
  }

  actual fun init() {
    networkCheckJob = coroutineScope.launch {
      performNetworkChecks()

      while (isActive) {
        delay(CHECK_INTERVAL_SECONDS)
        performNetworkChecks()
      }
    }
  }

  actual fun dispose() {
    networkCheckJob?.cancel()
    networkCheckJob = null
  }
}
