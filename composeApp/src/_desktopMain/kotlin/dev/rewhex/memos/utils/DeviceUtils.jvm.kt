package dev.rewhex.memos.utils

import dev.rewhex.memos.BuildKonfig
import dev.rewhex.memos.types.DeviceSystem
import java.awt.Desktop
import java.net.URI
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

actual object DeviceUtils : KoinComponent {
  actual suspend fun openUrl(url: String) {
    withContext(Dispatchers.IO) {
      if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(url))
      }
    }
  }

  actual fun getSystem(): DeviceSystem {
    val result by lazy(LazyThreadSafetyMode.NONE) {
      System.getProperty("os.name").lowercase(Locale.getDefault())
    }

    return when {
      result.contains("linux") -> DeviceSystem.Linux
      result.contains("win") -> DeviceSystem.Windows
      result.contains("mac") -> DeviceSystem.MacOS
      else -> DeviceSystem.Unknown
    }
  }

  actual fun getAppVersion(): String {
    return BuildKonfig.APP_VERSION
  }
}
