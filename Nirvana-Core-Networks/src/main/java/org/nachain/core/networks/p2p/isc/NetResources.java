package org.nachain.core.networks.p2p.isc;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.MapUtil;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class NetResources {


    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    private ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();


    private ConcurrentHashMap<String, Boolean> onlineMap = new ConcurrentHashMap<String, Boolean>();


    private ConcurrentHashMap<String, String> nodeNameMappingChannelID = new ConcurrentHashMap<String, String>();


    private ConcurrentHashMap<String, String> channelIDMappingNodeName = new ConcurrentHashMap<String, String>();


    private ConcurrentHashMap<String, Object> session = new ConcurrentHashMap<String, Object>();


    private ConcurrentHashMap<String, String> groupingNameMap = new ConcurrentHashMap<String, String>();


    private ConcurrentHashMap<String, ChannelGroup> groupingMap = new ConcurrentHashMap<String, ChannelGroup>();


    private Vector<NetData> returnedVector = new Vector<NetData>();


    private boolean isReady = false;


    public Object iServerClient;


    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }


    public Channel getChannel(String channelID) {
        return channelMap.get(channelID);
    }


    public Channel getChannelByNodeName(String nodeName) {
        String channelID = getChannelID(nodeName);
        return channelMap.get(channelID);
    }


    public void setChannel(String channelID, Channel channel) {
        this.channelMap.put(channelID, channel);
    }


    public void removeChannel(String channelID) {
        this.channelMap.remove(channelID);
    }


    public void login(String channelID) {
        onlineMap.put(channelID, true);
    }


    public void doLogout(String channelID) {
        onlineMap.remove(channelID);
    }


    public boolean isOnline(String channelID) {
        if (this.onlineMap.containsKey(channelID)) {
            return true;
        } else {
            return false;
        }
    }


    public int onlineTotal() {
        return onlineMap.size();
    }


    public List<Object> onlineList() {
        return MapUtil.map2KeyList(onlineMap);
    }


    public void addOnline(String channelID) {
        this.onlineMap.put(channelID, true);
    }


    public void removeOnline(String channelID) {
        this.onlineMap.remove(channelID);
    }


    public String getChannelID(String nodeName) {
        return CommUtil.null2String(nodeNameMappingChannelID.get(nodeName));
    }


    public void setChannelID(String nodeName, String channelID) {
        this.nodeNameMappingChannelID.put(nodeName, channelID);
    }


    public String getNodeName(String channelID) {
        return channelIDMappingNodeName.get(channelID);
    }


    public void setNodeName(String channelID, String nodeName) {
        this.channelIDMappingNodeName.put(channelID, nodeName);
    }


    public ConcurrentHashMap<String, Object> getSession() {
        return session;
    }


    public void setSession(String key, Object value) {
        this.session.put(key, value);
    }


    public void getSession(String key) {
        this.session.get(key);
    }


    public boolean isReady() {
        return isReady;
    }


    public void asReady() {
        isReady = true;
    }


    public void noReady() {
        isReady = false;
    }


    public void removeMapping(String channelID) {

        String nodeName = getNodeName(channelID);

        onlineMap.remove(channelID);
        if (!CommUtil.isEmpty(nodeName)) {

            channelGroup.remove(channelMap.get(channelID));

            channelMap.remove(channelID);

            nodeNameMappingChannelID.remove(nodeName);

            channelIDMappingNodeName.remove(channelID);
        }
    }


    public void addMapping(String channelID, NetResources netResources, NetData netData) {

        if (netData != null) {

            String nodeName = netData.getSenderName();

            if (!CommUtil.isEmpty(nodeName) && !netResources.nodeNameMappingChannelID.containsKey(nodeName)) {

                nodeNameMappingChannelID.put(nodeName, channelID);
                channelIDMappingNodeName.put(channelID, nodeName);
            }
        }
    }

    public Vector<NetData> getReturnedVector() {
        return returnedVector;
    }

    public void setReturnedVector(Vector<NetData> returnedVector) {
        this.returnedVector = returnedVector;
    }


    public Object getIServerClient() {
        return iServerClient;
    }

    public void setIServerClient(Object iServerClient) {
        this.iServerClient = iServerClient;
    }


    public void newChannelToGrouping(String groupName, Channel channel) {

        String channelID = channel.id().toString();

        synchronized (groupingMap) {

            ChannelGroup channelGroup;


            if (groupingMap.containsKey(groupName)) {

                channelGroup = groupingMap.get(groupName);


                log.debug("Gets an existing channel group" + groupName);
            } else {

                channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

                log.debug("Create a new channel group");
            }


            channelGroup.add(channel);


            groupingMap.put(groupName, channelGroup);


            groupingNameMap.put(channelID, groupName);


            log.debug("Save packet channel: channelID=" + channelID + ", groupName=" + groupName);
        }
    }


    public void removeChannelToGrouping(Channel channel) {

        String channelID = channel.id().toString();


        String groupName = CommUtil.null2String(groupingNameMap.get(channelID));


        log.debug("Deletes a user from a specified group:groupName=" + groupName + ", channelID=" + channelID);


        if (groupingMap.containsKey(groupName)) {

            ChannelGroup channelGroup = groupingMap.get(groupName);


            channelGroup.remove(channel);


            groupingNameMap.remove(channelID);


            log.debug("Deletion succeeded:" + channelID);
        }
    }


    public boolean existGroupingMap(String groupName) {
        if (groupingMap.containsKey(groupName)) {
            return true;
        }

        return false;
    }


    public ChannelGroup getChannelGroup(String groupName) {

        ChannelGroup channelGroup = groupingMap.get(groupName);
        return channelGroup;
    }


}