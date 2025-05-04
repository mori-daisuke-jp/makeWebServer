package bbs;

import java.io.*;
import henacat.servlet.http.*;

public class PostBBS extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // リクエストの文字コードをUTF-8に設定
        request.setCharacterEncoding("UTF-8");

        // フォームからのデータを取得
        String title = request.getParameter("title");
        String handle = request.getParameter("handle");
        String message = request.getParameter("message");

        // メッセージを保存
        Message msg = new Message(title, handle, message);
        Message.messageList.add(0, msg);

        // ShowBBSにリダイレクト
        response.sendRedirect("/testbbs_jsp/showbbs.jsp");
    }
}
