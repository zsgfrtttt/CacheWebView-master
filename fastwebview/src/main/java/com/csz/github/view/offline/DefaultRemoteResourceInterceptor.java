package com.csz.github.view.offline;

import android.content.Context;

import com.csz.github.view.WebResource;
import com.csz.github.view.loader.OkHttpResourceLoader;
import com.csz.github.view.loader.ResourceLoader;
import com.csz.github.view.loader.SourceRequest;

/**
 * @author caishuzhan
 */
public class DefaultRemoteResourceInterceptor implements ResourceInterceptor {

    private ResourceLoader mResourceLoader;

    DefaultRemoteResourceInterceptor(Context context) {
        mResourceLoader = new OkHttpResourceLoader(context);
    }

    @Override
    public WebResource load(Chain chain) {
        CacheRequest request = chain.getRequest();
        SourceRequest sourceRequest = new SourceRequest(request, true);
        WebResource resource = mResourceLoader.getResource(sourceRequest);
        if (resource != null) {
            return resource;
        }
        return chain.process(request);
    }
}
