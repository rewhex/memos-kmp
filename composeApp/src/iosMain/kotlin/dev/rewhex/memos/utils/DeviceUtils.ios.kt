package dev.rewhex.memos.utils

import dev.rewhex.memos.BuildKonfig
import dev.rewhex.memos.types.DeviceSystem
import kotlinx.io.files.Path
import org.koin.core.component.KoinComponent
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSBundle
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIApplication

actual object DeviceUtils : KoinComponent {
  actual suspend fun openUrl(url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return

    if (!UIApplication.sharedApplication.canOpenURL(nsUrl)) {
      return
    }

    UIApplication.sharedApplication.openURL(
      url = nsUrl,
      options = emptyMap<Any?, Any?>(),
      completionHandler = {},
    )
  }

  actual fun getSystem(): DeviceSystem {
    return DeviceSystem.IOS
  }

  actual fun getAppVersion(): String {
    return (NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") ?: "0").toString()
  }
}

fun getDataDirPath() = Path(
  NSSearchPathForDirectoriesInDomains(
    NSApplicationSupportDirectory,
    NSUserDomainMask,
    true,
  ).firstOrNull().toString(),
  BuildKonfig.APP_PACKAGE,
)
