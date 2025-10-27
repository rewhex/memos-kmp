package dev.rewhex.memos.utils

import javax.swing.SwingUtilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

inline fun <reified T> runOnUiThread(crossinline block: () -> T): T {
  if (SwingUtilities.isEventDispatchThread()) {
    return block()
  }

  var error: Throwable? = null
  var result: T? = null

  SwingUtilities.invokeAndWait {
    try {
      result = block()
    } catch (e: Throwable) {
      error = e
    }
  }

  error?.also { throw it }

  return result!!.cast<T>()
}

fun debounce(
  waitMs: Long,
  coroutineScope: CoroutineScope,
  action: () -> Unit,
): () -> Unit {
  var job: Job? = null
  val channel = Channel<Unit>(Channel.CONFLATED)

  coroutineScope.launch {
    for (event in channel) {
      job?.cancel()
      job = launch {
        delay(waitMs)
        action()
      }
    }
  }

  return {
    channel.trySend(Unit)
  }
}
