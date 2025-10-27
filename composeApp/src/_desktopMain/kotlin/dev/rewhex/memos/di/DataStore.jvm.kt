package dev.rewhex.memos.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.rewhex.memos.STORAGE_DIR_PATH
import java.io.File
import kotlinx.coroutines.CoroutineScope

fun dataStore(coroutineScope: CoroutineScope): DataStore<Preferences> {
  return createDataStore(
    coroutineScope = coroutineScope,
    producePath = {
      File("$STORAGE_DIR_PATH/$dataStoreFileName").canonicalPath
    },
  )
}
