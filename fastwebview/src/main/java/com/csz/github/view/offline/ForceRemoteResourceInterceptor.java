package com.csz.github.view.offline;

import android.content.Context;
import android.text.TextUtils;

import com.csz.github.view.config.CacheConfig;
import com.csz.github.view.config.MimeTypeFilter;
import com.csz.github.view.WebResource;
import com.csz.github.view.loader.OkHttpResourceLoader;
import com.csz.github.view.loader.ResourceLoader;
import com.csz.github.view.loader.SourceRequest;

/**
 * @author caishuzhan
 */
public class ForceRemoteResourceInterceptor implements Destroyable, ResourceInterceptor {

    private ResourceLoader mResourceLoader;
    private MimeTypeFilter mMimeTypeFilter;

    ForceRemoteResourceInterceptor(Context context, CacheConfig cacheConfig) {
        mResourceLoader = new OkHttpResourceLoader(context);
        mMimeTypeFilter = cacheConfig != null ? cacheConfig.getFilter() : null;
    }

    @Override
    public WebResource load(Chain chain) {
        CacheRequest request = chain.getRequest();
        String mime = request.getMime();

        boolean isFilter;
        if (TextUtils.isEmpty(mime)) {
            isFilter = isFilterHtml();
        } else {
            isFilter = mMimeTypeFilter.isFilter(mime);
        }
        SourceRequest sourceRequest = new SourceRequest(request, isFilter);
        WebResource resource = mResourceLoader.getResource(sourceRequest);
        if (resource != null) {
            return resource;
        }
        return chain.process(request);
    }

    @Override
    public void destroy() {
        if (mMimeTypeFilter != null) {
            mMimeTypeFilter.clear();
        }
    }

    private boolean isFilterHtml() {
        return mMimeTypeFilter.isFilter("text/html");
    }
}
