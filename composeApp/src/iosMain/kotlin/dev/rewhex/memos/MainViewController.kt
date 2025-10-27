package dev.rewhex.memos

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import dev.rewhex.memos.navigation.DefaultRootComponent
import dev.rewhex.memos.ui.app.AppContent
import platform.UIKit.UIViewController

@OptIn(ExperimentalDecomposeApi::class)
fun MainViewController(): UIViewController {
  val lifecycleRegistry = LifecycleRegistry()
  val backDispatcher = BackDispatcher()

  val component = DefaultRootComponent(
    componentContext = DefaultComponentContext(
      lifecycle = lifecycleRegistry,
      backHandler = backDispatcher,
    ),
  )

  return ComposeUIViewController {
    AppContent(component)
  }
}
