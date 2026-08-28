# MCCS Mobile Application

This Capacitor project packages the live Miran Commercial Control System website as a secure Android and iOS application. The Android shell is deliberately minimal: it does not inject a mobile menu, CSS, overlay, or URL handler. The MCCS website therefore controls navigation exactly as it does in the browser.

## What stays the same

- Same MCCS login, users, permissions, Supabase data and Google Drive documents.
- Any change deployed to the MCCS website is shown automatically in the mobile app.
- Dashboard, Vendors, Projects & Packages, Purchase Orders, Payment Milestones, Invoices, Payments, VDRL, Documents, Reports, Messages, and every other website tab remain normal website links.
- The native app ID is `com.miranenergy.mccs`.

## Build Android APK / Play Store bundle

1. Install Android Studio and Android SDK 35.
2. Run `npm install` then `npm run sync` inside this folder.
3. Open Android Studio: `npm run android`.
4. For testing: **Build → Build APK(s)**. The APK is under `android/app/build/outputs/apk/debug/`.
5. For Google Play: **Build → Generate Signed Bundle / APK → Android App Bundle**. Create and keep the keystore safely; Google Play needs a signed `.aab`.

## Build without Android Studio (GitHub)

The repository includes `.github/workflows/build-mccs-android.yml`. Push this MCCS folder to the `main` branch of your GitHub repository, then open **Actions → Build MCCS Android APK → Run workflow**. When it completes, download **MCCS-Android-APK** from the workflow page and install `app-debug.apk` on the Android phone.

## Build iPhone app

On a Mac with Xcode and an Apple Developer account, run `npm install`, `npm run sync`, then `npm run ios`. In Xcode select your Apple Team, set the signing profile, then archive for TestFlight/App Store.

## Before public-store submission

Set the final production MCCS domain in `capacitor.config.ts`, and ensure Supabase redirect URLs include that domain. The website must remain mobile responsive.
