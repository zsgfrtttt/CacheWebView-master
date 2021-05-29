package com.csz.github.view.offline;

import android.webkit.WebResourceResponse;

/**
 * @author caishuzhan
 */
public interface OfflineServer{

    WebResourceResponse get(CacheRequest request);

    void addResourceInterceptor(ResourceInterceptor interceptor);

    void destroy();
}
