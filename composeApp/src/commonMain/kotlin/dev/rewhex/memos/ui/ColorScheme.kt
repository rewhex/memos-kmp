package dev.rewhex.memos.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import dev.rewhex.memos.utils.getSurfaceColorFn

private val md_theme_light_primary = Color(0xFF0061a6)
private val md_theme_light_onPrimary = Color(0xFFffffff)
private val md_theme_light_primaryContainer = Color(0xFFd0e4ff)
private val md_theme_light_onPrimaryContainer = Color(0xFF001d36)
private val md_theme_light_secondary = Color(0xFF535f70)
private val md_theme_light_onSecondary = Color(0xFFffffff)
private val md_theme_light_secondaryContainer = Color(0xFFd6e3f7)
private val md_theme_light_onSecondaryContainer = Color(0xFF101c2b)
private val md_theme_light_tertiary = Color(0xFF6b5778)
private val md_theme_light_onTertiary = Color(0xFFffffff)
private val md_theme_light_tertiaryContainer = Color(0xFFf3daff)
private val md_theme_light_onTertiaryContainer = Color(0xFF251432)
private val md_theme_light_error = Color(0xFFba1b1b)
private val md_theme_light_errorContainer = Color(0xFFffdad4)
private val md_theme_light_onError = Color(0xFFffffff)
private val md_theme_light_onErrorContainer = Color(0xFF410001)
private val md_theme_light_background = Color(0xFFfdfcff)
private val md_theme_light_onBackground = Color(0xFF1b1b1b)
private val md_theme_light_surface = Color(0xFFfdfcff)
private val md_theme_light_onSurface = Color(0xFF1b1b1b)
private val md_theme_light_surfaceVariant = Color(0xFFdfe2eb)
private val md_theme_light_onSurfaceVariant = Color(0xFF42474e)
private val md_theme_light_outline = Color(0xFF73777f)
private val md_theme_light_inverseOnSurface = Color(0xFFf1f0f4)
private val md_theme_light_inverseSurface = Color(0xFF2f3033)
private val md_theme_light_inversePrimary = Color(0xFF9ccaff)

private val md_theme_dark_primary = Color(0xFF9ccaff)
private val md_theme_dark_onPrimary = Color(0xFF00325a)
private val md_theme_dark_primaryContainer = Color(0xFF00497f)
private val md_theme_dark_onPrimaryContainer = Color(0xFFd0e4ff)
private val md_theme_dark_secondary = Color(0xFFbbc8db)
private val md_theme_dark_onSecondary = Color(0xFF253140)
private val md_theme_dark_secondaryContainer = Color(0xFF3c4858)
private val md_theme_dark_onSecondaryContainer = Color(0xFFd6e3f7)
private val md_theme_dark_tertiary = Color(0xFFd6bee4)
private val md_theme_dark_onTertiary = Color(0xFF3b2948)
private val md_theme_dark_tertiaryContainer = Color(0xFF523f5f)
private val md_theme_dark_onTertiaryContainer = Color(0xFFf3daff)
private val md_theme_dark_error = Color(0xFFffb4a9)
private val md_theme_dark_errorContainer = Color(0xFF930006)
private val md_theme_dark_onError = Color(0xFF680003)
private val md_theme_dark_onErrorContainer = Color(0xFFffdad4)
private val md_theme_dark_background = Color(0xFF1b1b1b)
private val md_theme_dark_onBackground = Color(0xFFe2e2e6)
private val md_theme_dark_surface = Color(0xFF1b1b1b)
private val md_theme_dark_onSurface = Color(0xFFe2e2e6)
private val md_theme_dark_surfaceVariant = Color(0xFF42474e)
private val md_theme_dark_onSurfaceVariant = Color(0xFFc3c7d0)
private val md_theme_dark_outline = Color(0xFF8d9199)
private val md_theme_dark_inverseOnSurface = Color(0xFF1b1b1b)
private val md_theme_dark_inverseSurface = Color(0xFFe2e2e6)
private val md_theme_dark_inversePrimary = Color(0xFF0061a6)

private val getLightSurfaceColor = getSurfaceColorFn(
  isDarkTheme = false,
  surface = md_theme_light_surface,
  primary = md_theme_light_primary,
)

private val getDarkSurfaceColor = getSurfaceColorFn(
  isDarkTheme = true,
  surface = md_theme_dark_surface,
  primary = md_theme_dark_primary,
)

val LightColorScheme = lightColorScheme(
  primary = md_theme_light_primary,
  onPrimary = md_theme_light_onPrimary,
  primaryContainer = md_theme_light_primaryContainer,
  onPrimaryContainer = md_theme_light_onPrimaryContainer,
  secondary = md_theme_light_secondary,
  onSecondary = md_theme_light_onSecondary,
  secondaryContainer = md_theme_light_secondaryContainer,
  onSecondaryContainer = md_theme_light_onSecondaryContainer,
  tertiary = md_theme_light_tertiary,
  onTertiary = md_theme_light_onTertiary,
  tertiaryContainer = md_theme_light_tertiaryContainer,
  onTertiaryContainer = md_theme_light_onTertiaryContainer,
  error = md_theme_light_error,
  errorContainer = md_theme_light_errorContainer,
  onError = md_theme_light_onError,
  onErrorContainer = md_theme_light_onErrorContainer,
  background = md_theme_light_background,
  onBackground = md_theme_light_onBackground,
  surface = md_theme_light_surface,
  onSurface = md_theme_light_onSurface,
  surfaceVariant = md_theme_light_surfaceVariant,
  onSurfaceVariant = md_theme_light_onSurfaceVariant,
  outline = md_theme_light_outline,
  outlineVariant = md_theme_light_surfaceVariant,
  inverseOnSurface = md_theme_light_inverseOnSurface,
  inverseSurface = md_theme_light_inverseSurface,
  inversePrimary = md_theme_light_inversePrimary,
  surfaceBright = getLightSurfaceColor("surfaceBright"),
  surfaceContainer = getLightSurfaceColor("surfaceContainer"),
  surfaceContainerHigh = getLightSurfaceColor("surfaceContainerHigh"),
  surfaceContainerHighest = getLightSurfaceColor("surfaceContainerHighest"),
  surfaceContainerLow = getLightSurfaceColor("surfaceContainerLow"),
  surfaceContainerLowest = getLightSurfaceColor("surfaceContainerLowest"),
  surfaceDim = getLightSurfaceColor("surfaceDim"),
)

val DarkColorScheme = darkColorScheme(
  primary = md_theme_dark_primary,
  onPrimary = md_theme_dark_onPrimary,
  primaryContainer = md_theme_dark_primaryContainer,
  onPrimaryContainer = md_theme_dark_onPrimaryContainer,
  secondary = md_theme_dark_secondary,
  onSecondary = md_theme_dark_onSecondary,
  secondaryContainer = md_theme_dark_secondaryContainer,
  onSecondaryContainer = md_theme_dark_onSecondaryContainer,
  tertiary = md_theme_dark_tertiary,
  onTertiary = md_theme_dark_onTertiary,
  tertiaryContainer = md_theme_dark_tertiaryContainer,
  onTertiaryContainer = md_theme_dark_onTertiaryContainer,
  error = md_theme_dark_error,
  errorContainer = md_theme_dark_errorContainer,
  onError = md_theme_dark_onError,
  onErrorContainer = md_theme_dark_onErrorContainer,
  background = md_theme_dark_background,
  onBackground = md_theme_dark_onBackground,
  surface = md_theme_dark_surface,
  onSurface = md_theme_dark_onSurface,
  surfaceVariant = md_theme_dark_surfaceVariant,
  onSurfaceVariant = md_theme_dark_onSurfaceVariant,
  outline = md_theme_dark_outline,
  outlineVariant = md_theme_dark_surfaceVariant,
  inverseOnSurface = md_theme_dark_inverseOnSurface,
  inverseSurface = md_theme_dark_inverseSurface,
  inversePrimary = md_theme_dark_inversePrimary,
  surfaceBright = getDarkSurfaceColor("surfaceBright"),
  surfaceContainer = getDarkSurfaceColor("surfaceContainer"),
  surfaceContainerHigh = getDarkSurfaceColor("surfaceContainerHigh"),
  surfaceContainerHighest = getDarkSurfaceColor("surfaceContainerHighest"),
  surfaceContainerLow = getDarkSurfaceColor("surfaceContainerLow"),
  surfaceContainerLowest = getDarkSurfaceColor("surfaceContainerLowest"),
  surfaceDim = getDarkSurfaceColor("surfaceDim"),
)
