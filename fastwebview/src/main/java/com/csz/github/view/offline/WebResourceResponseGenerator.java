package com.csz.github.view.offline;

import android.webkit.WebResourceResponse;

import com.csz.github.view.WebResource;

/**
 * @author caishuzhan
 */
public interface WebResourceResponseGenerator {

    WebResourceResponse generate(WebResource resource, String urlMime);

}
