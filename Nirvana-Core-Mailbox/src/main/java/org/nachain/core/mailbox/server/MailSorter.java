package org.nachain.core.mailbox.server;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.das.TxDas;
import org.nachain.core.chain.das.TxDasService;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceDetailService;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxSendService;
import org.nachain.core.chain.transaction.TxService;
import org.nachain.core.config.Constants;
import org.nachain.core.intermediate.feedback.Feedback;
import org.nachain.core.mailbox.Mail;
import org.nachain.core.mailbox.MailBoxDAO;
import org.nachain.core.mailbox.MailStatus;
import org.nachain.core.mailbox.MailType;
import org.nachain.core.token.CoreTokenEnum;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;


@Slf4j
public class MailSorter {


    private final MailBoxDAO mailBoxDAO;

    private long instance;

    public MailSorter(long instance) {
        try {
            mailBoxDAO = new MailBoxDAO(instance);
            this.instance = instance;
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void processTx(Mail mail) throws Exception {

        if (mail.getMailType() != MailType.MSG_SEND_TX) {
            return;
        }


        Tx tx = mail.toTx();


        long blockHeight = InstanceDetailService.getNacBlockHeight();


        if (mail.getMailStatus() == MailStatus.PENDING) {

            Feedback feedback = TxService.validateTx(tx);


            if (feedback.isSucceed()) {
                try {

                    TxSendService.sendTx(tx);
                    log.info("sendToken:" + mail.getHash() + ", " + tx.getFrom() + "-->" + tx.getTo() + ":" + tx.getValue() + ", gas:" + tx.getGas());
                } catch (Exception e) {
                    feedback.asFail().setMessage(String.format("Send tx error. %s", e.getMessage()));
                }


                if (feedback.isFailed()) {
                    mail.setMailStatus(MailStatus.FAILED);
                    mail.setCause(feedback.getMessage());
                } else {
                    mail.setMailStatus(MailStatus.COMPLETED);
                }

                mail.setMarkBlockHeight(blockHeight);
                mailBoxDAO.edit(mail);
            } else {
                mail.setMailStatus(MailStatus.FAILED);
                mail.setCause(feedback.getMessage());
                mail.setMarkBlockHeight(blockHeight);
                mailBoxDAO.edit(mail);
            }
        } else {

            if (blockHeight - mail.getMarkBlockHeight() >= Constants.DPoS_BLOCKS_PER_DAY) {
                mailBoxDAO.delete(mail.getHash());
                log.info("Delete marked old data:" + mail.getHash());
            }
        }
    }


    private void processTxDas(Mail mail) throws Exception {

        if (mail.getMailType() != MailType.MSG_SEND_TX_DAS) {
            return;
        }


        TxDas txDas = mail.toTxDas();


        if (txDas.getInstance() != CoreInstanceEnum.NAC.id) {
            return;
        }
        if (txDas.getToken() != CoreTokenEnum.NAC.id) {
            return;
        }


        long blockHeight = InstanceDetailService.getNacBlockHeight();


        if (mail.getMailStatus() == MailStatus.PENDING) {

            Feedback feedback = TxService.validateTx(txDas);


            if (feedback.isFailed()) {

                TxDasService.sendTxDas(txDas);
                log.info("sendToken:" + mail.getHash() + ", " + txDas.getFrom() + "-->" + txDas.getTo() + ":" + txDas.getValue() + ", gas:" + txDas.getGas());


                mail.setMailStatus(MailStatus.COMPLETED);
                mail.setMarkBlockHeight(blockHeight);
                mailBoxDAO.edit(mail);
            } else {
                mail.setMailStatus(MailStatus.FAILED);
                mail.setCause(feedback.getMessage());
                mail.setMarkBlockHeight(blockHeight);
                mailBoxDAO.edit(mail);
            }
        } else {

            if (blockHeight - mail.getMarkBlockHeight() >= Constants.DPoS_BLOCKS_PER_DAY) {
                mailBoxDAO.delete(mail.getHash());
                log.info("Delete marked old data:" + mail.getHash());
            }
        }
    }


    public void execute() {
        try {

            List<Mail> mailList = mailBoxDAO.db().findAll(Mail.class);

            log.info("[instance={}] MailSorter execute size={}", instance, mailList.size());


            if (mailList.size() != 0) {

                for (Mail mail : mailList) {

                    long conditionBlock = mail.getConditionBlock();


                    if (conditionBlock > 0) {

                        long conditionInstance = mail.getConditionInstance();
                        long blockHeight = InstanceDetailService.getBlockHeight(conditionInstance);
                        if (blockHeight < conditionBlock) {

                            continue;
                        }
                    }


                    if (mail.getMailType() == MailType.MSG_SEND_TX) {
                        processTx(mail);
                    } else if (mail.getMailType() == MailType.MSG_SEND_TX_DAS) {
                        processTxDas(mail);
                    } else if (mail.getMailType() == MailType.MSG_SEND_TX_DAS) {
                        processTxDas(mail);
                    }
                }
            } else {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("MailSorter error:", e);
        }
    }
}
