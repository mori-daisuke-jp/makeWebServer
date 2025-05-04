package chapter2;

import java.io.*;
import java.util.*;
import java.text.*;

public class Util {
    static String readLine(InputStream input) throws Exception {
        int ch;
        StringBuilder sb = new StringBuilder();
        while ((ch = input.read()) != -1) {
            if (ch == '\r') {
                continue;
            }
            if (ch == '\n') {
                break;
            }
            sb.append((char) ch);
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    static void writeLine(OutputStream output, String line) throws Exception {
        for (char ch : line.toCharArray()) {
            output.write(ch);
        }
        output.write('\r');
        output.write('\n');
    }

    static String getDateStringUtc() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        df.setTimeZone(cal.getTimeZone());
        return df.format(cal.getTime()) + " GMT";
    }

    static final HashMap<String, String> contentTypeMap = new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
        {
            put("html", "text/html");
            put("htm", "text/html");
            put("txt", "text/plain");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("png", "image/png");
            put("gif", "image/gif");
            put("css", "text/css");
            put("js", "application/javascript");
        }
    };
    
    static String getContentType(String ext) {
        String ret = contentTypeMap.get(ext.toLowerCase());
        if (ret == null) {
            return "application/octet-stream";
        } else {
            return ret;
        }
    }
}
