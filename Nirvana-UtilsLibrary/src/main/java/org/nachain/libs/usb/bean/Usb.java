package org.nachain.libs.usb.bean;

public class Usb implements java.io.Serializable, Cloneable {

    private String DeviceID;

    private String VidPid;

    private String Name;

    private String Status;

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getVidPid() {
        return VidPid;
    }

    public void setVidPid(String vidPid) {
        VidPid = vidPid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}