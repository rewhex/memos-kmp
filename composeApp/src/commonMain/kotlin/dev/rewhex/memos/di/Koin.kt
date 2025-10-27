package dev.rewhex.memos.di

import dev.rewhex.memos.di.service.HttpClientService
import dev.rewhex.memos.di.service.NetworkStateService
import dev.rewhex.memos.di.service.SettingsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val dataStoreModule: Module

val mainModule = module {
  factory { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

  singleOf(::HttpClientService)
  singleOf(::NetworkStateService)
  singleOf(::SettingsService)
}

fun initKoin(appDeclaration: KoinAppDeclaration? = null) {
  startKoin {
    appDeclaration?.invoke(this)

    modules(
      dataStoreModule,
      mainModule,
    )
  }
}
