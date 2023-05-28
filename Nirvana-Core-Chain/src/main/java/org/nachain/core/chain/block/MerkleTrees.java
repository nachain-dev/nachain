package org.nachain.core.chain.block;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;


public class MerkleTrees {


    private final List<String> dataList;


    private String root;


    public MerkleTrees(List<String> dataList) {
        this.dataList = dataList;
        root = "";
    }


    public void merkleTree() {

        List<String> tempTxList = new ArrayList<String>();


        if (dataList.size() == 0) return;

        for (int i = 0; i < this.dataList.size(); i++) {
            tempTxList.add(this.dataList.get(i));
        }

        List<String> newTxList = getNewDataList(tempTxList);

        while (newTxList.size() != 1) {
            newTxList = getNewDataList(newTxList);
        }

        this.root = newTxList.get(0);
    }


    private List<String> getNewDataList(List<String> tempLxList) {

        List<String> newDataList = new ArrayList<String>();

        int index = 0;

        while (index < tempLxList.size()) {

            String left = tempLxList.get(index);
            index++;

            String right = "";


            if (index != tempLxList.size()) {
                right = tempLxList.get(index);
            }

            String sha2HexValue = getSHA2HexValue(left + right);
            newDataList.add(sha2HexValue);
            index++;
        }

        return newDataList;
    }


    public String getSHA2HexValue(String str) {
        byte[] cipher_byte;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            cipher_byte = md.digest();
            StringBuilder sBuilder = new StringBuilder(2 * cipher_byte.length);
            for (byte b : cipher_byte) {
                sBuilder.append(String.format("%02x", b & 0xff));
            }

            return sBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public String getRoot() {
        return this.root;
    }
}