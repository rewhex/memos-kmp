package dev.rewhex.memos.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState

@Composable
expect fun CustomWebView(
  modifier: Modifier,
  state: WebViewState,
  navigator: WebViewNavigator,
)
