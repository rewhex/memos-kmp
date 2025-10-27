package dev.rewhex.memos.ui.modifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import java.awt.Cursor

@Composable
actual fun Modifier.mouseHoverIcon() = this.pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR)))
