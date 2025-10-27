package dev.rewhex.memos.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun getColorScheme(isDarkTheme: Boolean): ColorScheme {
  return getDefaultColorScheme(isDarkTheme)
}
