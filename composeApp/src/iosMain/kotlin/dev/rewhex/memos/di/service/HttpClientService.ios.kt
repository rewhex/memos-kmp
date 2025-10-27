package dev.rewhex.memos.di.service

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

actual fun getHttpClientWithEngine(block: HttpClientConfig<*>.() -> Unit): HttpClient {
  return HttpClient(Darwin) {
    block()
  }
}
