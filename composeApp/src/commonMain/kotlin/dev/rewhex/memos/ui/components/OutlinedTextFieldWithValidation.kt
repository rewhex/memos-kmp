package dev.rewhex.memos.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import dev.rewhex.memos.ui.Size
import dev.rewhex.memos.ui.Spacings
import memos.composeapp.generated.resources.Res
import memos.composeapp.generated.resources.icon_error
import org.jetbrains.compose.resources.painterResource

@Composable
fun OutlinedTextFieldWithValidation(
  modifier: Modifier = Modifier,
  columnModifier: Modifier = Modifier,
  value: TextFieldValue,
  onValueChange: (TextFieldValue) -> Unit,
  enabled: Boolean = true,
  readOnly: Boolean = false,
  textStyle: TextStyle = LocalTextStyle.current,
  label: @Composable (() -> Unit)? = null,
  placeholder: @Composable (() -> Unit)? = null,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  isError: Boolean = false,
  errorMessage: String? = null,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  singleLine: Boolean = false,
  maxLines: Int = Int.MAX_VALUE,
) {
  Column(modifier = columnModifier) {
    OutlinedTextField(
      modifier = modifier,
      value = value,
      onValueChange = onValueChange,
      enabled = enabled,
      readOnly = readOnly,
      textStyle = textStyle,
      label = label,
      placeholder = placeholder,
      leadingIcon = leadingIcon,
      trailingIcon = if (isError || trailingIcon != null) {
        {
          TrailingIcon(
            isError = isError,
            trailingIcon = trailingIcon,
          )
        }
      } else null,
      isError = isError,
      interactionSource = interactionSource,
      visualTransformation = visualTransformation,
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      singleLine = singleLine,
      maxLines = maxLines,
    )

    ErrorMessage(
      isError = isError,
      errorMessage = errorMessage,
    )
  }
}

@Composable
private fun TrailingIcon(
  isError: Boolean,
  trailingIcon: @Composable (() -> Unit)? = null,
) {
  if (isError) {
    Icon(
      painter = painterResource(Res.drawable.icon_error),
      contentDescription = null,
      tint = MaterialTheme.colorScheme.error,
    )
  } else {
    trailingIcon?.invoke()
  }
}

@Composable
private fun ErrorMessage(
  isError: Boolean,
  errorMessage: String? = null,
) {
  AnimatedVisibility(
    visible = isError && errorMessage != null,
    enter = fadeIn() + expandVertically(),
    exit = fadeOut() + shrinkVertically(),
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .height(Size.S_16)
        .padding(horizontal = Spacings.S_16),
      color = Color.Transparent,
    ) {
      Text(
        text = errorMessage ?: "",
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
      )
    }
  }
}
