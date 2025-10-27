package dev.rewhex.memos

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import dev.rewhex.memos.constants.DESKTOP_WINDOW_MIN_HEIGHT_INT
import dev.rewhex.memos.constants.DESKTOP_WINDOW_MIN_WIDTH_INT
import dev.rewhex.memos.constants.IS_MAC_OS
import dev.rewhex.memos.di.initKoin
import dev.rewhex.memos.navigation.DefaultRootComponent
import dev.rewhex.memos.ui.app.AppContent
import dev.rewhex.memos.ui.app.MacOsStatusBar
import dev.rewhex.memos.ui.app.MenuBarView
import dev.rewhex.memos.utils.EventUtils
import dev.rewhex.memos.utils.runOnUiThread
import java.awt.Color
import java.awt.Dimension
import java.nio.file.Files
import java.nio.file.Paths
import memos.composeapp.generated.resources.Res
import memos.composeapp.generated.resources.app_name
import memos.composeapp.generated.resources.icon_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() {
  Files.createDirectories(Paths.get(STORAGE_DIR_PATH))

  System.setProperty("jna.nosys", "true")

  if (IS_MAC_OS) {
    System.setProperty("apple.laf.useScreenMenuBar", "true")
    System.setProperty("apple.awt.application.appearance", "system")
    System.setProperty("apple.awt.application.name", "Memos")
  } else {
    System.setProperty("skiko.renderApi", "OPENGL")
    System.setProperty("skiko.vsync.enabled", "false")
  }

  val lifecycleRegistry = LifecycleRegistry()
  val backDispatcher = BackDispatcher()

  val component = runOnUiThread {
    DefaultRootComponent(
      componentContext = DefaultComponentContext(
        lifecycle = lifecycleRegistry,
        backHandler = backDispatcher,
      ),
    )
  }

  initKoin()

  val savedWindowState = getSavedWindowState()

  return application {
    var isVisible by remember { mutableStateOf(true) }

    val windowState = rememberWindowState(
      placement = savedWindowState.placement,
      position = savedWindowState.position,
      size = savedWindowState.size,
    )

    LifecycleController(lifecycleRegistry, windowState)

    Window(
      visible = isVisible,
      state = windowState,
      title = stringResource(Res.string.app_name),
      icon = painterResource(Res.drawable.icon_logo),
      onCloseRequest = { isVisible = false },
    ) {
      WindowStateWatcher(window)

      LaunchedEffect(Unit) {
        window.apply {
          // Fix for white background flickering
          background = Color(25, 25, 25)
          minimumSize = Dimension(DESKTOP_WINDOW_MIN_WIDTH_INT, DESKTOP_WINDOW_MIN_HEIGHT_INT)

          if (IS_MAC_OS) {
            rootPane.putClientProperty("apple.awt.fullWindowContent", true)
            rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
            rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
            rootPane.putClientProperty("apple.awt.fullscreenable", true)
          }

          EventUtils.addListeners(
            component = component,
            onAppReopened = {
              isVisible = true
            },
          )
        }
      }

      if (IS_MAC_OS) {
        MenuBarView()
        MacOsStatusBar()
      }

      AppContent(component)
    }
  }
}
