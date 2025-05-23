package henacat.servletimpl;

import java.util.*;
import java.io.*;
import java.nio.charset.*;
import henacat.servlet.http.*;
import henacat.util.*;

public class HttpServletRequestImpl implements HttpServletRequest {
    private String method;
    private String characterEncoding = "ISO-8859-1";
    private Map<String, String[]> parameterMap;

    HttpServletRequestImpl(String method, Map<String, String[]> parameterMap){
        this.method = method;
        this.parameterMap = parameterMap;
    }

    @Override
    public String getMethod(){
        return this.method;
    }

    @Override
    public String getParameter(String name){
        String[] values = getParameterValues(name);
        if (values == null){
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name){
        String[] values = this.parameterMap.get(name);
        if (values == null){
            return null;
        }

        String[] decoded = new String[values.length];
        try{
            for(int i = 0; i < values.length; i++){
                decoded[i] = MyURLDecoder.decode(values[i], this.characterEncoding);
            }
        } catch(UnsupportedEncodingException ex){
            throw new AssertionError(ex);
        }
        return decoded;
    }

    @Override
    public void setCharacterEncoding(String env) 
                throws UnsupportedEncodingException {
        if(!Charset.isSupported(env)){
            throw new UnsupportedEncodingException("encdoing.." + env);
        }
        this.characterEncoding = env;
    }

}
