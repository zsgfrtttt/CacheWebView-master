package com.csz.github.view;

import com.csz.github.view.config.CacheConfig;
import com.csz.github.view.config.FastCacheMode;
import com.csz.github.view.offline.ResourceInterceptor;

/**
 * @author caishuzhan
 */
public interface FastOpenApi {

    void setCacheMode(FastCacheMode mode, CacheConfig cacheConfig);

    void addResourceInterceptor(ResourceInterceptor interceptor);
}
