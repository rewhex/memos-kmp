package dev.rewhex.memos.ui.app

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.rewhex.memos.di.service.NetworkStateService
import dev.rewhex.memos.di.service.SettingsService
import dev.rewhex.memos.di.service.SettingsState
import dev.rewhex.memos.dialogs.AboutDialog
import dev.rewhex.memos.dialogs.EditUrlDialog
import dev.rewhex.memos.navigation.RootComponent
import dev.rewhex.memos.screens.home.HomeScreen
import dev.rewhex.memos.screens.initial.InitialScreen
import dev.rewhex.memos.ui.components.NetworkStateOverlay
import org.koin.compose.koinInject

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun AppContent(component: RootComponent) {
  val stack by component.childStack.subscribeAsState()
  val dialogSlot by component.dialog.subscribeAsState()

  val networkStateService = koinInject<NetworkStateService>()

  val settingsService = koinInject<SettingsService>()
  val settingsState by settingsService.state.collectAsState()

  val memosUrl = when (val state = settingsState) {
    is SettingsState.Loaded -> state.settings.memosUrl
    else -> null
  }

  DisposableEffect(memosUrl) {
    networkStateService.init()

    onDispose {
      networkStateService.dispose()
    }
  }

  AppTheme {
    KCEFWrapper {
      Box(Modifier.fillMaxSize()) {
        ChildStack(
          stack = stack,
          animation = stackAnimation(
            animator = fade(animationSpec = tween(durationMillis = 150)),
            predictiveBackParams = {
              PredictiveBackParams(
                backHandler = component.backHandler,
                onBack = component::onBackClicked,
              )
            },
          ),
        ) {
          when (it.instance) {
            is RootComponent.Child.InitialScreen -> InitialScreen(component)
            is RootComponent.Child.HomeScreen -> HomeScreen(component)
          }
        }

        when (dialogSlot.child?.instance) {
          is RootComponent.DialogChild.AboutDialog -> AboutDialog(component)
          is RootComponent.DialogChild.EditUrlDialog -> EditUrlDialog(component)
          else -> {}
        }

        Box(
          Modifier
            .align(Alignment.BottomStart)
            .zIndex(1f),
        ) {
          NetworkStateOverlay()
        }
      }
    }
  }
}
