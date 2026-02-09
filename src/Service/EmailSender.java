package Service;

import static Util.utilities.*;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import view.viewAdmin;
import static Util.colors.*;
import java.util.Properties;

public class EmailSender {

    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, formPass);
            }
        });
    }

    public static void sendEmail(String toEmail, String subject, String body, boolean notify) {
        try {
            Session session = createSession();
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(body);

            Transport.send(msg);

            if (notify) {
                viewAdmin.emailSentSuccessfully();
            }
        } catch (MessagingException e) {
            viewAdmin.updateStatus(RED, "Email sending failed: " + e.getMessage(), RESET);
            e.printStackTrace();
        }
    }
}
