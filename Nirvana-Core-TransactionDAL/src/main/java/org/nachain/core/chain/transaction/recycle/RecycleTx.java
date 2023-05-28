package org.nachain.core.chain.transaction.recycle;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;


@Data
public class RecycleTx {


    long blockHeight;


    String miner;


    String cause;


    Tx tx;


    String hash;


    public String toHashString() {
        return "RecycleTx{" +
                "blockHeight=" + blockHeight +
                ", miner='" + miner + '\'' +
                ", cause='" + cause + '\'' +
                ", tx=" + tx +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }


    public byte[] encodeHash() throws Exception {

        return Hash.h256(this.toHashString().getBytes());
    }


    public String encodeHashString() throws Exception {
        return Hex.encode0x(encodeHash());
    }

}
