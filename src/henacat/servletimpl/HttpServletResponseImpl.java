package henacat.servletimpl;
import java.io.*;
import henacat.servlet.http.*;

public class HttpServletResponseImpl implements HttpServletResponse {
    String contentType = "application/octet-stream";
    private String characterEncoding = "ISO-8859-1";
    private OutputStream outputstream;
    PrintWriter printWriter;
    int status;
    String redirectLocation;

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
        String[] tmp = contentType.split(" *; *");
        if (tmp.length > 1){
            String[] keyValue = tmp[1].split("=");
            if (keyValue.length == 2 && keyValue[0].equals("charset")){
                this.characterEncoding = keyValue[1];
            }
        }
    }

    @Override
    public void setCharacterEncoding(String charset){
        this.characterEncoding = charset;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        this.printWriter = new PrintWriter(new OutputStreamWriter(this.outputstream, this.characterEncoding));
        return this.printWriter;
    }

    @Override
    public void sendRedirect(String location){
        this.redirectLocation = location;
        setStatus(SC_FOUND);
    }

    @Override
    public void setStatus(int status){
        this.status = status;
    }

    HttpServletResponseImpl(OutputStream outputstream){
        this.outputstream = outputstream;
        this.status = SC_OK;
    }

}
