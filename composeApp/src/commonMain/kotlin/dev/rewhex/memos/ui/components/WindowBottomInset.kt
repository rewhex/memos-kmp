package dev.rewhex.memos.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.rewhex.memos.di.service.NetworkStateService
import org.koin.compose.koinInject

@Composable
fun WindowBottomInset() {
  val networkStateService = koinInject<NetworkStateService>()

  val uiState by networkStateService.uiState.collectAsState()
  val isVisible = getIsNetworkStateOverlayVisible(uiState)

  Column {
    AnimatedVisibility(
      visible = isVisible,
      enter = expandVertically(),
      exit = shrinkVertically(),
    ) {
      Spacer(Modifier.requiredHeight(NETWORK_STATE_OVERLAY_SIZE))
    }

    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
  }
}

@Composable
fun windowBottomInsetsPadding(): Dp {
  val networkStateService = koinInject<NetworkStateService>()

  val uiState by networkStateService.uiState.collectAsState()
  val isVisible = getIsNetworkStateOverlayVisible(uiState)

  val bottomPadding = WindowInsets
    .safeDrawing
    .only(WindowInsetsSides.Bottom)
    .asPaddingValues()
    .calculateBottomPadding()

  val value by animateDpAsState(
    targetValue = (if (isVisible) NETWORK_STATE_OVERLAY_SIZE else 0.dp) + bottomPadding,
  )

  return value
}
