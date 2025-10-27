package dev.rewhex.memos.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import dev.rewhex.memos.ui.Size
import dev.rewhex.memos.ui.modifier.mouseHoverIcon
import memos.composeapp.generated.resources.Res
import memos.composeapp.generated.resources.icon_sync
import memos.composeapp.generated.resources.refresh
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val REFRESH_INITIAL_ROTATION = 160f
private const val REFRESH_TARGET_ROTATION = REFRESH_INITIAL_ROTATION - 360f

private const val REFRESH_TRANSITION_DURATION = 800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshButton(
  isLoading: Boolean,
  onPress: () -> Unit,
) {
  IconButton(
    modifier = if (isLoading) Modifier else Modifier.mouseHoverIcon(),
    enabled = !isLoading,
    onClick = onPress,
  ) {
    val transition = rememberInfiniteTransition(label = "RefreshButton")

    val animatedAlpha by transition.animateFloat(
      initialValue = if (isLoading) 0.3f else 1f,
      targetValue = 1f,
      animationSpec = infiniteRepeatable(
        tween(
          durationMillis = REFRESH_TRANSITION_DURATION,
          easing = LinearEasing,
        ),
        RepeatMode.Reverse,
      ),
      label = "RefreshButton",
    )

    val animatedRotation by transition.animateFloat(
      initialValue = REFRESH_INITIAL_ROTATION,
      targetValue = if (isLoading) REFRESH_TARGET_ROTATION else REFRESH_INITIAL_ROTATION,
      animationSpec = infiniteRepeatable(
        tween(
          durationMillis = REFRESH_TRANSITION_DURATION,
          easing = LinearEasing,
        ),
        RepeatMode.Restart,
      ),
      label = "RefreshButton",
    )

    Icon(
      modifier = Modifier
        .size(Size.S_20)
        .alpha(alpha = animatedAlpha)
        .rotate(degrees = animatedRotation),
      painter = painterResource(Res.drawable.icon_sync),
      contentDescription = stringResource(Res.string.refresh),
      tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
    )
  }
}
