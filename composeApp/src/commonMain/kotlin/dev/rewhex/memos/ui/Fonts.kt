package dev.rewhex.memos.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import memos.composeapp.generated.resources.NotoSans_Bold
import memos.composeapp.generated.resources.NotoSans_Medium
import memos.composeapp.generated.resources.NotoSans_Regular
import memos.composeapp.generated.resources.NotoSans_SemiBold
import memos.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
private fun getFontFamily() = FontFamily(
  Font(Res.font.NotoSans_Regular, weight = FontWeight.W400),
  Font(Res.font.NotoSans_Medium, weight = FontWeight.W500),
  Font(Res.font.NotoSans_SemiBold, weight = FontWeight.W600),
  Font(Res.font.NotoSans_Bold, weight = FontWeight.W700),
)

private val defaultTypography = Typography()

@Composable
fun appTypography() = Typography().run {
  val fontFamily = getFontFamily()

  copy(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = fontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = fontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = fontFamily),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = fontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = fontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = fontFamily),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = fontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = fontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = fontFamily),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = fontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = fontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = fontFamily),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = fontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = fontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = fontFamily),
  )
}
