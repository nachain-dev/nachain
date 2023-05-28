package org.nachain.core.networks.p2p.isc;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class NetServerNode {


    private String NodeName;


    private double NodeId;


    private String NodeType;


    private String IP;


    private int Port;


    private String AuthPassword;

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public double getNodeId() {
        return NodeId;
    }

    public void setNodeId(double nodeId) {
        NodeId = nodeId;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public String getAuthPassword() {
        return AuthPassword;
    }

    public void setAuthPassword(String authPassword) {
        AuthPassword = authPassword;
    }

    public String getNodeType() {
        return NodeType;
    }

    public void setNodeType(String nodeType) {
        this.NodeType = nodeType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }


}