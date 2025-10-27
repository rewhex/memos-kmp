package dev.rewhex.memos.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable

class DefaultRootComponent(
  componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext, BackHandlerOwner {
  private val navigation = StackNavigation<ScreenConfig>()
  private val dialogNavigation = SlotNavigation<DialogConfig>()

  override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
    source = navigation,
    serializer = ScreenConfig.serializer(),
    initialConfiguration = ScreenConfig.InitialScreen,
    handleBackButton = true,
    childFactory = { config, _ ->
      when (config) {
        is ScreenConfig.InitialScreen -> RootComponent.Child.InitialScreen
        is ScreenConfig.HomeScreen -> RootComponent.Child.HomeScreen
      }
    },
  )

  override val dialog: Value<ChildSlot<*, RootComponent.DialogChild>> = childSlot(
    source = dialogNavigation,
    serializer = DialogConfig.serializer(),
    handleBackButton = true,
    childFactory = { config, _ ->
      when (config) {
        is DialogConfig.AboutDialog -> RootComponent.DialogChild.AboutDialog
        is DialogConfig.EditUrlDialog -> RootComponent.DialogChild.EditUrlDialog
      }
    },
  )

  override val backDispatcher: BackDispatcher = backHandler as BackDispatcher

  override fun navigateToHomeScreen() {
    navigation.replaceAll(ScreenConfig.HomeScreen)
  }

  override fun onBackClicked() {
    navigation.pop()
  }

  override fun showAboutDialog() {
    dialogNavigation.activate(DialogConfig.AboutDialog)
  }

  override fun showEditUrlDialog() {
    dialogNavigation.activate(DialogConfig.EditUrlDialog)
  }

  override fun hideDialog() {
    dialogNavigation.dismiss()
  }

  @Serializable
  private sealed interface ScreenConfig {
    @Serializable
    data object InitialScreen : ScreenConfig

    @Serializable
    data object HomeScreen : ScreenConfig
  }

  @Serializable
  private sealed interface DialogConfig {
    @Serializable
    data object AboutDialog : DialogConfig

    @Serializable
    data object EditUrlDialog : DialogConfig
  }
}
