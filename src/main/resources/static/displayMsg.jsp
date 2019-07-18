
<%

    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

%>
<base href="<%=basePath%>">
<frameset rows="25%,*">
     <frame src="displayHead?msgnum=<%=request.getParameter("msgnum")%>" scrolling="no">
   <frame src="isplayContent?msgnum=<%=request.getParameter("msgnum")%>" scrolling="no">
</frameset>