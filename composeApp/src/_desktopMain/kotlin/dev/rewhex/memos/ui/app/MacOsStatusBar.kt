package dev.rewhex.memos.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.FrameWindowScope
import dev.rewhex.memos.constants.DESKTOP_TOOLBAR_HEIGHT
import dev.rewhex.memos.utils.CustomWindowDecorationAccessing

@Composable
fun FrameWindowScope.MacOsStatusBar() {
  LaunchedEffect(Unit) {
    if (CustomWindowDecorationAccessing.isSupported) {
      val height = DESKTOP_TOOLBAR_HEIGHT.value.toInt()

      CustomWindowDecorationAccessing.setCustomDecorationEnabled(window, true)
      CustomWindowDecorationAccessing.setCustomDecorationTitleBarHeight(window, height)
      CustomWindowDecorationAccessing.setCustomDecorationHitTestSpotsMethod(window, emptyMap())
    }
  }
}
