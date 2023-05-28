package org.nachain.libs.config;

import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.DateUtil;

import java.io.*;
import java.util.Date;
import java.util.Properties;

public class ConfigFile {

    private Properties config = new Properties();
    private String fileName = null;


    private boolean isEncode = false;


    private String charsetName = "ISO8859-1";

    public ConfigFile() {

    }


    public ConfigFile(String fileName) throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);

            config.load(fis);

        } catch (IOException ex) {
            throw new Exception("Unable to read the properties file:" + fileName, ex);
        } finally {
            fis.close();
        }
        this.fileName = fileName;
    }

    public Properties getConfig() {
        return config;
    }

    public ConfigFile setConfig(Properties config) {
        this.config = config;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public void setData(String data) throws Exception {

        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(data.getBytes());
            config.load(bais);
        } catch (IOException e) {
            throw new Exception("Set the data for the properties file", e);
        } finally {
            bais.close();
        }
    }


    public void setData(byte[] data) throws Exception {

        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(data);
            config.load(bais);
        } catch (IOException e) {
            throw new Exception("Set the data for the properties file", e);
        } finally {
            bais.close();
        }
    }

    public boolean isEncode() {
        return isEncode;
    }

    public void setEncode(boolean isEncode) {
        this.isEncode = isEncode;
    }


    public String getValue(String itemName) {
        String returnValue = config.getProperty(itemName);


        if (isEncode) {
            if (returnValue != null) {

                try {
                    returnValue = new String(returnValue.getBytes(charsetName), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return returnValue;
    }


    public String getValue(String itemName, String defaultValue) {
        String returnValue = config.getProperty(itemName, defaultValue);


        if (!config.containsKey(itemName)) {
            config.setProperty(itemName, defaultValue);
        }


        if (isEncode) {
            if (returnValue != null) {

                try {
                    returnValue = new String(returnValue.getBytes(charsetName), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return returnValue;
    }

    public int getValue(String itemName, int defaultValue) {
        int rtv = 0;

        String value = getValue(itemName, CommUtil.null2String(defaultValue));
        rtv = CommUtil.null2Int(value);

        return rtv;
    }

    public long getValue(String itemName, long defaultValue) {
        long rtv = 0;

        String value = getValue(itemName, CommUtil.null2String(defaultValue));
        rtv = CommUtil.null2Long(value);

        return rtv;
    }

    public double getValue(String itemName, double defaultValue) {
        double rtv = 0;

        String value = getValue(itemName, CommUtil.null2String(defaultValue));
        rtv = CommUtil.null2Double(value);

        return rtv;
    }


    public void setValue(String itemName, String value) {
        config.setProperty(itemName, value);
    }

    public void setValue(String itemName, int value) {
        setValue(itemName, CommUtil.null2String(value));
    }

    public void setValue(String itemName, long value) {
        setValue(itemName, CommUtil.null2String(value));
    }

    public void setValue(String itemName, double value) {
        setValue(itemName, CommUtil.null2String(value));
    }

    public int getIntValue(String itemName) {
        return CommUtil.null2Int(getValue(itemName));
    }

    public int getIntValue(String itemName, int defaultValue) {
        return CommUtil.null2Int(getValue(itemName, defaultValue));
    }

    public long getLongValue(String itemName) {
        return CommUtil.null2Long(getValue(itemName));
    }

    public double getDoubleValue(String itemName) {
        return CommUtil.null2Double(getValue(itemName));
    }

    public boolean getBooleanValue(String itemName) {
        return CommUtil.null2Boolean(getValue(itemName));
    }

    public Date getDateValue(String itemName) {
        return DateUtil.parseByDate(getValue(itemName));
    }

    public Date getDateTimeValue(String itemName) {
        return DateUtil.parseByDateTime(getValue(itemName));
    }


    public void saveFile(String fileName, String description) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);

            config.store(fos, description);
        } catch (IOException ex) {
            throw new ConfigFileException("Unable save the properties file:" + fileName);
        } finally {
            fos.close();
        }
    }


    public void saveFile(String fileName) throws Exception {
        saveFile(fileName, null);
    }


    public void saveFile() throws Exception {
        if (fileName == null || fileName.length() == 0) {
            throw new ConfigFileException("The configuration file name cannot be empty");
        }
        saveFile(fileName);
    }


}