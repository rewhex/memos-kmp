package dev.rewhex.memos.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.rewhex.memos.di.service.SettingsService
import dev.rewhex.memos.di.service.SettingsState
import dev.rewhex.memos.navigation.RootComponent
import dev.rewhex.memos.ui.modifier.mouseHoverIcon
import dev.rewhex.memos.utils.DeviceUtils
import kotlinx.coroutines.launch
import memos.composeapp.generated.resources.Res
import memos.composeapp.generated.resources.app_desc
import memos.composeapp.generated.resources.close
import memos.composeapp.generated.resources.dot
import memos.composeapp.generated.resources.memos_credits
import memos.composeapp.generated.resources.memos_desc
import memos.composeapp.generated.resources.memos_url
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

private val appVersion = DeviceUtils.getAppVersion()

@Composable
fun AboutDialog(component: RootComponent) {
  val coroutineScope = rememberCoroutineScope()

  val settingsService = koinInject<SettingsService>()
  val settingsState by settingsService.state.collectAsState()

  val settings = remember(settingsState) {
    when (val state = settingsState) {
      is SettingsState.Loading -> null
      is SettingsState.Loaded -> state.settings
    }
  }

  val currentUrl = settings?.memosUrl ?: ""

  val memosCreditsText = stringResource(Res.string.memos_credits)
  val memosUrlText = stringResource(Res.string.memos_url)
  val dotText = stringResource(Res.string.dot)

  val handleOpenMemos = remember {
    {
      coroutineScope.launch {
        DeviceUtils.openUrl(memosUrlText)
      }

      Unit
    }
  }

  val handleDismiss = remember(currentUrl) {
    {
      component.hideDialog()

      if (currentUrl.isEmpty()) {
        component.showEditUrlDialog()
      }
    }
  }

  val primaryColor = MaterialTheme.colorScheme.primary

  val creditsText = remember(primaryColor) {
    buildAnnotatedString {
      append(text = memosCreditsText)

      pushLink(
        LinkAnnotation.Clickable(
          tag = "memos",
          styles = TextLinkStyles(SpanStyle(color = primaryColor)),
          linkInteractionListener = { handleOpenMemos() },
        ),
      )

      withStyle(style = SpanStyle(color = primaryColor)) {
        append(text = memosUrlText)
      }

      pop()
      append(text = dotText)
    }
  }

  AlertDialog(
    modifier = Modifier.width(400.dp),
    title = {},
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          text = stringResource(Res.string.app_desc, appVersion),
          style = MaterialTheme.typography.bodyMedium,
        )

        Text(
          text = stringResource(Res.string.memos_desc),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
          text = creditsText,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    },
    confirmButton = {
      TextButton(
        modifier = Modifier.mouseHoverIcon(),
        onClick = handleDismiss,
      ) {
        Text(text = stringResource(Res.string.close))
      }
    },
    onDismissRequest = handleDismiss,
  )
}
