package dev.rewhex.memos.utils

import dev.rewhex.memos.navigation.RootComponent
import java.awt.Desktop
import java.awt.desktop.AppReopenedListener

object EventUtils {
  fun addListeners(
    component: RootComponent,
    onAppReopened: () -> Unit,
  ) {
    val desktop = Desktop.getDesktop()

    // Used to reopen window after close button is pressed, or the window trying to open multiple times
    desktop.addAppEventListener(
      AppReopenedListener {
        onAppReopened()
      },
    )

    if (desktop.isSupported(Desktop.Action.APP_ABOUT)) {
      desktop.setAboutHandler {
        component.showAboutDialog()
      }
    }

    if (desktop.isSupported(Desktop.Action.APP_PREFERENCES)) {
      desktop.setPreferencesHandler {
        component.showEditUrlDialog()
      }
    }

    if (desktop.isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
      desktop.setQuitHandler { _, response ->
        response.performQuit()
      }
    }
  }
}
