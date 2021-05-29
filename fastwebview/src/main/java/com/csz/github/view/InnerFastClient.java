package com.csz.github.view;

import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.csz.github.view.config.CacheConfig;
import com.csz.github.view.config.FastCacheMode;
import com.csz.github.view.offline.Destroyable;
import com.csz.github.view.offline.ResourceInterceptor;

/**
 * @author caishuzhan
 */
class InnerFastClient extends WebViewClient implements FastOpenApi, Destroyable {

    private static final String SCHEME_HTTP = "http";
    private static final String SCHEME_HTTPS = "https";
    private static final String METHOD_GET = "GET";
    private WebViewClient mDelegate;
    private WebViewCache mWebViewCache;
    private final int mWebViewCacheMode;
    private final String mUserAgent;
    private FastWebView mOwner;

    InnerFastClient(FastWebView owner) {
        mOwner = owner;
        WebSettings settings = owner.getSettings();
        mWebViewCacheMode = settings.getCacheMode();
        mUserAgent = settings.getUserAgentString();
        mWebViewCache = new WebViewCacheImpl(owner.getContext());
    }

    void updateProxyClient(WebViewClient webViewClient) {
        mDelegate = webViewClient;
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        if (mDelegate != null) {
            mDelegate.onTooManyRedirects(view, cancelMsg, continueMsg);
            return;
        }
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        if (mDelegate != null) {
            mDelegate.onReceivedHttpError(view, request, errorResponse);
            return;
        }
        super.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        if (mDelegate != null) {
            mDelegate.onFormResubmission(view, dontResend, resend);
            return;
        }
        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        if (mDelegate != null) {
            mDelegate.doUpdateVisitedHistory(view, url, isReload);
            return;
        }
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (mDelegate != null) {
            mDelegate.onReceivedSslError(view, handler, error);
            return;
        }
        super.onReceivedSslError(view, handler, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        if (mDelegate != null) {
            mDelegate.onReceivedClientCertRequest(view, request);
            return;
        }
        super.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        if (mDelegate != null) {
            mDelegate.onReceivedHttpAuthRequest(view, handler, host, realm);
            return;
        }
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if (mDelegate != null) {
            return mDelegate.shouldOverrideKeyEvent(view, event);
        }
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        if (mDelegate != null) {
            mDelegate.onUnhandledKeyEvent(view, event);
            return;
        }
        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        if (mDelegate != null) {
            mDelegate.onScaleChanged(view, oldScale, newScale);
            return;
        }
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        if (mDelegate != null) {
            mDelegate.onReceivedLoginRequest(view, realm, account, args);
            return;
        }
        super.onReceivedLoginRequest(view, realm, account, args);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        if (mDelegate != null) {
            return mDelegate.onRenderProcessGone(view, detail);
        }
        return super.onRenderProcessGone(view, detail);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (mDelegate != null) {
            mDelegate.onReceivedError(view, errorCode, description, failingUrl);
            return;
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (mDelegate != null) {
            mDelegate.onReceivedError(view, request, error);
            return;
        }
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (mDelegate != null) {
            mDelegate.onPageStarted(view, url, favicon);
            return;
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (mOwner.isRecycled() && !url.equals("about:blank")) {
            mOwner.setRecycled(false);
            mOwner.clearHistory();
        }
        if (mDelegate != null) {
            mDelegate.onPageFinished(view, url);
            return;
        }
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        if (mDelegate != null) {
            mDelegate.onLoadResource(view, url);
            return;
        }
        super.onLoadResource(view, url);
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        if (mDelegate != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDelegate.onPageCommitVisible(view, url);
            return;
        }
        super.onPageCommitVisible(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (mDelegate != null) {
            return mDelegate.shouldOverrideUrlLoading(view, url);
        }
        view.loadUrl(url);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (mDelegate != null) {
            return mDelegate.shouldOverrideUrlLoading(view, request);
        }
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return onIntercept(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        // don't intercept request below android 5.0
        // bc we can not get request method, request body and request headers
        // delegate intercept first
        return mDelegate != null ? mDelegate.shouldInterceptRequest(view, url) : null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private WebResourceResponse onIntercept(WebView view, WebResourceRequest request) {
        if (mDelegate != null) {
            WebResourceResponse response = mDelegate.shouldInterceptRequest(view, request);
            if (response != null) {
                return response;
            }
        }
        return loadFromWebViewCache(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private WebResourceResponse loadFromWebViewCache(WebResourceRequest request) {
        String scheme = request.getUrl().getScheme().trim();
        String method = request.getMethod().trim();
        if ((TextUtils.equals(SCHEME_HTTP, scheme)
                || TextUtils.equals(SCHEME_HTTPS, scheme))
                && method.equalsIgnoreCase(METHOD_GET)) {
            return mWebViewCache.getResource(request, mWebViewCacheMode, mUserAgent);
        }
        return null;
    }

    @Override
    public void setCacheMode(FastCacheMode mode, CacheConfig cacheConfig) {
        if (mWebViewCache != null) {
            mWebViewCache.setCacheMode(mode, cacheConfig);
        }
    }

    @Override
    public void addResourceInterceptor(ResourceInterceptor interceptor) {
        if (mWebViewCache != null) {
            mWebViewCache.addResourceInterceptor(interceptor);
        }
    }

    @Override
    public void destroy() {
        if (mWebViewCache != null) {
            mWebViewCache.destroy();
        }
    }
}
