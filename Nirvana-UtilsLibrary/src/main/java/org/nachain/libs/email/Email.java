package org.nachain.libs.email;

public class Email {


    private String From;


    private String Password;


    private String SmtpServer;


    private String To;


    private String Subject;


    private String Content;


    private String Sender;


    private String SenderLabel;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }


    public String getSenderLabel() {
        return SenderLabel;
    }

    public void setSenderLabel(String senderLabel) {
        SenderLabel = senderLabel;
    }

    public String getSmtpServer() {
        return SmtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        SmtpServer = smtpServer;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }


}