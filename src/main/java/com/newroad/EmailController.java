package com.newroad;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;

@RestController
public class EmailController {
    @GetMapping("/displayHead")
    public void displayHead(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setContentType("text/html;charset=gb2312");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        int msgnum = Integer.parseInt(request.getParameter("msgnum"));
        Folder folder = (Folder) session.getAttribute("folder");

        try {
            Message msg = folder.getMessage(msgnum);
            String from = msg.getFrom()[0].toString();
            String subject = msg.getSubject();
            String sendDate = DateFormat.getInstance().format(msg.getSentDate());

            out.println("邮件主题：" + subject + "<br/>");
            out.println("发件人:" + from + "<br/>");
            out.println("发送日期：" + sendDate + "<br/><br/>");

            System.out.println("contentType：" + msg.getContentType());

            // 如果该邮件是组合型"multipart/*"则可能包含附件等
            if (msg.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) msg.getContent();

                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart bp = mp.getBodyPart(i);

                    // 如果该BodyPart对象包含附件，则应该解析出来
                    if (bp.getDisposition() != null) {
                        String filename = bp.getFileName();
                        System.out.println("filename：" + filename);

                        if (filename.startsWith("=?")) {
                            // 把文件名编码成符合RFC822规范
                            filename = MimeUtility.decodeText(filename);
                        }

                        // 生成打开附件的超链接
                        out.print("附件：");
                        out.print("<a href=HandleAttach?msgnum=" + msgnum + "&&bodynum=" + i + "&&filename=" + filename
                                + ">" + filename + "</a><br/>");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/displayContent")
    public void displayContent(HttpServletRequest request, HttpServletResponse response)throws Exception{
        ServletOutputStream sos = response.getOutputStream();
        HttpSession session = request.getSession();
        int msgnum = Integer.parseInt(request.getParameter("msgnum"));
        Folder folder = (Folder) session.getAttribute("folder");

        try {
            Message msg = folder.getMessage(msgnum);
            // 邮件类型不是mixed时，表示邮件中不包含附件，直接输出邮件内容
            if (!msg.isMimeType("multipart/mixed")) {
                response.setContentType("message/rfc822");
                msg.writeTo(sos);
            } else {
                // 查找并输出邮件中的邮件正文
                Multipart mp = (Multipart) msg.getContent();
                int bodynum = mp.getCount();
                for (int i = 0; i < bodynum; i++) {
                    BodyPart bp = mp.getBodyPart(i);
                    /*
                     * MIME消息头中不包含disposition字段， 并且MIME消息类型不为mixed时，
                     * 表示当前获得的MIME消息为邮件正文
                     */
                    if (!bp.isMimeType("multipart/mixed") && bp.getDisposition() == null) {
                        response.setContentType("message/rfc822");
                        bp.writeTo(sos);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/handleAttach")
    public void handleAttach(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();

        int msgnum = Integer.parseInt(request.getParameter("msgnum"));
        int bodynum = Integer.parseInt(request.getParameter("bodynum"));
        String filename = request.getParameter("filename");
        Folder folder = (Folder) session.getAttribute("folder");

        try {
            Message msg = folder.getMessage(msgnum);

            // 将消息头类型设置为附件类型
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            Multipart multi = (Multipart) msg.getContent();
            BodyPart bodyPart = multi.getBodyPart(bodynum);

            InputStream is = bodyPart.getInputStream();
            int c = 0;
            while ((c = is.read()) != -1) {
                out.write(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/user.do")
    public ModelAndView login(String username, String password, String host, HttpSession session)throws Exception{
        Folder folder = POP3Help.getFolder(host,username,password);
        session.setAttribute("folder",folder);
        ModelAndView mv = new ModelAndView("login.jsp");
        return mv;
    }
}
