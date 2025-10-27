package dev.rewhex.memos.navigation

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.backhandler.BackHandlerOwner

interface RootComponent : BackHandlerOwner {
  val childStack: Value<ChildStack<*, Child>>
  val dialog: Value<ChildSlot<*, DialogChild>>

  val backDispatcher: BackDispatcher

  fun navigateToHomeScreen()

  fun onBackClicked()

  fun showAboutDialog()

  fun showEditUrlDialog()

  fun hideDialog()

  sealed class Child {
    data object InitialScreen : Child()

    data object HomeScreen : Child()
  }

  sealed class DialogChild {
    data object AboutDialog : DialogChild()

    data object EditUrlDialog : DialogChild()
  }
}
