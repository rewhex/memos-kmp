package dev.rewhex.memos.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.net.toUri
import dev.rewhex.memos.types.DeviceSystem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual object DeviceUtils : KoinComponent {
  actual suspend fun openUrl(url: String) {
    val context by inject<Context>()
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())

    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    context.startActivity(intent)
  }

  actual fun getSystem(): DeviceSystem {
    return DeviceSystem.Android
  }

  actual fun getAppVersion(): String {
    return try {
      getPackageInfo()?.versionName ?: "0"
    } catch (e: Exception) {
      println(e)

      "0"
    }
  }

  private fun getPackageInfo(): PackageInfo? {
    runCatching {
      val context by inject<Context>()

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
      }

      return context.packageManager.getPackageInfo(context.packageName, 0)
    }

    return null
  }
}
