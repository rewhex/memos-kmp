package dev.rewhex.memos.utils

import dev.rewhex.memos.types.DeviceSystem
import org.koin.core.component.KoinComponent

expect object DeviceUtils : KoinComponent {
  suspend fun openUrl(url: String)

  fun getSystem(): DeviceSystem

  fun getAppVersion(): String
}
