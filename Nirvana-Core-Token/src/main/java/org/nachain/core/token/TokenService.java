package org.nachain.core.token;

import com.google.common.collect.Lists;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.TxReservedWord;
import org.nachain.core.config.ChainConfig;
import org.nachain.core.token.protocol.AbstractProtocol;
import org.nachain.core.token.protocol.NFTProtocol;
import org.nachain.core.token.protocol.NormalProtocol;
import org.nachain.core.token.protocol.NormalProtocolService;
import org.nachain.core.util.JsonUtils;
import org.nachain.core.util.RegexpUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TokenService {


    public static final int VERIFY_SYMBOL_LENGTH = 7;
    public static final int VERIFY_NAME_LENGTH = 20;
    public static final int VERIFY_INFO_LENGTH = 200;
    public static final int VERIFY_ADDRESS_LENGTH = 35;


    public static List<String> RESERVED_SYMBOLS = Lists.newArrayList(

            "BTC", "ETH", "USDT", "USDC", "BNB", "BUSD", "XRP", "ADA", "SOL", "DOGE", "DOT", "SHIB", "DAI", "STETH", "MATIC", "TRX", "AVAX", "WBTC", "ATOM", "LEO", "UNI", "ETC", "OKB", "LTC", "LINK", "FTT", "NEAR", "XLM", "CRO", "XMR", "ALGO", "BCH", "LUNC", "APE", "FLOW", "VET", "ICP", "FIL", "QNT", "FRAX", "XTZ", "CHZ", "HBAR", "XCN", "MANA", "EOS", "SAND", "LDO", "AXS", "EGLD", "TUSD", "AAVE", "THETA", "USDP", "BSV", "CUSDC", "KCS", "EVMOS", "BTT", "XEC", "GRT", "USDD", "MIOTA", "ZEC", "HT", "USDN", "CEL", "CAKE", "OSMO", "KLAY", "GT", "SNX", "NEO", "FTM", "HNT", "XRD", "MKR", "PAXG", "BIT", "CDAI", "TKX", "RUNE", "NEXO", "CETH", "DFI", "AR", "ENJ", "RVN", "ZIL", "RPL", "LUNA", "BAT", "STX", "DASH", "XAUT", "WAVES", "TWT", "KAVA", "LRC", "AMP",

            "nBTC", "nETH", "nUSDT", "nUSDC", "nBNB", "nBUSD", "nXRP", "nADA", "nSOL", "nDOGE", "nDOT", "nSHIB", "nDAI", "nSTETH", "nMATIC", "nTRX", "nAVAX", "nWBTC", "nATOM", "nLEO", "nUNI", "nETC", "nOKB", "nLTC", "nLINK", "nFTT", "nNEAR", "nXLM", "nCRO", "nXMR", "nALGO", "nBCH", "nLUNC", "nAPE", "nFLOW", "nVET", "nICP", "nFIL", "nQNT", "nFRAX", "nXTZ", "nCHZ", "nHBAR", "nXCN", "nMANA", "nEOS", "nSAND", "nLDO", "nAXS", "nEGLD", "nTUSD", "nAAVE", "nTHETA", "nUSDP", "nBSV", "nCUSDC", "nKCS", "nEVMOS", "nBTT", "nXEC", "nGRT", "nUSDD", "nMIOTA", "nZEC", "nHT", "nUSDN", "nCEL", "nCAKE", "nOSMO", "nKLAY", "nGT", "nSNX", "nNEO", "nFTM", "nHNT", "nXRD", "nMKR", "nPAXG", "nBIT", "nCDAI", "nTKX", "nRUNE", "nNEXO", "nCETH", "nDFI", "nAR", "nENJ", "nRVN", "nZIL", "nRPL", "nLUNA", "nBAT", "nSTX", "nDASH", "nXAUT", "nWAVES", "nTWT", "nKAVA", "nLRC", "nAMP",

            "XSU", "BTN", "INR", "CNY", "MOP", "HKD", "XAF", "DKK", "UAH", "UZS", "UGX", "UYI", "UYU", "YER", "AMD", "ILS", "IQD", "IRR", "BWP", "BZD", "RUB", "BGN", "HRK", "USD", "GMD", "ISK", "GNF", "XOF", "CHF", "CDF", "LYD", "LRD", "CAD", "GHS", "HUF", "", "SSP", "ZAR", "QAR", "RWF", "EUR", "IDR", "GTQ", "ERN", "CUC", "CUP", "TWD", "KGS", "DJF", "KZT", "COP", "COU", "CRC", "XDR", "AUD", "TMT", "TRY", "XCD", "STN", "SHP", "ANG", "GYD", "TZS", "EGP", "ETB", "TJS", "RSD", "SLL", "SCR", "MXN", "MXV", "DOP", "GBP", "VEF", "BDT", "AOA", "NIO", "NGN", "NPR", "BSD", "PKR", "BBD", "PGK", "PYG", "PAB", "BHD", "BRL", "NOK", "BIF", "NZD", "KYD", "SBD", "CZK", "MDL", "MAD", "BND", "FJD", "SZL", "LKR", "SGD", "XPF", "JPY", "CLF", "CLP", "KPW", "KHR", "GEL", "MRU", "MUR", "TOP", "SAR", "PLN", "BAM", "THB", "ZWL", "HNL", "HTG", "JMD", "TTD", "BOB", "BOV", "SEK", "CHE", "CHW", "VUV", "BYR", "BMD", "GIP", "FKP", "KWD", "KMF", "PEN", "TND", "SOS", "JOD", "NAD", "CVE", "MMK", "RON", "USN", "LAK", "KES", "SDG", "SRD", "MZN", "LSL", "PHP", "SVC", "WST", "MNT", "ZMW", "VND", "AZN", "AFN", "DZD", "ALL", "SYP", "AED", "OMR", "ARS", "AWG", "XUA", "KRW", "MKD", "MVR", "MWK", "MYR", "MGA", "LBP",

            "NA", "Nirvana", "DAO", "SWAP", "DFS", "DNS", "NFT", "TOKEN", "WEB", "DWEB", "APP", "DAPP", "NPP"
    );


    public static Token newToken(String name, String symbol, String info, Amount amount, Amount initialAmount, Amount initialTestNetAmount, String initialAddress, int decimals, TokenTypeEnum tokenType, TokenProtocolEnum tokenProtocol, AbstractProtocol protocol) {

        Token token = new Token();

        token.setId(0);

        token.setInstanceId(0);
        token.setName(name);
        token.setVersion("1.0");
        token.setSymbol(symbol);
        token.setInfo(info);
        token.setLogoUrl("");
        token.setLogoBase64("");
        token.setAmount(amount.toBigInteger());
        token.setInitialAmount(initialAmount.toBigInteger());
        token.setInitialTestNetAmount(initialTestNetAmount.toBigInteger());
        token.setInitialAddress(initialAddress);
        token.setDecimals(decimals);
        token.setTokenType(tokenType);
        token.setTokenProtocol(tokenProtocol);

        token.setAuthor("");
        token.setInstanceType(InstanceType.Token);
        token.setProtocol(protocol);
        token.setHash(token.encodeHashString());

        return token;
    }


    public static Token newNormalToken(String name, String symbol, String info, Amount amount, String initialAddress) {

        NormalProtocol normalProtocol = NormalProtocolService.newNormalProtocol();


        return newToken(name, symbol, info, amount, amount, Amount.ZERO, initialAddress, Unit.NAC.exp, TokenTypeEnum.FIXED, TokenProtocolEnum.NORMAL, normalProtocol);
    }


    public static Token newNFToken(String name, String symbol, String info, Amount amount, NFTProtocol nftProtocol) {

        return newToken(name, symbol, info, amount, Amount.ZERO, Amount.ZERO, "", Unit.NAC.exp, TokenTypeEnum.FIXED, TokenProtocolEnum.NFT, nftProtocol);
    }


    public static String getProtocolJson(Token token) {

        final String[] protocolData = new String[1];


        IProtocolCallback callback = new IProtocolCallback() {
            @Override
            public void callNormalProtocol(NormalProtocol normalProtocol) {
                protocolData[0] = JsonUtils.objectToJson(normalProtocol);
            }

            @Override
            public void callNFTProtocol(NFTProtocol nftProtocol) {
                protocolData[0] = JsonUtils.objectToJson(nftProtocol);
            }
        };


        switch (token.getTokenProtocol()) {
            case NORMAL:
                NormalProtocol normalProtocol = (NormalProtocol) token.getProtocol();
                callback.callNormalProtocol(normalProtocol);
                break;
            case NFT:
                NFTProtocol nftProtocol = (NFTProtocol) token.getProtocol();
                callback.callNFTProtocol(nftProtocol);
                break;
        }

        return protocolData[0];
    }


    interface IProtocolCallback {
        void callNormalProtocol(NormalProtocol normalProtocol);

        void callNFTProtocol(NFTProtocol nftProtocol);
    }


    public static void sortByTokenId(List<Token> tokenList) {
        Collections.sort(tokenList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Token t1 = (Token) o1;
                Token t2 = (Token) o2;
                return Long.valueOf(t1.getId()).compareTo(Long.valueOf(t2.getId()));
            }
        });
    }


    public static boolean isReservedSymbol(String symbol) {
        for (String sym : RESERVED_SYMBOLS) {
            if (sym.equalsIgnoreCase(symbol)) {
                return true;
            }
        }
        return false;
    }


    private static boolean isGenesisAuthor(String author) {
        return author.equals(ChainConfig.GENESIS_WALLET_ADDRESS) || author.equals(TxReservedWord.GENESIS.toString());
    }


    public static void verifyDeployToken(Token token) {

        token.setSymbol(token.getSymbol().trim());
        token.setName(token.getName().trim());
        token.setInfo(token.getInfo().trim());

        String symbol = token.getSymbol();

        String author = token.getAuthor();


        if (symbol.length() > VERIFY_SYMBOL_LENGTH) {
            throw new ChainException("Deploy token symbol is too long. Limit to %d characters.", VERIFY_SYMBOL_LENGTH);
        } else if (!RegexpUtils.isValidate(RegexpUtils.REGEXP_EN_NUM, symbol)) {
            throw new ChainException("The symbol allows only English and numerals.");
        }

        if (TokenSingleton.get().existSymbol(symbol)) {

            if (isGenesisAuthor(author)) {

                if (TokenSingleton.get().count() >= 5) {
                    throw new ChainException("Is genesis author. The token name already exists. Token amount %d", TokenSingleton.get().count());
                }
            } else {
                throw new ChainException("The token name already exists.");
            }
        }


        if (isReservedSymbol(symbol) && !isGenesisAuthor(author)) {
            throw new ChainException("This symbol name '%s' is the internal reserved name. Author is %s", symbol, author);
        }


        String name = token.getName();
        if (name.length() > VERIFY_NAME_LENGTH) {
            throw new ChainException("Deploy token name is too long. Limit to %d characters.", VERIFY_NAME_LENGTH);
        } else if (!RegexpUtils.isValidate(RegexpUtils.REGEXP_NORMAL, name)) {
            throw new ChainException("The name allows only English and numerals.");
        }


        String info = token.getInfo();
        if (info.length() > VERIFY_INFO_LENGTH) {
            throw new ChainException("Deploy token info is too long. Limit to %d characters.", VERIFY_INFO_LENGTH);
        } else if (!RegexpUtils.isValidate(RegexpUtils.REGEXP_NORMAL, info)) {
            throw new ChainException("The info allows only English and numerals.");
        }


        String initialAddress = token.getInitialAddress();
        if (initialAddress.length() > VERIFY_ADDRESS_LENGTH) {
            throw new ChainException("Deploy token initialAddress is too long. Limit to %d characters.", VERIFY_ADDRESS_LENGTH);
        }


        switch (token.getTokenProtocol()) {
            case NORMAL:
                break;
            case NFT:

                NFTProtocol nftProtocol = (NFTProtocol) token.getProtocol();

                long sumBatchMintTotal = nftProtocol.sumBatchMintTotal();
                long tokenAmount = Amount.toTokenLong(token.getAmount());
                if (sumBatchMintTotal != tokenAmount) {
                    throw new ChainException("The number of tokens issued by deployed NFT does not match. Batch amount =%d, token amount=%d.", sumBatchMintTotal, tokenAmount);
                }
                break;
            default:
                break;
        }
    }


    public static boolean isValidateIcon(String logoBase64) {

        long fileSize = base64ImageSize(logoBase64);

        final int maxSize = 16 * 1024;
        if (fileSize > maxSize) {
            return false;
        }
        return true;
    }


    public static int base64ImageSize(String imageData) {

        String str = imageData.substring(22);


        int equalIndex = str.indexOf("=");
        if (str.indexOf("=") > 0) {
            str = str.substring(0, equalIndex);
        }


        int strLength = str.length();


        int size = strLength - (strLength / 8) * 2;

        return size;
    }


    public static boolean isNft(long tokenId) {
        return isNft(TokenSingleton.get().get(tokenId));
    }


    public static boolean isNft(Token token) {
        return token.getTokenProtocol() == TokenProtocolEnum.NFT;
    }

}
