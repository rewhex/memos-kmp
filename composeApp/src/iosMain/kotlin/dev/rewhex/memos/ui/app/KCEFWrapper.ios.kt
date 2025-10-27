package dev.rewhex.memos.ui.app

import androidx.compose.runtime.Composable

@Composable
actual fun KCEFWrapper(content: @Composable () -> Unit) {
  content()
}
