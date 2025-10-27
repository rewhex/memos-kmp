package dev.rewhex.memos.utils

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun getColorScheme(isDarkTheme: Boolean): ColorScheme {
  val activity = LocalActivity.current

  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && activity != null) {
    if (isDarkTheme) {
      dynamicDarkColorScheme(activity)
    } else {
      dynamicLightColorScheme(activity)
    }
  } else {
    getDefaultColorScheme(isDarkTheme)
  }
}
