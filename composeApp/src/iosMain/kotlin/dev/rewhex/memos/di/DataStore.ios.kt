package dev.rewhex.memos.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.rewhex.memos.utils.getDataDirPath
import kotlinx.coroutines.CoroutineScope

fun dataStore(coroutineScope: CoroutineScope): DataStore<Preferences> {
  return createDataStore(
    coroutineScope = coroutineScope,
    producePath = { "${getDataDirPath()}/$dataStoreFileName" },
  )
}
