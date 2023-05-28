package org.nachain.core.chain.instance.npp;

import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.TxReservedWord;
import org.nachain.core.config.ChainConfig;

public class CoreService {


    public static Core newCore(String name, String version, String author) {
        Core core = new Core();
        core.setId(0);
        core.setName(name);
        core.setVersion(version);
        core.setAuthor(author);
        core.setInstanceType(InstanceType.Core);
        core.setHash(core.encodeHashString());

        return core;
    }


    private static boolean isGenesisAuthor(String author) {
        return author.equals(ChainConfig.GENESIS_WALLET_ADDRESS) || author.equals(TxReservedWord.GENESIS.toString());
    }


    public static void verifyCore(Core core) {

        core.setName(core.getName().trim());


        String author = core.getAuthor();


        if (!isGenesisAuthor(author)) {
            throw new ChainException("The from is not the genesis address. Name is %s, author is %s.", core.getName(), author);
        }
    }
}
