package dev.rewhex.memos.di

import org.koin.dsl.module

actual val dataStoreModule = module {
  single { dataStore(get()) }
}
