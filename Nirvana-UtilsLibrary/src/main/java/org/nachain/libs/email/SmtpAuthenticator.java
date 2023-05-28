package org.nachain.libs.email;


public class SmtpAuthenticator extends javax.mail.Authenticator {

    private String Password;

    private String Username;

    public void setPassword(String password) {
        Password = password;
    }

    public void setUsername(String username) {
        Username = username;
    }


    @Override
    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
        return new javax.mail.PasswordAuthentication(Password, Username);
    }
}