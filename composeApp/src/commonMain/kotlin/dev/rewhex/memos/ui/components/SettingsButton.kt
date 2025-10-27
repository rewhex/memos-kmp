package dev.rewhex.memos.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.rewhex.memos.ui.Size
import dev.rewhex.memos.ui.modifier.mouseHoverIcon
import memos.composeapp.generated.resources.Res
import memos.composeapp.generated.resources.icon_settings
import memos.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsButton(onPress: () -> Unit) {
  IconButton(
    modifier = Modifier.mouseHoverIcon(),
    onClick = onPress,
  ) {
    Icon(
      modifier = Modifier.size(Size.S_22),
      painter = painterResource(Res.drawable.icon_settings),
      contentDescription = stringResource(Res.string.settings),
    )
  }
}
