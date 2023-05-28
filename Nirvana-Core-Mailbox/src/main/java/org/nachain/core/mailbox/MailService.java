package org.nachain.core.mailbox;

import com.google.common.collect.Lists;
import org.nachain.core.chain.instance.npp.Core;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.structure.instance.dto.InstanceDTO;
import org.nachain.core.chain.structure.instance.dto.InstanceDTOService;
import org.nachain.core.chain.transaction.*;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxContextService;
import org.nachain.core.crypto.Key;
import org.nachain.core.token.CoreTokenEnum;
import org.nachain.core.token.Token;
import org.nachain.core.token.TokenIconDTO;
import org.nachain.core.token.TokenSingleton;

import java.math.BigInteger;

public class MailService {


    public static Mail newInstallCoreMail(Core core, String sendWallet, BigInteger sendValue, BigInteger gas, long txHeight, Key sendKey) throws Exception {

        String toWallet = TxReservedWord.INSTANCE.name;


        Token nacToken = TokenSingleton.get().getByInstanceId(CoreTokenEnum.NAC.id);
        String nacTokenHash = "";
        if (nacToken != null) {
            nacTokenHash = nacToken.getHash();
        }


        InstanceDTO instanceDTO = InstanceDTOService.newInstanceDTO(core.getName(), core.getVersion(), core.getName(), core.getName(), InstanceType.Core, core.getHash(), Lists.newArrayList(nacTokenHash), core);


        TxContext txContext = TxContextService.newInstallContext(instanceDTO);


        Tx sendTx = TxService.newTx(TxType.TRANSFER, CoreInstanceEnum.APPCHAIN.id, CoreTokenEnum.NAC.id, sendWallet, toWallet, sendValue, gas, TxGasType.NAC.value, txHeight, txContext, "", 0, sendKey);


        Mail mail = Mail.newMail(MailType.MSG_SEND_TX, sendTx.toJson());

        return mail;
    }


    public static Mail newInstallTokenMail(Token token, String sendWallet, BigInteger sendValue, BigInteger gas, long txHeight, Key sendKey) throws Exception {

        String toWallet = TxReservedWord.INSTANCE.name;


        InstanceDTO instanceDTO = InstanceDTOService.newInstanceDTO(token.getName(), token.getVersion(), token.getSymbol(), token.getInfo(), InstanceType.Token, token.getHash(), Lists.newArrayList(token.getHash()), token);


        TxContext txContext = TxContextService.newInstallContext(instanceDTO);


        Tx sendTx = TxService.newTx(TxType.TRANSFER, CoreInstanceEnum.APPCHAIN.id, CoreTokenEnum.NAC.id, sendWallet, toWallet, sendValue, gas, TxGasType.NAC.value, txHeight, txContext, "", 0, sendKey);


        Mail mail = Mail.newMail(MailType.MSG_SEND_TX, sendTx.toJson());

        return mail;
    }


    public static Mail newTokenIconMail(TokenIconDTO tokenIconDTO, String sendWallet, BigInteger sendValue, BigInteger gas, long txHeight, Key sendKey) throws Exception {

        String toWallet = TxReservedWord.INSTANCE.name;


        TxContext txContext = TxContextService.newTokenIconContext(CoreInstanceEnum.APPCHAIN.id, tokenIconDTO);


        Tx sendTx = TxService.newTx(TxType.TRANSFER, CoreInstanceEnum.APPCHAIN.id, CoreTokenEnum.NAC.id, sendWallet, toWallet, sendValue, gas, TxGasType.NAC.value, txHeight, txContext, "Token Icon", 0, sendKey);


        Mail mail = Mail.newMail(MailType.MSG_SEND_TX, sendTx.toJson());

        return mail;
    }


    public static Mail sendSameChainMail(long fromInstanceId, long token, String fromWallet, String toWallet, BigInteger sendValue, long txHeight, BigInteger gas, TxContext txContext, SendMailCallback callback) throws Exception {

        Key sendKey = TxSendService.getSendKey(fromWallet);


        Tx sendTx = TxService.newTx(TxType.TRANSFER,
                fromInstanceId, token, fromWallet, toWallet, sendValue,
                gas, TxGasType.NAC.value, txHeight, txContext,
                "", 0, sendKey);


        Mail mail = Mail.newMail(MailType.MSG_SEND_TX, sendTx.toJson());


        if (callback != null) {
            callback.send(fromInstanceId, mail);
        } else {
            MailBoxDAO mailBoxDAO = new MailBoxDAO(fromInstanceId);
            mailBoxDAO.add(mail);
        }

        return mail;
    }

}
