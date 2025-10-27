package dev.rewhex.memos.di.service

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

expect fun getHttpClientWithEngine(block: HttpClientConfig<*>.() -> Unit): HttpClient

class HttpClientService(
  private val coroutineScope: CoroutineScope,
) {
  private val httpClient = getHttpClientWithEngine {
    install(Logging)
    install(HttpTimeout) { requestTimeoutMillis = 6000 }

    install(HttpRequestRetry) {
      retryOnServerErrors(maxRetries = 1)
    }
  }

  internal suspend inline fun getIsServerAccessible(url: String): Boolean {
    return suspendCoroutine { continuation ->
      coroutineScope.launch {
        try {
          val httpResponse = httpClient.get(url) {
            headers {
              append(HttpHeaders.CacheControl, "no-store")
            }
          }

          continuation.resumeWith(Result.success(httpResponse.status.isSuccess()))
        } catch (e: Exception) {
          e.printStackTrace()
          continuation.resumeWith(Result.failure(e))
        }
      }
    }
  }
}
