package dev.rewhex.memos.constants

import androidx.compose.ui.unit.dp
import dev.rewhex.memos.types.DeviceSystem
import dev.rewhex.memos.utils.DeviceUtils

private val SYSTEM = DeviceUtils.getSystem()

val IS_MAC_OS = SYSTEM == DeviceSystem.MacOS

const val DESKTOP_WINDOW_MIN_HEIGHT_INT = 420
const val DESKTOP_WINDOW_MIN_WIDTH_INT = 420

val DESKTOP_TOOLBAR_HEIGHT = 52.dp
