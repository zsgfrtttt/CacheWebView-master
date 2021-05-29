package com.csz.github.view;

import android.content.Context;
import android.content.MutableContextWrapper;
import androidx.core.util.Pools;

import com.csz.github.view.utils.LogUtils;

/**
 * A simple webview instance pool.
 * Reduce webview initialization time about 100ms.
 * my test env: vivo-x23, android api: 8.1
 * <p>
 * @author caishuzhan
 */
public class FastWebViewPool {

    private static final int MAX_POOL_SIZE = 2;
    private static final Pools.Pool<FastWebView> sPool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);

    public static void prepare(Context context) {
        release(acquire(context.getApplicationContext()));
    }

    public static FastWebView acquire(Context context) {
        FastWebView webView = sPool.acquire();
        if (webView == null) {
            MutableContextWrapper wrapper = new MutableContextWrapper(context);
            webView = new FastWebView(wrapper);
            LogUtils.d("create new webview instance.");
        } else {
            MutableContextWrapper wrapper = (MutableContextWrapper) webView.getContext();
            wrapper.setBaseContext(context);
            LogUtils.d("obtain webview instance from pool.");
        }
        return webView;
    }

    public static void release(FastWebView webView) {
        if (webView == null) {
            return;
        }
        webView.release();
        MutableContextWrapper wrapper = (MutableContextWrapper) webView.getContext();
        wrapper.setBaseContext(wrapper.getApplicationContext());
        sPool.release(webView);
        LogUtils.d("release webview instance to pool.");
    }
}
