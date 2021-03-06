package com.geansea.gszip.util;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.zip.CRC32;

public class GsZipUtil {
    public static void check(boolean state, String msg) throws IOException {
        if (!state) {
            throw new IOException(msg);
        }
    }

    public static int getStreamCRC(@NonNull InputStream stream) throws IOException {
        CRC32 crc32 = new CRC32();
        stream.reset();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = stream.read(buffer)) > 0) {
            crc32.update(buffer, 0, count);
        }
        stream.reset();
        return (int) crc32.getValue();
    }

    public static int getStreamLength(@NonNull InputStream stream) throws IOException {
        int length = 0;
        stream.reset();
        int count;
        while ((count = (int) stream.skip(1024)) > 0) {
            length += count;
        }
        stream.reset();
        return length;
    }

    public static @NonNull String getCanonicalPath(@NonNull String path) {
        Stack<String> parts = new Stack<>();
        for (String part : path.split("/|\\\\")) {
            if (part.isEmpty()) {
                continue;
            }
            if (part.equals(".")) {
                continue;
            }
            if (part.equals("..")) {
                if (!parts.isEmpty() && !parts.peek().equals("..")) {
                    parts.pop();
                    continue;
                }
            }
            parts.push(part);
        }
        if (parts.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(path.length());
        for (String part : parts) {
            builder.append("/");
            builder.append(part);
        }
        return builder.substring(1);
    }

    public static @NonNull String getParentPath(@NonNull String path) {
        int parentPos = path.indexOf("/");
        if (parentPos < 0) {
            return "";
        }
        return path.substring(0, parentPos);
    }
}
