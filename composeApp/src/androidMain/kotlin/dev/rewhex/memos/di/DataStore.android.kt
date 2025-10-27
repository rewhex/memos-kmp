package dev.rewhex.memos.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope

fun dataStore(
  context: Context,
  coroutineScope: CoroutineScope,
): DataStore<Preferences> {
  return createDataStore(
    coroutineScope = coroutineScope,
    producePath = { context.dataDir.resolve(dataStoreFileName).canonicalPath },
  )
}
