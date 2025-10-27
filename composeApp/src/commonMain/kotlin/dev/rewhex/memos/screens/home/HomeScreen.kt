package dev.rewhex.memos.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import dev.rewhex.memos.di.service.SettingsService
import dev.rewhex.memos.di.service.SettingsState
import dev.rewhex.memos.navigation.RootComponent
import dev.rewhex.memos.ui.Spacings
import dev.rewhex.memos.ui.app.CustomWebView
import dev.rewhex.memos.ui.components.RefreshButton
import dev.rewhex.memos.ui.components.SettingsButton
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(component: RootComponent) {
  val settingsService = koinInject<SettingsService>()
  val settingsState by settingsService.state.collectAsState()

  val memosUrl = when (val state = settingsState) {
    is SettingsState.Loaded -> state.settings.memosUrl
    else -> null
  }

  val state = rememberWebViewState(
    url = memosUrl ?: "",
    extraSettings = {
      backgroundColor = Color.Transparent
      isJavaScriptEnabled = true
      supportZoom = false

      androidWebSettings.domStorageEnabled = true

      iOSWebSettings.backgroundColor = Color.Transparent
      iOSWebSettings.underPageBackgroundColor = Color.Transparent
      iOSWebSettings.showHorizontalScrollIndicator = false
      iOSWebSettings.showVerticalScrollIndicator = false
      iOSWebSettings.opaque = true

      desktopWebSettings.transparent = true
    },
  )

  val navigator = rememberWebViewNavigator()

  var isInitiallyLoaded by remember { mutableStateOf(false) }

  val systemBars = WindowInsets
    .safeDrawing
    .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
    .asPaddingValues()

  val density = LocalDensity.current

  val statusBarHeight = with(density) { systemBars.calculateTopPadding().value.toInt() }
  val navigationBarHeight = with(density) { systemBars.calculateBottomPadding().value.toInt() }

  LaunchedEffect(isInitiallyLoaded, state.isLoading) {
    if (!isInitiallyLoaded) {
      // Semi-dark backgrounds to fix white page flickering
      navigator.evaluateJavaScript(
        """
        document.documentElement.style.setProperty('background-color', '#1b1b1b', 'important');
        document.body.style.setProperty('background-color', '#1b1b1b', 'important');
        """.trimIndent(),
      )
    }

    if (!state.isLoading && !isInitiallyLoaded) {
      isInitiallyLoaded = true

      // TODO: apply bottom padding when keyboard/network overlay size changed, check WindowBottomInset.kt
      navigator.evaluateJavaScript(
        """
        function applyStyles() {
          document.querySelector('.sticky').style.paddingTop = 'calc(${statusBarHeight}px + 12px)'
          // document.querySelector('#root').style.paddingBottom = '${navigationBarHeight}px'

          try { document.querySelector('[data-slot="sheet-trigger"]').querySelector('div').remove() } catch(e) {}
        }

        function waitForElementsAndApplyStyles() {
          const element = document.querySelector('.sticky')

          if (element) {
            applyStyles()

            setInterval(function() {
              const sidebarTitleNode = document.querySelector('#header-memos')
              const closeDialogNode = document.querySelector('div[role="dialog"] > button[type="button"]')

              if (sidebarTitleNode && sidebarTitleNode.previousSibling) {
                sidebarTitleNode.previousSibling.remove()
              }

              if (closeDialogNode) {
                closeDialogNode.remove()
              }
            }, 300)

            const observer = new MutationObserver(function(mutations) {
              let shouldReapply = false

              mutations.forEach(function(mutation) {
                if (mutation.type === 'childList') {
                  shouldReapply = true
                } else if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
                  shouldReapply = true
                }
              })

              if (shouldReapply) {
                applyStyles()
              }
            })

            observer.observe(document.body, {
              childList: true,
              subtree: true,
              attributes: true,
              attributeFilter: ['class']
            })
          } else {
            setTimeout(waitForElementsAndApplyStyles, 50)
          }
        }

        waitForElementsAndApplyStyles()
        """.trimIndent(),
      )
    }
  }

  Box(Modifier.fillMaxSize()) {
    TopAppBar(
      modifier = Modifier.zIndex(1f),
      title = {},
      actions = {
        Row(Modifier.padding(end = Spacings.S_4)) {
          RefreshButton(
            isLoading = state.isLoading,
            onPress = navigator::reload,
          )

          SettingsButton(onPress = component::showEditUrlDialog)
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent,
      ),
    )

    CustomWebView(
      modifier = Modifier
//        .padding(bottom = windowBottomInsetsPadding())
        .fillMaxSize()
        .alpha(if (isInitiallyLoaded) 1f else 0f),
      state = state,
      navigator = navigator,
    )
  }
}
