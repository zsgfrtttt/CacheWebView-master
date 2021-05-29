package com.csz.github.webview.sample;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.csz.github.view.utils.lru.Util;
import com.google.gson.Gson;
import com.csz.github.view.FastWebView;
import com.csz.github.view.FastWebViewPool;
import com.csz.github.view.WebResource;
import com.csz.github.view.config.CacheConfig;
import com.csz.github.view.config.DefaultMimeTypeFilter;
import com.csz.github.view.config.FastCacheMode;
import com.csz.github.view.cookie.CookieInterceptor;
import com.csz.github.view.cookie.FastCookieManager;
import com.csz.github.view.offline.Chain;
import com.csz.github.view.offline.ResourceInterceptor;
import com.csz.github.view.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "FastWebView";
    private FastWebView fastWebView;
    private long initStartTime;
    private long startTime;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FastWebView.setDebug(true);
        LogUtils.d("------------- start once load -------------");
        startTime = SystemClock.uptimeMillis();
        initStartTime = SystemClock.uptimeMillis();
        if (MainActivity.sUseWebViewPool) {
            fastWebView = FastWebViewPool.acquire(this);
        } else {
            LogUtils.d("create new webview instance.");
            fastWebView = new FastWebView(this);
        }

        fastWebView.setWebChromeClient(new MonitorWebChromeClient());
        fastWebView.setWebViewClient(new MonitorWebViewClient());
        setContentView(fastWebView);
        fastWebView.setFocusable(true);
        fastWebView.setFocusableInTouchMode(true);
        WebSettings webSettings = fastWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setBlockNetworkImage(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36");

        // 设置正确的cache mode以支持离线加载
        int cacheMode = NetworkUtils.isAvailable(this) ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK;
        webSettings.setCacheMode(cacheMode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(fastWebView, true);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        CacheConfig config = new CacheConfig.Builder(this).setCacheDir(getExternalCacheDir() + File.separator + "custom").setExtensionFilter(new DefaultMimeTypeFilter.WebMimeTypeFilter()).build();
        fastWebView.setCacheMode(FastCacheMode.FORCE, config);
        fastWebView.addResourceInterceptor(new ResourceInterceptor() {
            @Override
            public WebResource load(Chain chain) {
                return chain.process(chain.getRequest());
            }
        });
        fastWebView.addJavascriptInterface(this, "android");
        Map<String, String> headers = new HashMap<>();
        headers.put("custom", "test");

        //String url = "https://github.com/Ryan-Shz";
        String url = "https://www.bilibili.com";

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除旧的[可以省略]
        cookieManager.setCookie(url, "custom=12345678910;");
        CookieSyncManager.getInstance().sync();

        FastCookieManager fastCookieManager = fastWebView.getFastCookieManager();
        fastCookieManager.addRequestCookieInterceptor(new CookieInterceptor() {
            @Override
            public List<Cookie> newCookies(HttpUrl url, List<Cookie> originCookies) {
                for (Cookie cookie : originCookies) {
                    Log.v(TAG, "request cookies: " + cookie.toString());
                }
                return originCookies;
            }
        });
        fastCookieManager.addResponseCookieInterceptor(new CookieInterceptor() {
            @Override
            public List<Cookie> newCookies(HttpUrl url, List<Cookie> originCookies) {
                for (Cookie cookie : originCookies) {
                    Log.v(TAG, "response cookies: " + cookie.toString());
                }
                return originCookies;
            }
        });

        fastWebView.loadUrl(url, headers);
    }

    @JavascriptInterface
    public void sendResource(String timing) {
        Performance performance = new Gson().fromJson(timing, Performance.class);
        Log.v(TAG, "request cost time: " + (performance.getResponseEnd() - performance.getRequestStart()) + "ms");
        Log.v(TAG, "dom build time: " + (performance.getDomComplete() - performance.getDomInteractive()) + "ms.");
        Log.v(TAG, "dom ready time: " + (performance.getDomContentLoadedEventEnd() - performance.getNavigationStart()) + "ms.");
        Log.v(TAG, "load time: " + (performance.getLoadEventEnd() - performance.getNavigationStart()) + "ms.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fastWebView != null) {
            if (MainActivity.sUseWebViewPool) {
                FastWebViewPool.release(fastWebView);
            } else {
                fastWebView.destroy();
            }
        }
       /* try {
            //清空本地缓存
           clearDisk();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void clearDisk() throws IOException {
        File dir = new File(getExternalCacheDir() + File.separator + "custom");
        if (dir.exists()) {
            Util.deleteContents(dir);
        }
    }

    public class MonitorWebViewClient extends WebViewClient {

        private boolean first = true;

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.getSettings().setBlockNetworkImage(false);
            view.loadUrl("javascript:android.sendResource(JSON.stringify(window.performance.timing))");
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (first) {
                LogUtils.d("init cost time: " + (SystemClock.uptimeMillis() - initStartTime));
                first = false;
            }
            return super.shouldInterceptRequest(view, request);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fastWebView.canGoBack()) {
                fastWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MonitorWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.d(TAG, "white screen time: " + (SystemClock.uptimeMillis() - startTime));
        }
    }
}