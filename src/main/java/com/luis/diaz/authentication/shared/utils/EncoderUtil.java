package com.luis.diaz.authentication.shared.utils;

import java.util.Base64;

public class EncoderUtil {
    private EncoderUtil() {
    }

    /**
     * It decodes a string that is encoded in base64.
     *
     * @param stringBase64 The string to be decoded.
     * @return A string
     */
    public static String parseBase64ToString(String stringBase64) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(stringBase64.replace("Basic ", ""));
        return new String(decodedBytes).replace(":", "");
    }
}

