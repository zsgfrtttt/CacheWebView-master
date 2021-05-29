package com.csz.github.view.utils;

import androidx.collection.ArraySet;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;

/**
 * @author caishuzhan
 */
public class HeaderUtils {

    public static Map<String, String> generateHeadersMap(Headers headers) {
        Map<String, String> headersMap = new HashMap<>();
        for (String key : headers.names()) {
            StringBuilder values = new StringBuilder();
            for (String value : listToSet(headers.values(key))) {
                if (!TextUtils.isEmpty(values)) {
                    values.append(" ");
                }
                values.append(value);
            }
            headersMap.put(key, values.toString().trim());
            for (Map.Entry<String, String> en : headersMap.entrySet()) {
          //      Log.i("csz","hearer   " + en.getKey()+"    " +en.getValue() );
            }
        }
        return headersMap;
    }

    private static Set<String> listToSet(List<String> origin) {
        Set<String> target = new ArraySet<>(origin.size());
        target.addAll(origin);
        return target;
    }

    public static Map<String, String> generateHeadersMap(Map<String, List<String>> headers) {
        Map<String, String> headersMap = new HashMap<>();
        int index = 0;
        for (String key : headers.keySet()) {
            StringBuilder values = new StringBuilder();
            List<String> list = headers.get(key);
            for (String value : list) {
                values.append(value);
                if (index++ > 0) {
                    values.append(",");
                }
            }
            index = 0;
            headersMap.put(key, values.toString());
        }
        return headersMap;
    }
}
