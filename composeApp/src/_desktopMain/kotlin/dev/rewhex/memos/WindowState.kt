package dev.rewhex.memos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import dev.rewhex.memos.constants.DESKTOP_WINDOW_MIN_HEIGHT_INT
import dev.rewhex.memos.constants.DESKTOP_WINDOW_MIN_WIDTH_INT
import dev.rewhex.memos.utils.debounce
import java.awt.Point
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.io.File
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class SavedState(
  val placement: String,
  val x: Int,
  val y: Int,
  val width: Int,
  val height: Int,
)

private const val FILE_NAME = "windowState.json"
private val FILE = File(STORAGE_DIR_PATH, FILE_NAME)

private fun getInitialState(): WindowState {
  return WindowState(
    placement = WindowPlacement.Maximized,
    position = WindowPosition.Aligned(Alignment.Center),
    size = DpSize(DESKTOP_WINDOW_MIN_WIDTH_INT.dp, DESKTOP_WINDOW_MIN_HEIGHT_INT.dp),
  )
}

fun getSavedWindowState(): WindowState {
  try {
    val savedState = if (FILE.exists()) Json.decodeFromString<SavedState>(FILE.readText()) else null

    return if (savedState == null) {
      getInitialState()
    } else {
      WindowState(
        placement = WindowPlacement.valueOf(savedState.placement),
        position = WindowPosition.Absolute(savedState.x.dp, savedState.y.dp),
        size = DpSize(savedState.width.dp, savedState.height.dp),
      )
    }
  } catch (e: Exception) {
    e.printStackTrace()

    return getInitialState()
  }
}

private fun saveState(
  placement: WindowPlacement,
  position: Point,
  width: Int,
  height: Int,
) {
  try {
    FILE.writeText(
      Json.encodeToString(
        SavedState(
          placement = placement.name,
          x = position.x,
          y = position.y,
          width = width,
          height = height,
        ),
      ),
    )
  } catch (e: Exception) {
    e.printStackTrace()
  }
}

@Composable
fun WindowStateWatcher(window: ComposeWindow) {
  val coroutineScope = rememberCoroutineScope()

  var placement by remember { mutableStateOf(window.placement) }
  var position by remember { mutableStateOf(Point(0, 0)) }
  var height by remember { mutableStateOf(window.size.height) }
  var width by remember { mutableStateOf(window.size.width) }

  val handleChanged = debounce(700L, coroutineScope) {
    saveState(placement, position, width, height)
  }

  LaunchedEffect(Unit) {
    window.addComponentListener(
      object : ComponentListener {
        override fun componentResized(e: ComponentEvent) {
          height = e.component.height
          width = e.component.width

          handleChanged()
        }

        override fun componentMoved(e: ComponentEvent) {
          position = Point(e.component.x, e.component.y)

          handleChanged()
        }

        override fun componentShown(e: ComponentEvent) {}

        override fun componentHidden(e: ComponentEvent) {}
      },
    )

    coroutineScope.launch {
      while (isActive) {
        delay(1000)

        var isChanged = false

        if (placement != window.placement) {
          placement = window.placement
          isChanged = true
        }

        if (isChanged) {
          handleChanged()
        }
      }
    }
  }
}
