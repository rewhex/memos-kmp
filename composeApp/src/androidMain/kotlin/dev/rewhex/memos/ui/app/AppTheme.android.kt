package dev.rewhex.memos.ui.app

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.rewhex.memos.utils.cast

@Composable
actual fun ApplyStatusBarsAppearanceEffect(isDarkTheme: Boolean) {
  val view = LocalView.current
  val activity = view.context.cast<Activity>()

  if (!view.isInEditMode) {
    SideEffect {
      try {
        val insetsController = WindowCompat.getInsetsController(activity.window, view)

        insetsController.isAppearanceLightStatusBars = !isDarkTheme
        insetsController.isAppearanceLightNavigationBars = !isDarkTheme
      } catch (e: Exception) {
        //
      }
    }
  }
}
