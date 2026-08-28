package com.miranenergy.mccs;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Native MCCS Android container.
 * The top menu is a real Android control, not a website overlay.
 */
public class MainActivity extends Activity {
    private static final String HOME_URL = "https://miran-commercial-control-system.vercel.app/dashboard";
    private static final String BASE_URL = "https://miran-commercial-control-system.vercel.app";

    private static final String[] PAGE_NAMES = {
        "Dashboard", "Vendors", "Projects & Packages", "Purchase Orders",
        "Payment Milestones", "Invoices", "Payments", "VDRL", "Documents",
        "Reports", "Messages", "Administration"
    };

    private static final String[] PAGE_PATHS = {
        "/dashboard", "/vendors", "/projects", "/purchase-orders",
        "/payment-milestones", "/invoices", "/payments", "/vdrl", "/documents",
        "/reports", "/messages", "/admin"
    };

    private WebView webView;
    private ProgressBar loading;
    private Button menuButton;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(7, 27, 51));
        buildScreen();
        configureWebView();
        webView.loadUrl(HOME_URL);
    }

    private void buildScreen() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(12), dp(8), dp(12), dp(8));
        toolbar.setBackgroundColor(Color.rgb(7, 27, 51));

        menuButton = new Button(this);
        menuButton.setText("☰  Menu");
        menuButton.setTextColor(Color.WHITE);
        menuButton.setTextSize(15);
        menuButton.setAllCaps(false);
        menuButton.setPadding(dp(12), 0, dp(12), 0);
        menuButton.setBackground(menuBackground());
        menuButton.setOnClickListener(v -> showMenu());
        toolbar.addView(menuButton, new LinearLayout.LayoutParams(dp(112), dp(48)));

        pageTitle = new TextView(this);
        pageTitle.setText("MCCS  •  Dashboard");
        pageTitle.setTextColor(Color.WHITE);
        pageTitle.setTextSize(18);
        pageTitle.setSingleLine(true);
        pageTitle.setGravity(Gravity.CENTER_VERTICAL);
        pageTitle.setPadding(dp(14), 0, 0, 0);
        toolbar.addView(pageTitle, new LinearLayout.LayoutParams(0, dp(48), 1));

        root.addView(toolbar, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(64)
        ));

        loading = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        loading.setIndeterminate(true);
        loading.setVisibility(View.GONE);
        root.addView(loading, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(3)
        ));

        webView = new WebView(this);
        root.addView(webView, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1
        ));
        setContentView(root);
    }

    private void configureWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLoadWithOverviewMode(false);
        settings.setUseWideViewPort(false);
        settings.setTextZoom(100);
        settings.setSupportZoom(false);

        CookieManager cookies = CookieManager.getInstance();
        cookies.setAcceptCookie(true);
        cookies.setAcceptThirdPartyCookies(webView, true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                applyPhoneLayout();
                loading.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
    }

    private void showMenu() {
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(18), dp(14), dp(18), dp(10));
        GradientDrawable panelBackground = new GradientDrawable();
        panelBackground.setColor(Color.WHITE);
        panelBackground.setCornerRadius(dp(18));
        panel.setBackground(panelBackground);

        TextView heading = new TextView(this);
        heading.setText("Go to section");
        heading.setTextColor(Color.rgb(7, 27, 51));
        heading.setTextSize(22);
        panel.addView(heading, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(42)
        ));

        TextView instruction = new TextView(this);
        instruction.setText("Select a section to open it");
        instruction.setTextColor(Color.rgb(85, 98, 115));
        instruction.setTextSize(14);
        panel.addView(instruction, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(32)
        ));

        ListView list = new ListView(this);
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PAGE_NAMES));
        panel.addView(list, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(440)
        ));

        PopupWindow popup = new PopupWindow(
            panel,
            getResources().getDisplayMetrics().widthPixels - dp(24),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        );
        popup.setBackgroundDrawable(panelBackground);
        popup.setOutsideTouchable(true);
        popup.setElevation(dp(18));
        list.setOnItemClickListener((parent, view, position, id) -> {
            popup.dismiss();
            pageTitle.setText("MCCS  •  " + PAGE_NAMES[position]);
            webView.loadUrl(BASE_URL + PAGE_PATHS[position]);
        });
        popup.showAsDropDown(menuButton, dp(12) - menuButton.getLeft(), dp(4));
    }

    private void applyPhoneLayout() {
        String script = "(function(){"
            + "var viewport=document.querySelector('meta[name=viewport]');"
            + "if(!viewport){viewport=document.createElement('meta');viewport.name='viewport';document.head.appendChild(viewport);}"
            + "viewport.content='width=device-width,initial-scale=1,maximum-scale=1,viewport-fit=cover';"
            + "var id='mccs-android-phone-layout';if(document.getElementById(id))return;"
            + "var style=document.createElement('style');style.id=id;"
            + "style.textContent='@media(max-width:768px){"
            + "html,body{width:100%!important;max-width:100%!important;min-width:0!important;overflow-x:hidden!important;-webkit-text-size-adjust:100%!important;}"
            + "body>div>aside{display:none!important;}"
            + "body>div,body>div>main,main{width:100%!important;max-width:100%!important;min-width:0!important;margin:0!important;}"
            + "main>header{height:auto!important;min-height:64px!important;padding:12px 14px!important;flex-wrap:wrap!important;gap:10px!important;}"
            + "main>div{padding:14px!important;}"
            + "img,svg,canvas{max-width:100%!important;height:auto!important;}"
            + "table{display:block!important;width:100%!important;max-width:100%!important;overflow-x:auto!important;-webkit-overflow-scrolling:touch!important;white-space:nowrap!important;}"
            + "input,select,textarea,button{font-size:16px!important;max-width:100%!important;}"
            + "[class*=grid-cols-2],[class*=grid-cols-3],[class*=grid-cols-4],[class*=grid-cols-5],[class*=grid-cols-6]{grid-template-columns:minmax(0,1fr)!important;}"
            + "[role=dialog],dialog{width:calc(100vw - 24px)!important;max-width:640px!important;margin:auto!important;}"
            + "}';document.head.appendChild(style);"
            + "})();";
        webView.evaluateJavascript(script, null);
    }

    private GradientDrawable menuBackground() {
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.rgb(23, 105, 170));
        background.setCornerRadius(dp(24));
        return background;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
