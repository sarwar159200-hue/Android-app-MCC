package com.miranenergy.mccs;

import com.getcapacitor.BridgeActivity;

/**
 * MCCS mobile presentation layer.
 *
 * The business system remains the live MCCS website, so every module, user
 * permission, document, calculation and update remains exactly the same.
 * This class adds a phone-first presentation shell without removing website
 * functionality.
 */
public class MainActivity extends BridgeActivity {
    @Override
    public void onResume() {
        super.onResume();
        getBridge().getWebView().postDelayed(this::applyMobilePresentation, 900);
    }

    private void applyMobilePresentation() {
        String script = "(function(){"
                + "if(!document.getElementById('mccs-mobile-presentation')) {"
                + "var s=document.createElement('style');s.id='mccs-mobile-presentation';"
                + "s.textContent=`"
                + "@media(max-width:768px){"
                + "html{font-size:15px!important;-webkit-text-size-adjust:100%;}"
                + "body{overflow-x:hidden!important;padding-bottom:76px!important;}"
                + "main{width:100%!important;max-width:100%!important;margin-left:0!important;padding:14px!important;}"
                + "aside{position:fixed!important;z-index:9998!important;top:0!important;left:0!important;height:100dvh!important;width:min(82vw,330px)!important;max-width:330px!important;overflow-y:auto!important;transform:translateX(-110%)!important;transition:transform .2s ease!important;box-shadow:0 12px 36px rgba(0,0,0,.35)!important;}"
                + "body.mccs-menu-open aside{transform:translateX(0)!important;}"
                + "body.mccs-menu-open:after{content:''!important;position:fixed!important;inset:0!important;background:rgba(0,0,0,.42)!important;z-index:9997!important;}"
                + "table{display:block!important;max-width:100%!important;overflow-x:auto!important;-webkit-overflow-scrolling:touch!important;white-space:nowrap!important;}"
                + "input,select,textarea,button{font-size:16px!important;min-height:42px!important;}"
                + "[role=dialog],dialog{width:calc(100vw - 24px)!important;max-width:640px!important;max-height:88dvh!important;overflow-y:auto!important;margin:auto!important;}"
                + "[class*=grid]{min-width:0!important;}"
                + "[class*=grid-cols-2],[class*=grid-cols-3],[class*=grid-cols-4],[class*=grid-cols-5],[class*=grid-cols-6]{grid-template-columns:minmax(0,1fr)!important;}"
                + "[class*=overflow-x-auto]{-webkit-overflow-scrolling:touch!important;}"
                + "}`;document.head.appendChild(s);"
                + "var b=document.createElement('button');b.id='mccs-mobile-menu-button';b.type='button';b.setAttribute('aria-label','Open MCCS menu');b.innerHTML='☰ <span>Menu</span>';"
                + "b.style.cssText='position:fixed;left:12px;bottom:14px;z-index:9999;border:0;border-radius:24px;background:#0b3b69;color:#fff;padding:11px 16px;font-size:15px;font-weight:700;box-shadow:0 5px 18px rgba(0,0,0,.28);min-height:44px';"
                + "b.onclick=function(){document.body.classList.toggle('mccs-menu-open')};document.body.appendChild(b);"
                + "document.addEventListener('click',function(e){if(document.body.classList.contains('mccs-menu-open')&&!e.target.closest('aside')&&!e.target.closest('#mccs-mobile-menu-button'))document.body.classList.remove('mccs-menu-open')});"
                + "}"
                + "})();";
        getBridge().getWebView().evaluateJavascript(script, null);
    }
}
