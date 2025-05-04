package bbs;

import java.io.*;
import henacat.servlet.http.*;

public class ShowBBS extends HttpServlet {
    // HTMLで意味を持つ文字をエスケープするユーティリティーメソッド
    private String escapeHtml(String str) {
        if (str == null) return null;
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>テスト掲示板</title></head><body>");
        out.println("<h1>テスト掲示板</h1>");

        out.println("<form method='POST' action='/testbbs/PostBBS'>");
        out.println("<p>タイトル: <input type='text' name='title'></p>" + "<br>");
        out.println("<p>ハンドルネーム: <input type='text' name='handle'></p>" + "<br>");
        out.println("<p>メッセージ: <textarea name='message' rows='5' cols='40'></textarea></p>" + "<br>");
        out.println("<input type='submit' value='投稿'>");
        out.println("</form>");
        out.println("<hr>");
        out.println("<h2>掲示板のメッセージ</h2>");
        out.println("<hr>");


        // ここに掲示板の表示ロジックを追加
        // 例: out.println("<p>" + escapeHtml(message) + "</p>");
        for (Message msg : Message.messageList) {
            out.println("<p><strong>" + escapeHtml(msg.title) + "</strong> (" +
                        escapeHtml(msg.handle) + ")<br>");
            out.println(escapeHtml(msg.message) + "<br>");
            out.println("<small>" + msg.date + "</small></p>");
            out.println("<hr>");
        }
        out.println("</body></html>");
        out.close();
    }
}
