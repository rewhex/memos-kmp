package dev.rewhex.memos.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.rewhex.memos.ui.DarkColorScheme
import dev.rewhex.memos.ui.LightColorScheme

fun getDefaultColorScheme(isDarkTheme: Boolean): ColorScheme {
  return if (isDarkTheme) DarkColorScheme else LightColorScheme
}

@Composable
expect fun getColorScheme(isDarkTheme: Boolean): ColorScheme

private fun getSurfaceAlphaByType(
  isDarkTheme: Boolean,
  type: String,
): Float {
  return when (type) {
    "surfaceDim" -> if (isDarkTheme) 0.06f else 0.13f
    "surfaceBright" -> if (isDarkTheme) 0.24f else 0.02f
    "surfaceContainer" -> if (isDarkTheme) 0.12f else 0.06f
    "surfaceContainerHigh" -> if (isDarkTheme) 0.17f else 0.08f
    "surfaceContainerHighest" -> if (isDarkTheme) 0.22f else 0.1f
    "surfaceContainerLow" -> if (isDarkTheme) 0.10f else 0.04f
    "surfaceContainerLowest" -> if (isDarkTheme) 0.04f else 0.0f
    else -> 1.0f
  }
}

fun Color.mix(
  color: Color,
  weight: Float = 0.5f,
): Color {
  val w = 2 * weight - 1
  val a = color.alpha - alpha

  val w1 = ((if (w * a == -1f) w else (w + a) / (1 + w * a)) + 1) / 2
  val w2 = 1 - w1

  return copy(
    red = w1 * color.red + w2 * red,
    green = w1 * color.green + w2 * green,
    blue = w1 * color.blue + w2 * blue,
    alpha = color.alpha * weight + alpha * (1 - weight),
  )
}

fun getSurfaceColorFn(
  isDarkTheme: Boolean,
  surface: Color,
  primary: Color,
): (type: String) -> Color {
  return {
    surface
      .mix(primary.copy(alpha = getSurfaceAlphaByType(isDarkTheme, it)))
      .copy(alpha = 1f)
  }
}
