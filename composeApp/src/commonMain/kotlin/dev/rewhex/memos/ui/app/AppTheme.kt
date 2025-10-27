package dev.rewhex.memos.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.rewhex.memos.ui.appTypography
import dev.rewhex.memos.utils.getColorScheme

@Composable
expect fun ApplyStatusBarsAppearanceEffect(isDarkTheme: Boolean)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
  val isDarkTheme = isSystemInDarkTheme()

  ApplyStatusBarsAppearanceEffect(isDarkTheme)

  MaterialTheme(
    colorScheme = getColorScheme(isDarkTheme),
    typography = appTypography(),
    content = {
      val containerColor = MaterialTheme.colorScheme.surface

      CompositionLocalProvider(
        LocalContentColor provides contentColorFor(containerColor),
      ) {
        Box(
          Modifier
            .fillMaxSize()
            .background(containerColor),
        ) {
          content()
        }
      }
    },
  )
}
