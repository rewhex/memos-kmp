package dev.rewhex.memos

import android.app.Application
import dev.rewhex.memos.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    initKoin {
      androidContext(this@MainApplication)
    }
  }
}
