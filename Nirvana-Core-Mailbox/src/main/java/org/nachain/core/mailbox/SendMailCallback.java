package org.nachain.core.mailbox;


public interface SendMailCallback {
    void send(long instanceId, Mail mail);
}
