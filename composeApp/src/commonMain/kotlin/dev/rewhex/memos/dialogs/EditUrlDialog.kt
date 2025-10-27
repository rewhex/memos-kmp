package dev.rewhex.memos.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.rewhex.memos.di.service.SettingsService
import dev.rewhex.memos.di.service.SettingsState
import dev.rewhex.memos.navigation.RootComponent
import dev.rewhex.memos.ui.ContentAlpha
import dev.rewhex.memos.ui.Size
import dev.rewhex.memos.ui.components.OutlinedTextFieldWithValidation
import dev.rewhex.memos.ui.modifier.mouseHoverIcon
import kotlinx.coroutines.delay
import memos.composeapp.generated.resources.Res
import memos.composeapp.generated.resources.about
import memos.composeapp.generated.resources.applyLabel
import memos.composeapp.generated.resources.cancel
import memos.composeapp.generated.resources.error_field_must_not_be_empty
import memos.composeapp.generated.resources.error_invalid_url
import memos.composeapp.generated.resources.icon_info
import memos.composeapp.generated.resources.memos_instance_url
import memos.composeapp.generated.resources.memos_instance_url_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun EditUrlDialog(component: RootComponent) {
  val settingsService = koinInject<SettingsService>()
  val settingsState by settingsService.state.collectAsState()

  val settings = remember(settingsState) {
    when (val state = settingsState) {
      is SettingsState.Loading -> null
      is SettingsState.Loaded -> state.settings
    }
  }

  val currentUrl = settings?.memosUrl ?: ""

  var textFieldValue by remember(currentUrl) {
    mutableStateOf(
      TextFieldValue(
        text = currentUrl,
        selection = TextRange(currentUrl.length),
      ),
    )
  }

  var errorMessage by remember { mutableStateOf<String?>(null) }
  val focusRequester = remember { FocusRequester() }

  val placeholder = stringResource(Res.string.memos_instance_url_placeholder)
  val errorFieldEmpty = stringResource(Res.string.error_field_must_not_be_empty)
  val errorInvalidUrl = stringResource(Res.string.error_invalid_url)

  val handleValidate: (String) -> String? = remember(errorFieldEmpty, errorInvalidUrl) {
    { text ->
      when {
        text.isBlank() -> errorFieldEmpty
        !isValidUrl(text) -> errorInvalidUrl
        else -> null
      }
    }
  }

  val handleChange: (TextFieldValue) -> Unit = remember(handleValidate) {
    { newValue ->
      textFieldValue = newValue
      errorMessage = handleValidate(newValue.text)
    }
  }

  val isConfirmAllowed = textFieldValue.text.isNotBlank() && errorMessage == null

  val handleDismiss = remember(currentUrl) {
    {
      textFieldValue = TextFieldValue(text = currentUrl, selection = TextRange(currentUrl.length))
      errorMessage = null
      component.hideDialog()
    }
  }

  val handleConfirm = remember(isConfirmAllowed, settingsService) {
    {
      if (isConfirmAllowed) {
        settingsService.saveMemosUrl(textFieldValue.text.takeIf { it.isNotBlank() })
        component.hideDialog()
      }
    }
  }

  LaunchedEffect(Unit) {
    delay(500L)
    focusRequester.requestFocus()
  }

  AlertDialog(
    modifier = Modifier.width(400.dp),
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = stringResource(Res.string.memos_instance_url),
          style = MaterialTheme.typography.bodyLarge,
        )

        IconButton(
          modifier = Modifier.mouseHoverIcon(),
          onClick = component::showAboutDialog,
        ) {
          Icon(
            modifier = Modifier.size(Size.S_20),
            painter = painterResource(Res.drawable.icon_info),
            contentDescription = stringResource(Res.string.about),
          )
        }
      }
    },
    text = {
      OutlinedTextFieldWithValidation(
        modifier = Modifier
          .focusRequester(focusRequester)
          .fillMaxWidth(),
        isError = errorMessage != null,
        errorMessage = errorMessage,
        placeholder = { Text(text = placeholder) },
        singleLine = true,
        maxLines = 1,
        value = textFieldValue,
        onValueChange = handleChange,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { handleConfirm() }),
      )
    },
    dismissButton = {
      TextButton(
        modifier = Modifier.mouseHoverIcon(),
        onClick = handleDismiss,
      ) {
        Text(text = stringResource(Res.string.cancel))
      }
    },
    confirmButton = {
      TextButton(
        modifier = if (isConfirmAllowed) Modifier.mouseHoverIcon() else Modifier,
        enabled = isConfirmAllowed,
        onClick = handleConfirm,
      ) {
        Text(
          text = stringResource(Res.string.applyLabel),
          color = when (isConfirmAllowed) {
            true -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.disabled)
          },
        )
      }
    },
    onDismissRequest = handleDismiss,
  )
}

private fun isValidUrl(url: String): Boolean {
  return try {
    val regex = Regex("^(https?)://[\\w.-]+(?:\\.[\\w\\.-]+)+[/#?]?.*$")

    return regex.matches(url.trim())
  } catch (_: Exception) {
    false
  }
}
