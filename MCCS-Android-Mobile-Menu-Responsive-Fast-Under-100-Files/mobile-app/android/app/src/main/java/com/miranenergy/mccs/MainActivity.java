package com.miranenergy.mccs;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.getcapacitor.BridgeActivity;

/**
 * MCCS native shell with a native navigation menu.
 *
 * The menu only opens MCCS URLs. It never changes the web page's HTML, CSS,
 * sidebar, or click handlers, so every website module remains available.
 */
public class MainActivity extends BridgeActivity {
    private static final String MCCS_URL = "https://miran-commercial-control-system.vercel.app";
    private boolean navigationButtonAdded = false;

    private static final String[] MENU_LABELS = {
        "Dashboard",
        "Vendors",
        "Projects & Packages",
        "Purchase Orders",
        "Payment Milestones",
        "Invoices",
        "Payments",
        "VDRL",
        "Documents",
        "Reports",
        "Messages",
        "Administration"
    };

    private static final String[] MENU_PATHS = {
        "/dashboard",
        "/vendors",
        "/projects",
        "/purchase-orders",
        "/payment-milestones",
        "/invoices",
        "/payments",
        "/vdrl",
        "/documents",
        "/reports",
        "/messages",
        "/admin"
    };

    @Override
    public void onStart() {
        super.onStart();
        configureWebView();
        addNavigationButton();
    }

    private void configureWebView() {
        WebSettings settings = getBridge().getWebView().getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLoadWithOverviewMode(false);
        settings.setUseWideViewPort(false);
        settings.setTextZoom(100);

        CookieManager cookies = CookieManager.getInstance();
        cookies.setAcceptCookie(true);
        cookies.setAcceptThirdPartyCookies(getBridge().getWebView(), true);

        // The MCCS website is a desktop-first layout. Apply the safe
        // phone-only presentation after it has rendered. The native menu
        // remains independent from this presentation rule.
        applyMobilePresentationAfterLoad();
    }

    private void applyMobilePresentationAfterLoad() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(this::applyMobilePresentation);
        handler.postDelayed(this::applyMobilePresentation, 900);
        handler.postDelayed(this::applyMobilePresentation, 2200);
    }

    private void applyMobilePresentation() {
        String script = "(function(){"
            + "var id='mccs-native-phone-layout';"
            + "if(!document.getElementById('mccs-native-viewport')){"
            + "var m=document.createElement('meta');m.id='mccs-native-viewport';"
            + "m.name='viewport';m.content='width=device-width, initial-scale=1, maximum-scale=1, viewport-fit=cover';"
            + "document.head.appendChild(m);"
            + "}"
            + "if(document.getElementById(id))return;"
            + "var s=document.createElement('style');s.id=id;"
            + "s.textContent='@media (max-width: 768px){"
            + "html,body{width:100%!important;max-width:100%!important;min-width:0!important;overflow-x:hidden!important;}"
            + "aside{display:none!important;}"
            + "body>div,body>div>main,main{width:100%!important;max-width:100%!important;min-width:0!important;margin-left:0!important;}"
            + "main>header{height:auto!important;min-height:74px!important;padding:72px 14px 12px!important;flex-wrap:wrap!important;gap:10px!important;}"
            + "main>header>div{min-width:0!important;}"
            + "main>div{padding:14px!important;}"
            + "table{display:block!important;width:100%!important;max-width:100%!important;overflow-x:auto!important;-webkit-overflow-scrolling:touch!important;white-space:nowrap!important;}"
            + "input,select,textarea,button{font-size:16px!important;max-width:100%!important;}"
            + "[class*=grid-cols-2],[class*=grid-cols-3],[class*=grid-cols-4],[class*=grid-cols-5],[class*=grid-cols-6]{grid-template-columns:minmax(0,1fr)!important;}"
            + "[role=dialog],dialog{width:calc(100vw - 24px)!important;max-width:640px!important;margin:auto!important;}"
            + "}';document.head.appendChild(s);"
            + "})();";
        getBridge().getWebView().evaluateJavascript(script, null);
    }

    private void addNavigationButton() {
        if (navigationButtonAdded) return;
        navigationButtonAdded = true;

        Button button = new Button(this);
        button.setText("☰  Menu");
        button.setTextSize(15);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setPadding(dp(14), 0, dp(14), 0);

        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(Color.rgb(7, 74, 137));
        buttonBackground.setCornerRadius(dp(24));
        button.setBackground(buttonBackground);
        button.setElevation(dp(8));

        ViewGroup.LayoutParams size = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            dp(48)
        );
        addContentView(button, size);

        button.setTranslationX(dp(14));
        button.setTranslationY(dp(48));
        button.setOnClickListener(view -> showNavigationMenu(view));
    }

    private void showNavigationMenu(View anchor) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(dp(18), dp(16), dp(18), dp(12));

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(dp(18));
        container.setBackground(background);

        TextView title = new TextView(this);
        title.setText("MCCS Menu");
        title.setTextColor(Color.rgb(7, 27, 51));
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER_VERTICAL);
        container.addView(title, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(42)
        ));

        TextView hint = new TextView(this);
        hint.setText("Select a section to open it");
        hint.setTextColor(Color.rgb(90, 100, 115));
        hint.setTextSize(13);
        container.addView(hint, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(28)
        ));

        ListView list = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            MENU_LABELS
        );
        list.setAdapter(adapter);
        container.addView(list, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(440)
        ));

        PopupWindow popup = new PopupWindow(
            container,
            dp(320),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        );
        popup.setElevation(dp(16));
        popup.setBackgroundDrawable(background);
        popup.setOutsideTouchable(true);

        list.setOnItemClickListener((parent, view, position, id) -> {
            popup.dismiss();
            getBridge().getWebView().loadUrl(MCCS_URL + MENU_PATHS[position]);
            applyMobilePresentationAfterLoad();
        });

        popup.showAtLocation(anchor, Gravity.TOP | Gravity.START, dp(16), dp(108));
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
