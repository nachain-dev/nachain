package org.nachain.core.networks.p2p.isc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.util.JsonUtils;

import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class SenderService {


    public static void broadcast(ChannelGroup channelGroup, String data) {


        ByteBuf bb = SCService.toByteBuf(data);


        channelGroup.writeAndFlush(bb);
    }


    public static void broadcast(ChannelGroup channelGroup, NetData netData) {

        ByteBuf bb = SCService.toByteBuf(JsonUtils.objectToJson(netData));


        channelGroup.writeAndFlush(bb);
    }


    public static boolean send(ChannelHandlerContext ctx, String data) {

        if (ctx == null) {
            return false;
        }


        if (ctx.channel() == null) {
            return false;
        }


        return send(ctx.channel(), data);
    }


    public static boolean sendJsonNetData(ChannelHandlerContext ctx, String CmdName, String CmdInfo, Object data) {
        return send(ctx, new NetData(CmdName, CmdInfo, JsonUtils.objectToJson(data)));
    }


    public static boolean sendJsonNetData(ChannelHandlerContext ctx, String cmdName, String cmdInfo, String senderName, Object data) {
        return send(ctx, new NetData(cmdName, cmdInfo, senderName, JsonUtils.objectToJson(data)));
    }

    public static boolean sendJsonNetData(Channel channel, String cmdName, String cmdInfo, Object data) {
        return send(channel, new NetData(cmdName, cmdInfo, JsonUtils.objectToJson(data)));
    }


    public static boolean sendJsonNetData(ChannelHandlerContext ctx, int cmdValue, String cmdInfo, Object data) {
        return send(ctx, new NetData(cmdValue, cmdInfo, JsonUtils.objectToJson(data)));
    }

    public static boolean sendJsonNetData(Channel channel, int CmdValue, String CmdInfo, Object data) {
        return send(channel, new NetData(CmdValue, CmdInfo, JsonUtils.objectToJson(data)));
    }


    public static boolean send(ChannelHandlerContext ctx, NetData netData) {

        if (ctx == null) {
            return false;
        }


        if (ctx.channel() == null) {
            return false;
        }


        return send(ctx.channel(), netData);
    }


    public static boolean send(Channel channel, String data) {


        if (channel == null) {
            return false;
        }


        log.debug("SenderService sends data:" + data.toString());


        ByteBuf bb = SCService.toByteBuf(data);


        channel.writeAndFlush(bb);


        return true;
    }


    public static boolean send(Channel channel, NetData netData) {

        String msg = JsonUtils.objectToJson(netData);


        return send(channel, msg);
    }


    public static boolean send(ConcurrentHashMap<String, Channel> channelMap, String channelID, String data) {

        Channel channel = channelMap.get(channelID);


        if (channel == null) {
            return false;
        }


        return send(channel, data);
    }


    public static boolean send(ConcurrentHashMap<String, Channel> channelMap, String channelID, NetData netData) {

        String msg = JsonUtils.objectToJson(netData);


        return send(channelMap, channelID, msg);
    }
}