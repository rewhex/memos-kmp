package dev.rewhex.memos.di.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class NetworkStateService actual constructor(
  private val coroutineScope: CoroutineScope,
  private val httpClientService: HttpClientService,
  private val settingsService: SettingsService,
) : KoinComponent {
  private val context by inject<Context>()

  private val _uiState = MutableStateFlow(NetworkUiState())
  actual val uiState = _uiState.asStateFlow()

  private val scope = CoroutineScope(Dispatchers.IO) + SupervisorJob()

  private val connectivityManager = context
    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  private val networkCallback = object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
      scope.launch {
        withContext(Dispatchers.Main) {
          val isConnected = getIsConnected(network)

          _uiState.value = _uiState.value.copy(isConnected = isConnected)

          if (isConnected) {
            checkServerAccessibility()
          } else {
            _uiState.value = _uiState.value.copy(isServerAccessible = false)
          }
        }
      }

      super.onAvailable(network)
    }

    override fun onLost(network: Network) {
      scope.launch {
        withContext(Dispatchers.Main) {
          _uiState.value = _uiState.value.copy(
            isConnected = false,
            isServerAccessible = false,
          )
        }
      }

      super.onLost(network)
    }
  }

  private fun getIsConnected(network: Network?): Boolean {
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

    if (network == null || networkCapabilities == null) {
      return false
    }

    var isConnected = false

    if (
      (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED))
      || networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)
    ) {
      isConnected = true
    }

    return isConnected
  }

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
    connectivityManager.registerDefaultNetworkCallback(networkCallback)

    scope.launch {
      val activeNetwork = connectivityManager.activeNetwork

      if (activeNetwork != null && getIsConnected(activeNetwork)) {
        checkServerAccessibility()
      }
    }
  }

  actual fun dispose() {
    connectivityManager.unregisterNetworkCallback(networkCallback)
  }
}
