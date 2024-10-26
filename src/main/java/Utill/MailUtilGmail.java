package Utill;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class MailUtilGmail {
    public static void sendMail(String to, String from,
                                String subject, String body, boolean bodyIsHTML)
            throws MessagingException {
        // 1- get a mail session
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.port", "465");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.quitwait", "false");

        // Khởi tạo session
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        // 2- Tạo một message
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html");
        } else {
            message.setText(body);
        }

        // 3- Đặt địa chỉ gửi và nhận
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4- Gửi email
        Transport transport = session.getTransport();
        try {
            // Sử dụng địa chỉ email và mật khẩu của bạn để kết nối
            transport.connect("nguyensanghuy2@gmail.com", "jqwu zckj mtcu duvc"); // Thay thế bằng email và mật khẩu thực của bạn
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            transport.close();
        }
    }
}
