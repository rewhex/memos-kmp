package dev.rewhex.memos.translations

import dev.rewhex.memos.types.MenuStrings
import java.util.Locale

private val stringsEn = MenuStrings(
  edit = "Edit",
  undo = "Undo",
  redo = "Redo",
  cut = "Cut",
  copy = "Copy",
  paste = "Paste",
  selectAll = "Select All",
  window = "Window",
  minimize = "Minimize",
  zoom = "Zoom",
  close = "Close",
)

private val stringsEs = MenuStrings(
  edit = "Editar",
  undo = "Deshacer",
  redo = "Rehacer",
  cut = "Cortar",
  copy = "Copiar",
  paste = "Pegar",
  selectAll = "Seleccionar todo",
  window = "Ventana",
  minimize = "Minimizar",
  zoom = "Zoom",
  close = "Cerrar",
)

private val stringsFr = MenuStrings(
  edit = "Édition",
  undo = "Annuler",
  redo = "Rétablir",
  cut = "Couper",
  copy = "Copier",
  paste = "Coller",
  selectAll = "Tout sélectionner",
  window = "Fenêtre",
  minimize = "Réduire",
  zoom = "Zoom",
  close = "Fermer",
)

private val stringsDe = MenuStrings(
  edit = "Bearbeiten",
  undo = "Widerrufen",
  redo = "Wiederholen",
  cut = "Ausschneiden",
  copy = "Kopieren",
  paste = "Einsetzen",
  selectAll = "Alles auswählen",
  window = "Fenster",
  minimize = "Minimieren",
  zoom = "Zoom",
  close = "Schließen",
)

private val stringsIt = MenuStrings(
  edit = "Modifica",
  undo = "Annulla",
  redo = "Ripeti",
  cut = "Taglia",
  copy = "Copia",
  paste = "Incolla",
  selectAll = "Seleziona tutto",
  window = "Finestra",
  minimize = "Minimizza",
  zoom = "Zoom",
  close = "Chiudi",
)

private val stringsPt = MenuStrings(
  edit = "Editar",
  undo = "Desfazer",
  redo = "Refazer",
  cut = "Cortar",
  copy = "Copiar",
  paste = "Colar",
  selectAll = "Selecionar tudo",
  window = "Janela",
  minimize = "Minimizar",
  zoom = "Zoom",
  close = "Fechar",
)

private val stringsJa = MenuStrings(
  edit = "編集",
  undo = "取り消し",
  redo = "やり直し",
  cut = "カット",
  copy = "コピー",
  paste = "ペースト",
  selectAll = "すべて選択",
  window = "ウインドウ",
  minimize = "しまう",
  zoom = "拡大/縮小",
  close = "閉じる",
)

private val stringsZh = MenuStrings(
  edit = "编辑",
  undo = "撤销",
  redo = "重做",
  cut = "剪切",
  copy = "拷贝",
  paste = "粘贴",
  selectAll = "全选",
  window = "窗口",
  minimize = "最小化",
  zoom = "缩放",
  close = "关闭",
)

private val stringsKo = MenuStrings(
  edit = "편집",
  undo = "실행 취소",
  redo = "다시 실행",
  cut = "잘라내기",
  copy = "복사",
  paste = "붙여넣기",
  selectAll = "모두 선택",
  window = "윈도우",
  minimize = "최소화",
  zoom = "확대/축소",
  close = "닫기",
)

private val stringsRu = MenuStrings(
  edit = "Правка",
  undo = "Отменить",
  redo = "Повторить",
  cut = "Вырезать",
  copy = "Копировать",
  paste = "Вставить",
  selectAll = "Выбрать все",
  window = "Окно",
  minimize = "Свернуть",
  zoom = "Масштаб",
  close = "Закрыть",
)

private val stringsNl = MenuStrings(
  edit = "Bewerk",
  undo = "Ongedaan maken",
  redo = "Opnieuw",
  cut = "Knip",
  copy = "Kopieer",
  paste = "Plak",
  selectAll = "Selecteer alles",
  window = "Venster",
  minimize = "Minimaliseer",
  zoom = "Zoom",
  close = "Sluit",
)

fun getMacOSMenuStrings(): MenuStrings {
  return when (Locale.getDefault().language) {
    "en" -> stringsEn
    "es" -> stringsEs
    "fr" -> stringsFr
    "de" -> stringsDe
    "it" -> stringsIt
    "pt" -> stringsPt
    "ja" -> stringsJa
    "zh" -> stringsZh
    "ko" -> stringsKo
    "ru" -> stringsRu
    "nl" -> stringsNl
    else -> stringsEn // Default to English
  }
}
