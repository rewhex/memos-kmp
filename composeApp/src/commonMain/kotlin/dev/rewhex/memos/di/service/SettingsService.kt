package dev.rewhex.memos.di.service

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.rewhex.memos.types.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SettingsState {
  data object Loading : SettingsState
  data class Loaded(val settings: Settings) : SettingsState
}

class SettingsService(
  private val coroutineScope: CoroutineScope,
  private val dataStore: DataStore<Preferences>,
) {
  private object Keys {
    val MEMOS_URL = stringPreferencesKey("memos_url")
  }

  val state: StateFlow<SettingsState> = dataStore.data.catch { exception ->
    if (exception is IOException) {
      emit(emptyPreferences())
    } else {
      throw exception
    }
  }.map { prefs ->
    SettingsState.Loaded(Settings(memosUrl = prefs[Keys.MEMOS_URL]))
  }.stateIn(
    scope = coroutineScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = SettingsState.Loading,
  )

  fun saveMemosUrl(value: String?) {
    coroutineScope.launch {
      dataStore.edit { prefs ->
        if (value != null) {
          prefs[Keys.MEMOS_URL] = value
        } else {
          prefs.remove(Keys.MEMOS_URL)
        }
      }
    }
  }
}
