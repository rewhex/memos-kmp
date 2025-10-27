package dev.rewhex.memos.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import dev.rewhex.memos.translations.getMacOSMenuStrings
import java.awt.event.KeyEvent
import kotlin.system.exitProcess

@Composable
fun FrameWindowScope.MenuBarView() {
  val menuStrings = remember { getMacOSMenuStrings() }

  MenuBar {
    Menu(text = menuStrings.edit) {
      Item(
        text = menuStrings.undo,
        onClick = {
          window.dispatchKeyShortcut(KeyEvent.VK_Z)
        },
        shortcut = KeyShortcut(Key.Z, meta = true), // Cmd+Z
      )

      Item(
        text = menuStrings.redo,
        onClick = {
          window.dispatchKeyShortcut(KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_Z)
        },
        shortcut = KeyShortcut(Key.Z, meta = true, shift = true), // Cmd+Shift+Z
      )

      Separator()

      Item(
        text = menuStrings.cut,
        onClick = {
          window.dispatchKeyShortcut(KeyEvent.VK_X)
        },
        shortcut = KeyShortcut(Key.X, meta = true), // Cmd+X
      )

      Item(
        text = menuStrings.copy,
        onClick = {
          window.dispatchKeyShortcut(KeyEvent.VK_C)
        },
        shortcut = KeyShortcut(Key.C, meta = true), // Cmd+C
      )

      Item(
        text = menuStrings.paste,
        onClick = {
          window.dispatchKeyShortcut(KeyEvent.VK_V)
        },
        shortcut = KeyShortcut(Key.V, meta = true), // Cmd+V
      )

      Separator()

      Item(
        text = menuStrings.selectAll,
        onClick = {
          window.dispatchKeyShortcut(KeyEvent.VK_A)
        },
        shortcut = KeyShortcut(Key.A, meta = true), // Cmd+A
      )
    }

    Menu(text = menuStrings.window) {
      Item(
        text = menuStrings.close,
        onClick = { exitProcess(0) },
        shortcut = KeyShortcut(Key.W, meta = true), // Cmd+W
      )
    }
  }
}

/**
 * Helper extension function for `ComposeWindow` to dispatch a key shortcut
 * with a single modifier (like Cmd+C).
 *
 * This finds the currently focused component in the window and sends
 * KEY_PRESSED and KEY_RELEASED events to it.
 *
 * @param keyCode The `java.awt.event.KeyEvent` virtual key code (e.g., `KeyEvent.VK_C`).
 */
private fun ComposeWindow.dispatchKeyShortcut(keyCode: Int) {
  val focusOwner = this.focusOwner

  if (focusOwner == null) {
    System.err.println("No component focused, cannot dispatch key event.")
    return
  }

  val modifiers = KeyEvent.META_DOWN_MASK // META is Cmd on macOS

  val pressEvent = KeyEvent(
    focusOwner,
    KeyEvent.KEY_PRESSED,
    System.currentTimeMillis(),
    modifiers,
    keyCode,
    KeyEvent.CHAR_UNDEFINED, // Use CHAR_UNDEFINED for non-char keys/shortcuts
  )

  focusOwner.dispatchEvent(pressEvent)

  val releaseEvent = KeyEvent(
    focusOwner,
    KeyEvent.KEY_RELEASED,
    System.currentTimeMillis(),
    modifiers,
    keyCode,
    KeyEvent.CHAR_UNDEFINED,
  )

  focusOwner.dispatchEvent(releaseEvent)
}

/**
 * Helper extension function for `ComposeWindow` to dispatch a key shortcut
 * with two modifiers (like Cmd+Shift+Z).
 *
 * @param modifier2 The second modifier mask (e.g., `KeyEvent.SHIFT_DOWN_MASK`).
 * @param keyCode The `java.awt.event.KeyEvent` virtual key code (e.g., `KeyEvent.VK_Z`).
 */
private fun ComposeWindow.dispatchKeyShortcut(modifier2: Int, keyCode: Int) {
  val focusOwner = this.focusOwner

  if (focusOwner == null) {
    System.err.println("No component focused, cannot dispatch key event.")
    return
  }

  // Combine the primary modifier (Cmd) with the secondary one (Shift)
  val modifiers = KeyEvent.META_DOWN_MASK or modifier2

  val pressEvent = KeyEvent(
    focusOwner,
    KeyEvent.KEY_PRESSED,
    System.currentTimeMillis(),
    modifiers,
    keyCode,
    KeyEvent.CHAR_UNDEFINED,
  )

  focusOwner.dispatchEvent(pressEvent)

  val releaseEvent = KeyEvent(
    focusOwner,
    KeyEvent.KEY_RELEASED,
    System.currentTimeMillis(),
    modifiers,
    keyCode,
    KeyEvent.CHAR_UNDEFINED,
  )

  focusOwner.dispatchEvent(releaseEvent)
}
