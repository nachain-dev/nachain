package org.nachain.libs.email;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


@Slf4j
public class SendEmail extends Thread {

    private Email email;

    @SuppressWarnings("unused")
    private SendEmail() {
    }

    public SendEmail(Email email) {
        this.email = email;
    }

    @Override
    public void run() {
        try {
            send();
        } catch (Exception e) {
            log.error("Error sending Email using thread:" + email.getTo(), e);
        }
    }

    public void send() throws Exception {
        Properties props = new Properties();


        props.put("mail.smtp.host", email.getSmtpServer());


        props.put("mail.smtp.auth", "true");

        SmtpAuthenticator sa = new SmtpAuthenticator();
        sa.setUsername(email.getFrom());
        sa.setPassword(email.getPassword());


        Session session = Session.getInstance(props, sa);


        session.setDebug(false);


        Message message = new MimeMessage(session);
        Address from = new InternetAddress(email.getSender(), email.getSenderLabel());
        message.setFrom(from);


        Address to = new InternetAddress(email.getTo(), email.getTo());


        message.setRecipient(Message.RecipientType.TO, to);

        message.setSubject(email.getSubject());

        message.setContent(email.getContent(), "text/html;charset=utf-8");

        message.setSentDate(new Date());

        message.saveChanges();


        Transport transport = session.getTransport("smtp");
        transport.connect(email.getSmtpServer(), email.getFrom(), email.getPassword());


        transport.sendMessage(message, message.getAllRecipients());


        transport.close();
    }


}


