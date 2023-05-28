package org.nachain.core.dapp.internal.swap;

public class PairService {


    private static final String PAIR_SEPARATOR = "/";


    public static String toPairName(String base, String quote) {
        return String.format("%s%s%s", base, PAIR_SEPARATOR, quote);
    }


    public static String getBaseName(String pairName) {
        return pairName.substring(0, pairName.indexOf(PAIR_SEPARATOR));
    }


    public static String getQuoteName(String pairName) {
        return pairName.substring(pairName.indexOf(PAIR_SEPARATOR) + PAIR_SEPARATOR.length());
    }


    public static String reverse(String pairName) {
        return toPairName(getQuoteName(pairName), getBaseName(pairName));
    }
}
