package com.csz.github.view.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author caishuzhan
 */
public class StreamUtils {

    public static byte[] streamToBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        in.close();
        return out.toByteArray();
    }
}
