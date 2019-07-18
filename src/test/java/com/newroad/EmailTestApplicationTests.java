package com.newroad;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailTestApplicationTests {

	@Test
	public void contextLoads() throws Exception{
	/*	// 准备连接服务器的会话信息
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imap");
		props.setProperty("mail.imap.host", "imap.163.com");
		props.setProperty("mail.imap.port", "143");

		// 创建Session实例对象
		Session session = Session.getInstance(props);

		// 创建IMAP协议的Store对象
		Store store = session.getStore("imap");

		// 连接邮件服务器
		store.connect("zhengchangzccc@163.com", "haohaowanba3141");

		// 获得收件箱
		Folder folder = store.getFolder("INBOX");
		// 以读写模式打开收件箱
		folder.open(Folder.READ_WRITE);

		// 获得收件箱的邮件列表
		Message[] messages = folder.getMessages();

		// 打印不同状态的邮件数量
		System.out.println("收件箱中共" + messages.length + "封邮件!");
		System.out.println("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!");
		System.out.println("收件箱中共" + folder.getNewMessageCount() + "封新邮件!");
		System.out.println("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!");

		System.out.println("------------------------开始解析邮件----------------------------------");


		int total = folder.getMessageCount();
		System.out.println("-----------------您的邮箱共有邮件：" + total + " 封--------------");
		// 得到收件箱文件夹信息，获取邮件列表
		Message[] msgs = folder.getMessages();
		System.out.println("\t收件箱的总邮件数：" + msgs.length);
		for (int i = 0; i < total; i++) {
			Message a = msgs[i];
			//   获取邮箱邮件名字及时间

			System.out.println(a.getReplyTo());

			System.out.println("==============");
//                System.out.println(a.getSubject() + "   接收时间：" + a.getReceivedDate().toLocaleString()+"  contentType()" +a.getContentType());
		}
		System.out.println("\t未读邮件数：" + folder.getUnreadMessageCount());
		System.out.println("\t新邮件数：" + folder.getNewMessageCount());
		System.out.println("----------------End------------------");



		// 关闭资源
		folder.close(false);
		store.close();
*/



		// 定义连接POP3服务器的属性信息
		        String pop3Server = "pop.163.com";
		         String protocol = "pop3";
		         String username = "zhengchangzccc@163.com";
		         String password = "haohaowanba3141"; // QQ邮箱的SMTP的授权码，什么是授权码，它又是如何设置？

		         Properties props = new Properties();
		         props.setProperty("mail.transport.protocol", protocol); // 使用的协议（JavaMail规范要求）
		         props.setProperty("mail.smtp.host", pop3Server); // 发件人的邮箱的 SMTP服务器地址

		         // 获取连接
		         Session session = Session.getDefaultInstance(props);
		         session.setDebug(false);

		         // 获取Store对象
		         Store store = session.getStore(protocol);
		         store.connect(pop3Server, username, password); // POP3服务器的登陆认证

		         // 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
		         Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
		         folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限

		         Message[] messages = folder.getMessages();// 得到邮箱帐户中的所有邮件

		         for (Message message : messages) {
		             String subject = message.getSubject();// 获得邮件主题
		             Address from = (Address) message.getFrom()[0];// 获得发送者地址
		             System.out.println("邮件的主题为: " + subject + "\t发件人地址为: " + from);
		             System.out.println("邮件的内容为：");
		             message.writeTo(System.out);// 输出邮件内容到控制台
		         }

		         folder.close(false);// 关闭邮件夹对象
		         store.close(); // 关闭连接对象
}

}
