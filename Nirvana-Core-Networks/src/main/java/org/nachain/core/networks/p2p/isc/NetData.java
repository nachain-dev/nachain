package org.nachain.core.networks.p2p.isc;

import org.nachain.core.networks.p2p.isc.bean.Form;
import org.nachain.libs.util.CommUtil;

import java.util.ArrayList;
import java.util.List;


public class NetData {


    private int cmdValue;


    private String cmdName;


    private String cmdInfo;


    private String sendToIP;


    private String senderName;


    private int senderServerPort;


    private String version;


    private String dataType;


    private Object data;


    private boolean flag = true;


    private int responseCode;


    private String message;


    private NetData returnedNetData;

    private NetData() {

    }


    public NetData(int CmdValue, String CmdInfo, Object Data) {
        this.cmdValue = CmdValue;
        this.cmdInfo = CmdInfo;
        this.data = Data;
    }


    public NetData(int CmdValue, String CmdInfo, String SenderName, Object Data) {
        this.cmdValue = CmdValue;
        this.cmdInfo = CmdInfo;
        this.senderName = SenderName;
        this.data = Data;
    }


    public NetData(String CmdName, String CmdInfo, Object Data) {
        this.cmdName = CmdName;
        this.cmdInfo = CmdInfo;
        this.data = Data;
    }


    public NetData(String CmdName, String CmdInfo, String SenderName, Object Data) {
        this.cmdName = CmdName;
        this.cmdInfo = CmdInfo;
        this.senderName = SenderName;
        this.data = Data;
    }

    public int getCmdValue() {
        return cmdValue;
    }

    public NetData setCmdValue(int cmdValue) {
        this.cmdValue = cmdValue;
        return this;
    }

    public String getCmdName() {
        return cmdName;
    }

    public NetData setCmdName(String cmdName) {
        this.cmdName = cmdName;
        return this;
    }

    public String getCmdInfo() {
        return cmdInfo;
    }

    public NetData setCmdInfo(String cmdInfo) {
        this.cmdInfo = cmdInfo;
        return this;
    }

    public String getSendToIP() {
        return sendToIP;
    }

    public NetData setSendToIP(String sendToIP) {
        this.sendToIP = sendToIP;
        return this;
    }

    public String getSenderName() {
        return senderName;
    }

    public NetData setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public int getSenderServerPort() {
        return senderServerPort;
    }

    public NetData setSenderServerPort(int senderServerPort) {
        this.senderServerPort = senderServerPort;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public NetData setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public NetData setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public Object getData() {
        return data;
    }

    public NetData setData(Object data) {
        this.data = data;
        return this;
    }

    public boolean isFlag() {
        return flag;
    }

    public NetData setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public NetData setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public NetData setMessage(String message) {
        this.message = message;
        return this;
    }

    public NetData getReturnedNetData() {
        return returnedNetData;
    }

    public NetData setReturnedNetData(NetData returnedNetData) {
        this.returnedNetData = returnedNetData;
        return this;
    }

    @Override
    public String toString() {
        return "NetData{" +
                "cmdValue=" + cmdValue +
                ", cmdName='" + cmdName + '\'' +
                ", cmdInfo='" + cmdInfo + '\'' +
                ", sendToIP='" + sendToIP + '\'' +
                ", senderName='" + senderName + '\'' +
                ", senderServerPort=" + senderServerPort +
                ", version='" + version + '\'' +
                ", dataType='" + dataType + '\'' +
                ", data=" + data +
                ", flag=" + flag +
                ", responseCode=" + responseCode +
                ", message='" + message + '\'' +
                ", returnedNetData=" + returnedNetData +
                '}';
    }


    public List<?> toDataList() {
        if (getData() == null) {
            return new ArrayList<Object>();
        } else if (getData() instanceof List) {
            return (List<?>) getData();
        } else {
            return new ArrayList<Object>();
        }
    }


    public String toDataString() {
        return CommUtil.null2String(data);
    }


    public double toDataDouble() {
        if (data instanceof Double) {
            return CommUtil.null2Double(data);
        }

        return 0;
    }


    public long toDataLong() {
        if (data instanceof Long) {
            return CommUtil.null2Long(data);
        }
        return 0;
    }


    public int toDataInt() {
        if (data instanceof Long) {
            return CommUtil.null2Int(data);
        }
        return 0;
    }


    public Form toForm() {

        Form form = SCService.buildForm(cmdName);

        return form;
    }


}