package dev.rewhex.memos.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.rewhex.memos.di.service.NetworkStateService
import dev.rewhex.memos.di.service.NetworkUiState
import dev.rewhex.memos.ui.Size
import dev.rewhex.memos.ui.Spacings
import kotlinx.coroutines.delay
import memos.composeapp.generated.resources.Res
import memos.composeapp.generated.resources.back_online
import memos.composeapp.generated.resources.icon_check
import memos.composeapp.generated.resources.icon_sync_problem
import memos.composeapp.generated.resources.no_connection
import memos.composeapp.generated.resources.server_unreachable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

val NETWORK_STATE_OVERLAY_SIZE = Size.S_36

@Composable
fun NetworkStateOverlay() {
  val networkStateService = koinInject<NetworkStateService>()

  val uiState by networkStateService.uiState.collectAsState()
  val isVisible = getIsNetworkStateOverlayVisible(uiState)

  val background by animateColorAsState(
    when {
      !uiState.isConnected || !uiState.isServerAccessible -> Color(0xFFBC0C1A)
      else -> Color(0xFF168759) // Green for back online
    },
    label = "NetworkStateOverlayBackground",
  )

  AnimatedVisibility(
    visible = isVisible,
    enter = expandVertically(),
    exit = shrinkVertically(),
  ) {
    Column(
      Modifier
        .fillMaxWidth()
        .background(background),
    ) {
      Row(
        modifier = Modifier
          .requiredHeight(NETWORK_STATE_OVERLAY_SIZE)
          .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
      ) {
        Icon(
          modifier = Modifier
            .padding(end = Spacings.S_8)
            .size(Size.S_14),
          painter = painterResource(
            when (uiState.isConnected && uiState.isServerAccessible) {
              true -> Res.drawable.icon_check
              else -> Res.drawable.icon_sync_problem
            },
          ),
          contentDescription = null,
          tint = Color.White,
        )

        Text(
          text = stringResource(
            when {
              !uiState.isConnected -> Res.string.no_connection
              !uiState.isServerAccessible -> Res.string.server_unreachable
              else -> Res.string.back_online
            },
          ),
          style = MaterialTheme.typography.bodySmall,
          color = Color.White,
        )
      }

      Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
    }
  }
}

@Composable
fun getIsNetworkStateOverlayVisible(uiState: NetworkUiState): Boolean {
  var isVisible by remember { mutableStateOf(false) }

  LaunchedEffect(uiState) {
    isVisible = if (!uiState.isConnected || !uiState.isServerAccessible) {
      true
    } else {
      delay(2000)
      false
    }
  }

  return isVisible
}
