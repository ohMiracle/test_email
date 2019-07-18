<%@ page import="javax.mail.*,com.newroad.*"
         contentType="text/html;charset=utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
    Folder folder = (Folder) session.getAttribute("folder");
    Message [] messages = folder.getMessages();
    String from = "";
    String subject = "";
    for(int i=0;i<messages.length;i++)
    {
        try
        {
            from = messages[i].getFrom()[0].toString();
            subject = messages[i].getSubject();
            out.print(i + 1);
%>
发件人地址：<%=from %>  邮件主题：<%=subject %>
<a href="displayMsg.jsp?msgnum=<%=i+1%>">查看邮件</a><br/>
<%
        }
        catch(Exception e){}
    }
%>