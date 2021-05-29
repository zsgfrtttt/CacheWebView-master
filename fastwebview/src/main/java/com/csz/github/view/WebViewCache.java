package com.csz.github.view;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import com.csz.github.view.offline.Destroyable;

/**
 * @author caishuzhan
 */
public interface WebViewCache extends FastOpenApi, Destroyable {

    WebResourceResponse getResource(WebResourceRequest request, int webViewCacheMode, String userAgent);

}
