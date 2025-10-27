import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
// import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

@Suppress("PrivatePropertyName")
private val APP_NAME = "Memos"

@Suppress("PrivatePropertyName")
private val APP_PACKAGE = "dev.rewhex.memos"

@Suppress("PrivatePropertyName")
private val VERSION_CODE = project.findProperty("VERSION_CODE").toString()

@Suppress("PrivatePropertyName")
private val VERSION_NAME = project.findProperty("VERSION_NAME").toString()

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinxSerialization)
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.composeHotReload)
  alias(libs.plugins.buildkonfig)
}

buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath(libs.buildkonfig.gradle.plugin)
  }
}

kotlin {
  val iosBuildType = NativeBuildType.DEBUG

  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }

  targets.configureEach {
    compilations.all {
      compileTaskProvider.configure {
        compilerOptions {
          freeCompilerArgs.addAll(
            "-opt-in=kotlin.ExperimentalUnsignedTypes,kotlin.RequiresOptIn,kotlin.Experimental",
            "-Xexpect-actual-classes",
            "-Xjvm-default=all",
          )
        }
      }

      tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
          jvmTarget.set(JvmTarget.JVM_17)
        }
      }

      tasks.withType<ProcessResources>().configureEach {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
      }
    }
  }

  androidTarget()

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach {
    it.binaries.framework(buildTypes = setOf(iosBuildType)) {
      baseName = "ComposeApp"
      isStatic = true
      binaryOption("bundleId", APP_PACKAGE)
    }
  }

  // jvm("desktop")

  sourceSets.commonMain.dependencies {
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.ui)
    implementation(compose.components.resources)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.decompose)
    implementation(libs.decompose.extensions.compose.experimental)
    implementation(libs.essenty.lifecycle)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    api(libs.compose.webview.multiplatform)
  }

  sourceSets.androidMain.dependencies {
    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.ktor.client.cio)
    implementation(libs.kotlinx.coroutines.android)
  }

  sourceSets.iosMain.dependencies {
    implementation(libs.ktor.client.darwin)
  }

// sourceSets.getByName("desktopMain") {
//   dependencies {
//     implementation(compose.desktop.currentOs)
//      implementation(compose.desktop.windows_x64)
//      implementation(compose.desktop.linux_x64)
//      implementation(compose.desktop.linux_arm64)
//      implementation(compose.desktop.macos_x64)
//      implementation(compose.desktop.macos_arm64)
//
//     implementation(libs.androidx.datastore.jvm)
//     implementation(libs.koin.core.jvm)
//     implementation(libs.ktor.client.okhttp)
//     implementation(libs.logback)
//   }
// }
}

buildkonfig {
  packageName = APP_PACKAGE

  defaultConfigs {
    buildConfigField(STRING, "APP", APP_NAME)
    buildConfigField(STRING, "APP_PACKAGE", APP_PACKAGE)
  }

  targetConfigs {
    create("desktop") {
      buildConfigField(STRING, "APP_VERSION", VERSION_NAME)
    }
  }
}

@Suppress("PrivatePropertyName")
private val MIN_SDK = 28

@Suppress("PrivatePropertyName")
private val TARGET_SDK = 36

android {
  namespace = APP_PACKAGE
  compileSdk = TARGET_SDK

  defaultConfig {
    applicationId = APP_PACKAGE
    minSdk = MIN_SDK
    targetSdk = TARGET_SDK

    versionCode = VERSION_CODE.toInt()
    versionName = VERSION_NAME
  }

  signingConfigs {
    getByName("debug") {
      storeFile = file("debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }

    create("release") {
      storeFile = file("release.keystore")
      storePassword = project.properties["RELEASE_STORE_PASSWORD"] as String
      keyAlias = project.properties["RELEASE_STORE_KEYALIAS"] as String
      keyPassword = project.properties["RELEASE_KEY_PASSWORD"] as String
    }
  }

  buildTypes {
    getByName("debug") {
      signingConfig = signingConfigs.getByName("debug")
    }

    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      excludes += "DebugProbesKt.bin"
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

dependencies {
  debugImplementation(compose.uiTooling)
}

// compose.desktop {
//   application {
//     mainClass = "$APP_PACKAGE.MainKt"
//
//     run {
//       jvmArgs(
//         "--add-opens=java.base/java.lang=ALL-UNNAMED",
//         "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
//         "--add-opens=java.desktop/sun.java2d=ALL-UNNAMED",
//         "--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED"
//       )
//
//       if (System.getProperty("os.name").contains("Mac")) {
//         jvmArgs(
//           "--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED",
//           "--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED"
//         )
//       }
//     }
//
//   nativeDistributions {
//     targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//
//     // Most of the JetBrains Runtime APIs are private to JetBrains, and the class sun.misc.Unsafe is used to get access to these APIs.
//     // Must be added the jdk.unsupported module to the compose dependency as in the following code to make it work.
//     modules("jdk.unsupported")
//     modules("jdk.unsupported.desktop")
//
//     packageName = APP_NAME
//     packageVersion = VERSION_NAME
//
//     description = "Compose Multiplatform App"
//     copyright = "© 2025 My Name. All rights reserved."
//     vendor = "Example vendor"
//
//     macOS {
//       bundleID = APP_PACKAGE
//       iconFile.set(project.file("icons/icon.icns"))
//
//       minimumSystemVersion = "13.5"
//       appCategory = "public.app-category.productivity"
//
//       entitlementsFile.set(File("PATH_ENT"))
//       runtimeEntitlementsFile.set(File("PATH_R_ENT"))
//
//       dmgPackageVersion = "DMG_VERSION"
//       pkgPackageVersion = "PKG_VERSION"
//       packageBuildVersion = "DMG_VERSION"
//       dmgPackageBuildVersion = "DMG_VERSION"
//       pkgPackageBuildVersion = "PKG_VERSION"
//     }
//
//     windows {
//       iconFile.set(project.file("icons/icon.ico"))
//       perUserInstall = true
//
//       msiPackageVersion = "MSI_VERSION"
//       exePackageVersion = "EXE_VERSION"
//     }
//
//     linux {
//       iconFile.set(project.file("icons/icon.png"))
//       debMaintainer = "rewhexdev@gmail.com"
//       debPackageVersion = "DEB_VERSION"
//       rpmPackageVersion = "RPM_VERSION"
//       appRelease = "1"
//       appCategory = "CATEGORY"
//       rpmLicenseType = "TYPE_OF_LICENSE"
//     }
//   }
//
//   // TODO: Windows release build crash: Failed to launch JVM
//   buildTypes.release.proguard {
//     configurationFiles.from(project.file("proguard-rules.pro"))
//   }
// }
// }
//
// afterEvaluate {
//   tasks.withType<JavaExec> {
//     jvmArgs(
//       "--add-opens=java.base/java.lang=ALL-UNNAMED",
//       "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
//       "--add-opens=java.desktop/sun.java2d=ALL-UNNAMED",
//       "--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED"
//     )
//
//     if (System.getProperty("os.name").contains("Mac")) {
//       jvmArgs(
//         "--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED",
//         "--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED"
//       )
//     }
//   }
// }

val plistBuddy = "/usr/libexec/PlistBuddy"

tasks.register<Exec>("updatePlistVersionCode") {
  commandLine(
    plistBuddy,
    "-c",
    "Set :CFBundleVersion $VERSION_CODE",
    "../iosApp/iosApp/Info.plist"
  )
}

tasks.register<Exec>("updatePlistVersionName") {
  commandLine(
    plistBuddy,
    "-c",
    "Set :CFBundleShortVersionString $VERSION_NAME",
    "../iosApp/iosApp/Info.plist"
  )
}

tasks.named("compileKotlinIosArm64") {
  dependsOn(
    "updatePlistVersionCode",
    "updatePlistVersionName"
  )
}
