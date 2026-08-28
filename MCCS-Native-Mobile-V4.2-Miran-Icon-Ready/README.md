# MCCS Android Mobile App

This is a fresh native Android application for the live MCCS website. It has no Capacitor, Node.js, npm, web-menu injection, or nested `mobile-app` folder.

## Mobile behaviour

- A permanent native **Menu** button is at the top of every screen.
- The Menu opens Dashboard, Vendors, Projects & Packages, Purchase Orders, Payment Milestones, Invoices, Payments, VDRL, Documents, Reports, Messages and Administration.
- The selected section opens immediately in the same app.
- The website's desktop sidebar is hidden only on phone screens, giving MCCS the full screen width.
- Website tables remain horizontally scrollable.

## Build

Upload the contents of this folder to the root of an empty GitHub repository. Open **Actions**, run **Build MCCS Android APK**, then download the `MCCS-Android-APK` artifact. The workflow also works if GitHub has one outer uploaded folder.
