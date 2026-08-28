# KAIVO — Clipboard Storage App

A minimal, offline, black-and-white clipboard manager for Android.
Kotlin + Jetpack Compose + Material 3 + Room + DataStore.

## What it does
- Manual-only: KAIVO never reads the clipboard automatically. It reads it
  once, only when you tap **Paste**.
- No background service, no clipboard-listener permission, no INTERNET
  permission — check `AndroidManifest.xml`.
- Saved items live in a local Room database (`kaivo.db`) on-device only.
- English + Persian, with automatic RTL layout in Persian via the
  standard Android/Compose locale mechanism.
- Export to `.txt` or `.json` through Android's Storage Access Framework
  (the system "Save As" file picker) — no storage permission needed.

## How to build
1. Open the `kaivo/` folder as a project in **Android Studio** (Koala or
   newer recommended).
2. Let Gradle sync — it will download the Android Gradle Plugin, Kotlin,
   Compose, and the KSP plugin (needs internet the first time).
3. Run on a device/emulator with **Run ▶**, or build a release APK via
   **Build → Build Bundle(s) / APK(s) → Build APK(s)**.

Minimum SDK is 26 (Android 8.0) so the app can use adaptive icons and
the modern per-app language APIs without needing legacy fallback
resources. Target/compile SDK is 34.

## Project layout
```
app/src/main/java/com/kaivo/app/
  data/          Room entity, DAO, database, repository, DataStore settings
  ui/theme/      Strict black & white color scheme, typography
  ui/screens/    Onboarding, Home (paste/save/search/history), Settings
  ui/components/ ClipCard (the saved-item card)
  ui/navigation/ Two-route NavHost (home ↔ settings)
  util/          Clipboard read/write, date formatting, export builders,
                 locale switching
  viewmodel/     HomeViewModel, SettingsViewModel
```

## Notes on things you may want to adjust
- The launcher icon is a simple vector "K" mark (black/white, adaptive
  icon). Swap `ic_launcher_foreground.xml` / `ic_launcher_background.xml`
  for real artwork whenever you're ready.
- Card corner radius, spacing, and font sizes live in `Type.kt` and the
  screen files if you want to tune the "premium utility" feel further.
- If you later want a Persian (Jalali) calendar for the saved-item
  timestamps instead of Gregorian, that's a follow-up — right now dates
  are shown/exported in `yyyy-MM-dd HH:mm` regardless of language.
