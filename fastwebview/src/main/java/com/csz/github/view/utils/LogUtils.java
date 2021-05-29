package com.csz.github.view.utils;

import android.util.Log;

import com.csz.github.view.webview.BuildConfig;

/**
 * @author caishuzhan
 */
public class LogUtils {

    private static final String TAG = "FastWebView";
    public static boolean DEBUG = BuildConfig.DEBUG;

    public static void d(String message) {
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            Log.e(TAG, message);
        }
    }
}
