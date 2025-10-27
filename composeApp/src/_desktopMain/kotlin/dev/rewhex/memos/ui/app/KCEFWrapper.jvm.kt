package dev.rewhex.memos.ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dev.datlag.kcef.KCEF
import dev.datlag.kcef.KCEFBuilder
import dev.rewhex.memos.STORAGE_DIR_PATH
import dev.rewhex.memos.ui.ContentAlpha
import dev.rewhex.memos.ui.Spacings
import java.io.File
import kotlin.math.max
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import memos.composeapp.generated.resources.Res
import memos.composeapp.generated.resources.downloading_webview
import memos.composeapp.generated.resources.please_wait
import memos.composeapp.generated.resources.restart_required
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun KCEFWrapper(content: @Composable () -> Unit) {
  var isInitialized by remember { mutableStateOf(false) }
  var isDownloading by remember { mutableStateOf(false) }
  var isRestartRequired by remember { mutableStateOf(false) }
  var downloadProgress by remember { mutableStateOf(0f) }

  val download = remember { KCEFBuilder.Download.Builder().github().build() }

  LaunchedEffect(Unit) {
    withContext(Dispatchers.IO) {
      KCEF.init(
        builder = {
          installDir(File(STORAGE_DIR_PATH, "kcef-bundle"))

          KCEFBuilder.Download.Builder()
            .github { release("jbr-release-17.0.10b1087.23") }
            .buffer(download.bufferSize)
            .build()

          progress {
            onDownloading {
              isDownloading = true
              downloadProgress = max(it, 0F)
            }

            onInitialized {
              isDownloading = false
              isInitialized = true
            }
          }

          settings {
            cachePath = File(STORAGE_DIR_PATH, "cache").absolutePath
          }
        },
        onError = { it?.printStackTrace() },
        onRestartRequired = {
          // all required CEF packages downloaded but
          // the application needs a restart to load them (unlikely to happen)
          isRestartRequired = true
        },
      )
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      KCEF.disposeBlocking()
    }
  }

  if (isRestartRequired || !isInitialized) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      if (isRestartRequired) {
        Text(
          text = stringResource(Res.string.restart_required),
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.high),
          textAlign = TextAlign.Center,
        )
      } else if (isDownloading) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            modifier = Modifier.padding(bottom = Spacings.S_4),
            text = stringResource(Res.string.please_wait),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.high),
            textAlign = TextAlign.Center,
          )

          Text(
            text = stringResource(Res.string.downloading_webview, downloadProgress.toInt()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium),
            textAlign = TextAlign.Center,
          )
        }
      }
    }
  } else {
    content()
  }
}
