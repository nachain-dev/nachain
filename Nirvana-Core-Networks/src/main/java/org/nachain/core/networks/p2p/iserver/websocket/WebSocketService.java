package org.nachain.core.networks.p2p.iserver.websocket;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.factory.IReadyGo;
import org.nachain.core.networks.p2p.iserver.global.IServerConfig;
import org.nachain.core.networks.p2p.iserver.websocket.factory.IAction;
import org.nachain.core.networks.p2p.iserver.websocket.factory.IEventTrigger;
import org.nachain.libs.util.CommUtil;


@Slf4j
public class WebSocketService {


    public static IAction instanceAction(String actionClass) {
        IAction af = null;

        if (actionClass != null && (!actionClass.equals(""))) {
            try {
                af = (IAction) Class.forName(actionClass).newInstance();
            } catch (Exception e) {
                log.error("Action creation error" + actionClass, e);
            }
        }

        return af;
    }


    public static IReadyGo instanceReadyGo(String clazz) {
        IReadyGo af = null;

        if (clazz != null && (!clazz.equals(""))) {
            try {
                af = (IReadyGo) Class.forName(clazz).newInstance();
            } catch (Exception e) {

                log.error("Error creating ReadyGo: " + clazz, e);
            }
        }

        return af;
    }


    public static IEventTrigger instanceTrigger() {
        IEventTrigger trigger = null;

        if (StringUtils.isNotEmpty(IServerConfig.eventTriggerClassPath)) {
            try {
                trigger = (IEventTrigger) Class.forName(IServerConfig.eventTriggerClassPath).newInstance();
            } catch (Exception e) {
                log.error("Trigger creation error" + IServerConfig.eventTriggerClassPath, e);
            }
        }
        return trigger;
    }


    public static void broadcast(ChannelGroup channelGroup, String data) {
        TextWebSocketFrame tws = new TextWebSocketFrame(data);


        channelGroup.writeAndFlush(tws);
    }


    public static void broadcastGroup(NetResources netResources, String groupName, String data) {

        TextWebSocketFrame tws = new TextWebSocketFrame(data);

        if (netResources.existGroupingMap(groupName)) {

            ChannelGroup channelGroup = netResources.getChannelGroup(groupName);


            channelGroup.writeAndFlush(tws);
        }
    }


    public static void broadcast(Channel channel, String data) {

        TextWebSocketFrame tws = new TextWebSocketFrame(data);
        channel.writeAndFlush(tws);
    }


    public static void executeReadyGo(NetResources netResources) {

        if (!CommUtil.isEmpty(IServerConfig.webSocketReadyGo)) {

            IReadyGo readyGo = WebSocketService.instanceReadyGo(IServerConfig.webSocketReadyGo);
            if (readyGo != null) {
                log.debug("Execute ReadyGo:" + IServerConfig.webSocketReadyGo);

                readyGo.execute(netResources);
            }
        }
    }


    public static String toActionURL(String uri) {


        String url = "/" + CommUtil.contentSubstring(uri, 2, 0, "/", "");

        url = CommUtil.contentSubstring(url, "", "?");

        return url;
    }


    public static void sendMsg(Channel channel, String msg) {
        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }
}