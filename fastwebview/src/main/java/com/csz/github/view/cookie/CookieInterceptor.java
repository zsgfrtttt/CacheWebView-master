package com.csz.github.view.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * @author caishuzhan
 */
public interface CookieInterceptor {

    List<Cookie> newCookies(HttpUrl url, List<Cookie> originCookies);

}
