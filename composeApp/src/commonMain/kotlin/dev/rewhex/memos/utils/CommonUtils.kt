package dev.rewhex.memos.utils

inline fun <reified T : Any?> Any.cast(): T {
  return try {
    this as T
  } catch (e: Exception) {
    null as T
  }
}
