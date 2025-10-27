import SwiftUI
import ComposeApp

@UIApplicationMain
class AppDelegate: NSObject, UIApplicationDelegate {
  var window: UIWindow?

  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    KoinKt.doInitKoin(appDeclaration: nil)

    self.window = UIWindow(frame: UIScreen.main.bounds)
    let mainViewController = UIHostingController(rootView: ContentView())
    self.window!.rootViewController = mainViewController
    self.window!.makeKeyAndVisible()

    return true
  }
}
