package dev.rewhex.memos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackHandler
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import dev.rewhex.memos.navigation.DefaultRootComponent
import dev.rewhex.memos.ui.app.AppContent

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()

    super.onCreate(savedInstanceState)

    val component = DefaultRootComponent(
      componentContext = DefaultComponentContext(
        lifecycle = lifecycle.asEssentyLifecycle(),
        backHandler = BackHandler(onBackPressedDispatcher),
      ),
    )

    setContent {
      AppContent(component)
    }
  }
}
