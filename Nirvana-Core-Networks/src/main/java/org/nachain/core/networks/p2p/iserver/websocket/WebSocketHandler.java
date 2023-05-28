package org.nachain.core.networks.p2p.iserver.websocket;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SCService;
import org.nachain.core.networks.p2p.isc.bean.Form;
import org.nachain.core.networks.p2p.isc.bean.Session;
import org.nachain.core.networks.p2p.iserver.GroupService;
import org.nachain.core.networks.p2p.iserver.global.IServerConfig;
import org.nachain.core.networks.p2p.iserver.websocket.factory.IAction;
import org.nachain.core.networks.p2p.iserver.websocket.factory.IConnectTrigger;
import org.nachain.core.networks.p2p.iserver.websocket.factory.IEventTrigger;
import org.nachain.libs.action.ActionUtil;
import org.nachain.libs.util.ClassUtil;
import org.nachain.libs.util.CommUtil;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.nachain.core.networks.p2p.iserver.global.IServerConfig.coreThreadNumber;


@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private NetResources netResources;

    public WebSocketHandler(NetResources netResources) {
        this.netResources = netResources;
    }


    private WebSocketServerHandshaker handshaker;


    private static IConnectTrigger iConnectTrigger;


    private static Map<Channel, Session> sessionMap = Maps.newHashMap();

    public static Map<Channel, Session> getSessionMap() {
        return sessionMap;
    }


    private static ExecutorService executorService = Executors.newFixedThreadPool(coreThreadNumber);


    public static void stop() {
        executorService.shutdownNow();
    }


    static {
        try {

            iConnectTrigger = (IConnectTrigger) ClassUtil.newInstance(IServerConfig.connectTriggerClassPath);
        } catch (Exception e) {
            log.error(String.format("WSServerHandler - iConnectTrigger instantiation error"), e);
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {


        Channel channel = ctx.channel();

        Session session = new Session();

        sessionMap.put(channel, session);


        netResources.getChannelGroup().add(channel);

        log.debug("The connection between the client and server is enabled");


        IEventTrigger eventTrigger = WebSocketService.instanceTrigger();

        if (eventTrigger != null) {
            eventTrigger.active(channel);
        }

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        Channel channel = ctx.channel();


        sessionMap.remove(channel);


        netResources.getChannelGroup().remove(channel);

        netResources.removeChannelToGrouping(channel);

        log.debug("The connection between the client and server is closed");


        IEventTrigger eventTrigger = WebSocketService.instanceTrigger();

        if (eventTrigger != null) {
            eventTrigger.inactive(channel);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {


        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }


        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }


        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported. This program only supports text messages, do not support binary messages", frame.getClass().getName()));
        }
        final Channel channel = ctx.channel();


        String requestTex = ((TextWebSocketFrame) frame).text();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.debug("handlerWebSocketFrame.requestTex:" + requestTex);


                processAction(requestTex, channel);
            }
        };

        executorService.execute(runnable);
    }


    public void processAction(String requestTex, Channel channel) {


        String uri = handshaker.uri();


        String url = WebSocketService.toActionURL(uri);

        log.debug("processAction() uri:" + uri + ", url:" + url);

        if (StringUtils.isEmpty(url)) {
            return;
        }


        Form formFromUri = SCService.buildForm(uri);


        Form formFromRequest = SCService.buildFormByParam(requestTex);


        Map<String, Object> textElement = formFromUri.getTextElement();


        textElement.putAll(formFromRequest.getTextElement());

        log.debug("url=" + url);


        if (!CommUtil.isEmpty(url)) {
            String actionClass = ActionUtil.getActionClass(url, IServerConfig.webSocketClassPath);

            IAction iAction = WebSocketService.instanceAction(actionClass);
            if (iAction != null) {
                log.debug("Execute action:" + actionClass);

                try {

                    iAction.execute(netResources, formFromUri, requestTex, channel);
                } catch (Exception e) {
                    log.error(String.format("WSServerHandler - processing the action error"), e);
                }
            }
        }
    }


    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {


        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }


        String uri = req.uri();
        String webSocketURL = "ws:";

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(webSocketURL, null, false);
        handshaker = wsFactory.newHandshaker(req);


        log.debug("Construct the handshake response handleHttpRequest:" + webSocketURL);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {

            GroupService.joinGroup(netResources, handshaker.uri(), ctx.channel());

            handshaker.handshake(ctx.channel(), req);

            if (iConnectTrigger != null) {

                iConnectTrigger.afterConnect(ctx, req);
            }

        }

    }


    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {

        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }


        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

        log.error("Netty exception information was captured.", cause);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }
}