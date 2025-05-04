package henacat.util;

import java.util.*;
import java.io.*;

public class MyURLDecoder {
    private static int hex2int(byte b1, byte b2) {
        int digit;
        if (b1 >= 'A') {
            // 0xDFとの&演算で小文字を大文字に変換
            digit = (b1 & 0xDF) - 'A' + 10;
        } else {
            digit = (b1 - '0');
        }

        digit *= 16;
        if (b2 >= 'A') {
            digit += (b2 & 0xDF) - 'A' + 10;
        } else {
            digit += (b2 - '0');
        }

        return digit;
    }
    
    public static String decode(String src, String enc)
                        throws UnsupportedEncodingException {
            byte[] srcBytes = src.getBytes("ISO-8859-1");
            // 変換後のほうが長くなることはないので、srcBytesの
            // 長さの配列をいったん確保する。
            byte[] dstBytes = new byte[srcBytes.length];

            int dstIdx = 0;
            for (int srcIdx = 0; srcIdx < srcBytes.length; srcIdx++) {
                if (srcBytes[srcIdx] == '%') {
                    dstBytes[dstIdx] = (byte)hex2int(srcBytes[srcIdx+1],
                                                     srcBytes[srcIdx+2]);
                    srcIdx += 2;
                } else {
                    dstBytes[dstIdx] = srcBytes[srcIdx];
                }
                dstIdx++;
            }

            byte[] dstBytes2 = Arrays.copyOf(dstBytes, dstIdx);

            return new String(dstBytes2, enc);
           
    }
}
