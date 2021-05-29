package com.csz.github.view.offline;

import com.csz.github.view.WebResource;

/**
 * @author caishuzhan
 */
public interface ResourceInterceptor{

    WebResource load(Chain chain);

}
