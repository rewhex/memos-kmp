# KMP WebView Wrapper for [Memos](https://github.com/usememos/memos)

A Kotlin Multiplatform (KMP) WebView wrapper designed for the [Memos](https://github.com/usememos/memos) project.
This repository is currently **a work in progress (WIP)**.

---

## Current Status / Known Issues

The following tasks are still in progress or need improvements:

* **Desktop WebView z-index issue**\
  Components cannot be rendered above the WebView due to z-index limitations.

* **Windows app crash in release mode**\
  The app crashes when built in release mode. (Possible cause: ProGuard configuration.)

* **System bar and keyboard handling**\
  Better spacing management is needed for system bars across different pages and dialogs.
  The web page should also respond dynamically to keyboard size changes.

* **Self-signed certificates**\
  Need to support self-signed certificates securely, *without* allowing all untrusted certs (to prevent MITM attacks).

* **Generate App Icons for all apps**\
  Missing: composeApp/src/commonMain/composeResources/drawable/icon_logo.xml\
  Missing: iOS AppIcon.png (1024x1024)\
  Missing: iOS Logo.png (512x512)\
  Missing: Android mipmaps

---

## Contributing

Contributions and pull requests are **very welcome**, especially for fixing the issues above or improving cross-platform behavior.
