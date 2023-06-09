package org.nachain.core.chain.transaction.context;

import static java.util.Arrays.stream;


public enum TxEventType {


    INSTRUCTION_SET(-1, "INSTRUCTION_SET"),


    CORE_INSTALL(10, "CORE_INSTALL"),


    TOKEN_INSTALL(20, "TOKEN_INSTALL"),


    TOKEN_UNINSTALL(21, "TOKEN_UNINSTALL"),


    TOKEN_MINED(22, "TOKEN_MINED"),


    TOKEN_TRANSFER(23, "TOKEN_TRANSFER"),


    TOKEN_INSTANCE_DEPOSIT(24, "TOKEN_INSTANCE_DEPOSIT"),


    TOKEN_INSTANCE_WITHDRAWAL(25, "TOKEN_INSTANCE_WITHDRAWAL"),


    TOKEN_ICON(26, "TOKEN_ICON"),


    TOKEN_NFT_MINT(40, "TOKEN_NFT_MINT"),


    TOKEN_NFT_MINTED(41, "TOKEN_NFT_MINTED"),


    TOKEN_NFT_TRANSFER(42, "TOKEN_NFT_TRANSFER"),


    TOKEN_NFT_MAXMINT(43, "TOKEN_NFT_MAXMINT"),


    SUPERNODE_VOTE(60, "SUPERNODE_VOTE"),


    SUPERNODE_CANCEL(61, "SUPERNODE_CANCEL"),


    SUPERNODE_CANDIDATE(62, "SUPERNODE_CANDIDATE"),


    SUPERNODE_CANCEL_CANDIDATE(63, "SUPERNODE_CANCEL_CANDIDATE"),


    FULLNODE_PAYNOMC(70, "FULLNODE_PAYNOMC"),

    FULLNODE_PAYNAC(71, "FULLNODE_PAYNAC"),

    FULLNODE_CHANGE(72, "FULLNODE_CHANGE"),

    FULLNODE_ADMIN(73, "FULLNODE_ADMIN"),

    FULLNODE_ENABLED(74, "FULLNODE_ENABLED"),


    BRIDGE_BINDING(80, "BRIDGE_BINDING"),


    DAPP_INSTALL(200, "DAPP_INSTALL"),


    DAPP_UNINSTALL(201, "DAPP_UNINSTALL"),


    DAPP_ACTION(202, "DAPP_ACTION"),


    DWEB_INSTALL(300, "DWEB_INSTALL"),


    DWEB_UNINSTALL(301, "DWEB_UNINSTALL"),


    DWEB_ACTION(302, "DWEB_ACTION"),


    DCONTRACT_INSTALL(400, "DCONTRACT_INSTALL"),


    DCONTRACT_UNINSTALL(401, "DCONTRACT_UNINSTALL"),


    DCONTRACT_ACTION(402, "DCONTRACT_ACTION"),


    DAS_ACTION(500, "DAS_ACTION"),


    DAS_PREPAID(501, "DAS_PREPAID"),


    DFS_ACTION(600, "DFS_ACTION"),


    DAO_PROMOTION(700, "DAO_PROMOTION"),


    DAO_FREELOCK(701, "DAO_FREELOCK"),


    DAO_FREEUNLOCK(702, "DAO_FREEUNLOCK"),


    DAO_VOTES(703, "DAO_VOTES"),


    ORACLE_ACTION(800, "ORACLE_ACTION"),


    SWAP_ACTION(900, "SWAP_ACTION"),


    DNS_DOMAIN_RENT(1001, "DNS_DOMAIN_RENT"),


    DNS_DOMAIN_RENTING(1010, "DNS_DOMAIN_RENTING");

    public final int exp;
    public final String name;

    TxEventType(int exp, String name) {
        this.exp = exp;
        this.name = name;
    }


    public static TxEventType of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static TxEventType of(int id) {
        return stream(values()).filter(v -> v.exp == id).findAny().orElse(null);
    }
}
