package org.nachain.core.networks.p2p.iclient;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.factory.ISCAction;


@Slf4j
@ChannelHandler.Sharable
public class ClientChannelHandler extends SimpleChannelInboundHandler<Object> {


    private ISCAction iAction;

    private NetResources netResources;

    public ClientChannelHandler(ISCAction iAction, NetResources netResources) {
        this.iAction = iAction;
        this.netResources = netResources;
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        try {

            String channelID = ctx.channel().id().toString();


            log.debug("Channel connection(handlerAdded), ChannelID=" + channelID);


            netResources.getChannelGroup().add(ctx.channel());
            netResources.setChannel(channelID, ctx.channel());


            iAction.connect(ctx, netResources);
        } catch (Exception e) {

            log.error("Error during channel connection:", e);
        }
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        try {

            String channelID = ctx.channel().id().toString();

            log.debug("Channel disconnection(handlerRemoved), ChannelID=" + channelID);


            netResources.getChannelGroup().remove(ctx.channel());
            netResources.removeChannel(channelID);


            iAction.disconnect(ctx, netResources);


            IClient iClient = (IClient) netResources.getIServerClient();
            iClient.setConnection(false);

            log.debug("Ready to reconnect, ChannelID=" + channelID);

        } catch (Exception e) {
            log.error("Error during channel disconnection:", e);
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {

            iAction.read(ctx, msg, netResources);
        } catch (Exception e) {

            log.error("The channel failed to read data:", e);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        try {

            iAction.readComplete(ctx, netResources);
        } catch (Exception e) {

            log.error("Error occurred when the channel finished reading data:", e);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            ctx.close();

            iAction.exception(ctx, cause, netResources);
        } catch (Exception e) {

            log.error("Channel receives abnormal message:", e);
        }
    }

}