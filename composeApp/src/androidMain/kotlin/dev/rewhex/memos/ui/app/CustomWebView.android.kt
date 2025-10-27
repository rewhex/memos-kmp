package dev.rewhex.memos.ui.app

import android.annotation.SuppressLint
import android.app.Application
import android.net.http.SslError
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.AccompanistWebViewClient
import com.multiplatform.webview.web.PlatformWebViewParams
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun CustomWebView(
  modifier: Modifier,
  state: WebViewState,
  navigator: WebViewNavigator,
) {
  val activity = LocalActivity.current

  WebView(
    modifier = modifier,
    state = state,
    navigator = navigator,
    platformWebViewParams = PlatformWebViewParams(
      client = remember { CustomWebViewClient() },
    ),
    factory = {
      val webview = android.webkit.WebView(it.context)

      CookieManager.getInstance().setAcceptCookie(true)
      CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true)

      webview.isVerticalScrollBarEnabled = false
      webview.isHorizontalScrollBarEnabled = false

      webview.settings.cacheMode = WebSettings.LOAD_DEFAULT
      webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

      // Duplicate just to be sure it's applied to the webview
      webview.settings.domStorageEnabled = true
      webview.settings.javaScriptEnabled = true

      val processName = Application.getProcessName()

      if (processName != activity?.packageName) {
        android.webkit.WebView.setDataDirectorySuffix(processName)
      }

      webview
    },
  )
}

private class CustomWebViewClient : AccompanistWebViewClient() {
  @SuppressLint("WebViewClientOnReceivedSslError")
  override fun onReceivedSslError(
    view: android.webkit.WebView?,
    handler: SslErrorHandler,
    error: SslError?,
  ) {
    handler.proceed()
  }

  override fun onReceivedError(
    view: android.webkit.WebView,
    request: WebResourceRequest?,
    error: WebResourceError?,
  ) {
    super.onReceivedError(view, request, error)
    println(error)
  }

  override fun onReceivedHttpError(
    view: android.webkit.WebView?,
    request: WebResourceRequest?,
    errorResponse: WebResourceResponse?,
  ) {
    super.onReceivedHttpError(view, request, errorResponse)
    println(errorResponse)
  }
}
