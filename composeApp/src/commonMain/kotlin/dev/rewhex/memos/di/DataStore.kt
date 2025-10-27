package dev.rewhex.memos.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.CoroutineScope
import okio.Path.Companion.toPath

fun createDataStore(
  coroutineScope: CoroutineScope,
  producePath: () -> String,
): DataStore<Preferences> {
  return PreferenceDataStoreFactory.createWithPath(
    scope = coroutineScope,
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
    migrations = emptyList(),
    produceFile = { producePath().toPath() },
  )
}

const val dataStoreFileName = "app.preferences_pb"
