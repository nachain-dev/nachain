package org.nachain.core.mailbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.chain.das.TxDas;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;


public class Mail {


    private String hash;


    private MailType mailType;


    private String body;


    private long conditionInstance;


    private long conditionBlock;


    private MailStatus mailStatus;


    private String cause;


    private long markBlockHeight;

    public static Mail of(String json) throws JsonProcessingException {
        return JsonUtils.jsonToPojo(json, Mail.class);
    }


    public static Mail newMail(MailType mailType, String body) {
        return newMail(mailType, 0, 0, body);
    }


    public static Mail newMail(MailType mailType, long conditionInstance, long conditionBlock, String body) {
        Mail mail = new Mail();
        mail.setMailType(mailType);
        mail.setBody(body);
        mail.setConditionInstance(conditionInstance);
        mail.setConditionBlock(conditionBlock);
        mail.setMailStatus(MailStatus.PENDING);
        mail.setCause("");

        mail.setHash(mail.toHash());

        return mail;
    }

    public String getHash() {
        return hash;
    }

    public Mail setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public MailType getMailType() {
        return mailType;
    }

    public Mail setMailType(MailType mailType) {
        this.mailType = mailType;
        return this;
    }

    public long getConditionInstance() {
        return conditionInstance;
    }

    public Mail setConditionInstance(long conditionInstance) {
        this.conditionInstance = conditionInstance;
        return this;
    }

    public long getConditionBlock() {
        return conditionBlock;
    }

    public Mail setConditionBlock(long conditionBlock) {
        this.conditionBlock = conditionBlock;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Mail setBody(String body) {
        this.body = body;
        return this;
    }


    public MailStatus getMailStatus() {
        return mailStatus;
    }

    public Mail setMailStatus(MailStatus mailStatus) {
        this.mailStatus = mailStatus;
        return this;
    }

    public String getCause() {
        return cause;
    }

    public Mail setCause(String cause) {
        this.cause = cause;
        return this;
    }

    public long getMarkBlockHeight() {
        return markBlockHeight;
    }

    public Mail setMarkBlockHeight(long markBlockHeight) {
        this.markBlockHeight = markBlockHeight;
        return this;
    }


    public String toHash() {
        return Hex.encode0x(Hash.h256(toHashString().getBytes()));
    }

    public String toHashString() {
        return "Mail{" +
                ", mailType=" + mailType +
                ", body=" + JsonUtils.objectToJson(body) +
                ", conditionInstance=" + conditionInstance +
                ", conditionBlock=" + conditionBlock +
                '}';
    }

    @Override
    public String toString() {
        return "Mail{" +
                "hash='" + hash + '\'' +
                ", mailType=" + mailType +
                ", body='" + body + '\'' +
                ", conditionInstance=" + conditionInstance +
                ", conditionBlock=" + conditionBlock +
                ", mailStatus=" + mailStatus +
                ", cause='" + cause + '\'' +
                ", markBlockHeight=" + markBlockHeight +
                '}';
    }

    public void of(Tx tx) throws JsonProcessingException {
        this.body = tx.toJson();
    }


    public Tx toTx() {
        return Tx.toTx(this.body);
    }


    public TxDas toTxDas() {
        return TxDas.toTxDas(this.body);
    }

    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }
}
