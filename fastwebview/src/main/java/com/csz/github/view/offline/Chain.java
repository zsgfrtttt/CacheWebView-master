package com.csz.github.view.offline;

import com.csz.github.view.WebResource;

import java.util.List;

/**
 * @author caishuzhan
 */
public class Chain {

    private List<ResourceInterceptor> mInterceptors;
    private int mIndex = -1;
    private CacheRequest mRequest;

    Chain(List<ResourceInterceptor> interceptors) {
        mInterceptors = interceptors;
    }

    public WebResource process(CacheRequest request) {
        if (++mIndex >= mInterceptors.size()) {
            return null;
        }
        mRequest = request;
        ResourceInterceptor interceptor = mInterceptors.get(mIndex);
        return interceptor.load(this);  //递归遍历资源拦截器
    }

    public CacheRequest getRequest() {
        return mRequest;
    }
}
