package org.nachain.core.networks.p2p.iserver;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.factory.ISCAction;


@Slf4j
@ChannelHandler.Sharable
public class ServerChannelHandler extends SimpleChannelInboundHandler<Object> {


    private ISCAction iAction;


    private NetResources netResources;

    public ServerChannelHandler(ISCAction iAction, NetResources netResources) {
        this.iAction = iAction;
        this.netResources = netResources;
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {

        try {

            String channelID = ctx.channel().id().toString();

            log.debug("Connection of channels(handlerAdded), ChannelID=" + channelID);


            netResources.getChannelGroup().add(ctx.channel());
            netResources.setChannel(channelID, ctx.channel());


            iAction.connect(ctx, netResources);

        } catch (Exception e) {
            log.error("netty connection error", e);
        }
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {

        try {

            String channelID = ctx.channel().id().toString();


            log.debug("Channel interrupt(handlerRemoved), ChannelID=" + channelID);


            netResources.getChannelGroup().remove(ctx.channel());
            netResources.removeChannel(channelID);


            iAction.disconnect(ctx, netResources);
        } catch (Exception e) {
            log.error("netty connection error", e);
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {

            iAction.read(ctx, msg, netResources);
        } catch (Exception e) {
            log.error("netty read error", e);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

        iAction.readComplete(ctx, netResources);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();


        iAction.exception(ctx, cause, netResources);
    }

}