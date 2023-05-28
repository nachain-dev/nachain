package org.nachain.core.networks;


import org.nachain.core.nodes.NodeRole;


public class NodeNetwork {


    private String ip;


    private String deviceName;


    private String area;


    private long lastTimestamp;


    private long lastOnlineTimestamp;


    private NodeRole nodeRole;

    public String getIp() {
        return ip;
    }

    public NodeNetwork setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public NodeNetwork setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getArea() {
        return area;
    }

    public NodeNetwork setArea(String area) {
        this.area = area;
        return this;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public NodeNetwork setLastTimestamp(long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
        return this;
    }

    public long getLastOnlineTimestamp() {
        return lastOnlineTimestamp;
    }

    public NodeNetwork setLastOnlineTimestamp(long lastOnlineTimestamp) {
        this.lastOnlineTimestamp = lastOnlineTimestamp;
        return this;
    }

    public NodeRole getNodeRole() {
        return nodeRole;
    }

    public NodeNetwork setNodeRole(NodeRole nodeRole) {
        this.nodeRole = nodeRole;
        return this;
    }

    @Override
    public String toString() {
        return "NodeNetwork{" +
                "ip='" + ip + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", area='" + area + '\'' +
                ", lastTimestamp=" + lastTimestamp +
                ", lastOnlineTimestamp=" + lastOnlineTimestamp +
                ", nodeRole=" + nodeRole +
                '}';
    }
}
