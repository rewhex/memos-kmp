package dev.rewhex.memos.di.service

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

actual fun getHttpClientWithEngine(block: HttpClientConfig<*>.() -> Unit): HttpClient {
  return HttpClient(OkHttp) {
    engine {
      config {
        val trustAllCerts = arrayOf<TrustManager>(
          @Suppress("CustomX509TrustManager")
          object : X509TrustManager {
            @Suppress("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Suppress("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
          },
        )

        val sslContext = SSLContext.getInstance("SSL").apply {
          init(null, trustAllCerts, java.security.SecureRandom())
        }

        sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
      }
    }

    block()
  }
}
