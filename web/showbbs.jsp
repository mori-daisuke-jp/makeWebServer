<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="chapter3.Message" %>
<%!
// HTMLで意味を持つ文字をエスケープするユーティリティーメソッド
    private String escapeHtml(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
%>
<html>
    <head>
        <title>テスト掲示板</title>
    </head>
    <body>
    <h1>テスト掲示板</h1>
            <form action="/testbbs_jsp/PostBBS" method="post">
                タイトル：<input type="text" name="title" size="60"><br>
                ハンドルネーム：<input type="text" name="handle"><br>
                <textarea name="message" rows="4" cols="60"></textarea><br>
                <input type="submit" value="投稿">
            </form>
            <hr>
            <h2>掲示板のメッセージ</h2>
            <%
                for (Message message : Message.messageList) {
            %>
            <h3>『<%= escapeHtml(message.title) %>』&nbsp;&nbsp;</h3>
            <p><%= escapeHtml(message.handle) %>さん&nbsp;&nbsp;</p>
            <p><%= escapeHtml(message.date.toString()) %></p>
            <p><%= escapeHtml(message.message).replace("\r\n", "<br>") %></p>
            <hr>
            <%
                }
            %>
        </body>
</html>