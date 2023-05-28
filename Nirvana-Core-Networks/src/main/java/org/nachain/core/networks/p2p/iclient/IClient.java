package org.nachain.core.networks.p2p.iclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.iclient.global.IClientConfig;
import org.nachain.core.networks.p2p.isc.NetClientNode;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SenderService;
import org.nachain.core.networks.p2p.isc.factory.ISCAction;
import org.nachain.core.networks.p2p.isc.global.ISCConfig;

import java.util.Iterator;


@Slf4j
public class IClient extends Thread {


    private String ServerIP;


    private int ServerPort;


    protected boolean isRun = false;


    protected boolean isEnd = true;


    protected boolean isRetry = true;


    private boolean isConnection = false;


    private NetClientNode netClientNode = IClientConfig.defaultClientNode;


    private NetResources netResources = new NetResources();


    private EventLoopGroup group;
    private Bootstrap bootstrap;


    private ClientChannelHandler clientChannelHandler;


    private ChannelInitializer channelInitializer;


    public IClient(String ServerIP, int ServerPort, ISCAction iAction) {
        this.ServerIP = ServerIP;
        this.ServerPort = ServerPort;


        clientChannelHandler = new ClientChannelHandler(iAction, netResources);
    }


    public IClient(String ServerIP, int ServerPort, String ClientName, ISCAction iAction) {
        this.ServerIP = ServerIP;
        this.ServerPort = ServerPort;


        netClientNode.setNodeName(ClientName);


        clientChannelHandler = new ClientChannelHandler(iAction, netResources);
    }


    private void initClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);

        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(getChannelInitializer());
    }


    @Override
    public void run() {


        synchronized (this) {

            if (isRun) {


                log.warn("Warning: This thread has been started and cannot be run more than once. For data security, this thread actively stopped the task running!", new Exception("Active throw stack tracks program call locations"));
                return;
            } else {

                isRun = true;

                isEnd = false;
            }
        }


        log.debug("Client starts, client name:" + getNodeName() + ", ServerIP:" + ServerIP + ", ServerPort:" + ServerPort + ", Server version:" + IClientConfig.appVersion);


        try {

            netResources.setIServerClient(this);


            if (isRetry) {

                while (isRun) {

                    if (!isConnection) {
                        connection();
                    }

                    Thread.sleep(8000);
                }
            } else {
                connection();
            }
        } catch (Exception e) {

            log.error("Server side error:", e);
        } finally {
            group.shutdownGracefully();
        }


        isEnd = true;
    }


    public void connection() {
        try {

            initClient();


            ChannelFuture f = bootstrap.connect(ServerIP, ServerPort);
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {

                        isConnection = true;

                        log.debug("ChannelFutureListener Connection successful");
                    } else {
                        log.debug("ChannelFutureListener Connection failure");
                        isConnection = false;
                    }
                }
            }).sync();


            if (isConnection) {
                f.channel().closeFuture().sync();
            }

        } catch (Exception e) {
            if (group != null) {
                group.shutdownGracefully();
            }
            log.error("Server side error, Connection close.", e);
        }
    }


    public void waitReady() {
        while (!netResources.isReady()) {

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }


    public void sendNetData(String cmdName, String cmdInfo, Object data) {

        waitReady();


        Iterator<Channel> channels = netResources.getChannelGroup().iterator();
        if (channels.hasNext()) {
            SenderService.sendJsonNetData(channels.next(), cmdName, cmdInfo, data);
        } else {

            log.error("Error sending data, please check the message channel!");
        }
    }


    public void stopClient() {
        group.shutdownGracefully();
    }


    private ChannelInitializer getDefaultChannelInit() {
        return getDelimiterChannelInit();
    }


    private ChannelInitializer getDelimiterChannelInit() {
        ChannelInitializer channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ByteBuf delimiter = Unpooled.copiedBuffer(ISCConfig.DELIMITER.getBytes());


                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(IClientConfig.maxFrameLength, delimiter));

                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(getClientChannelHandler());
            }
        };

        return channelInitializer;
    }


    public ChannelInitializer getChannelInitializer() {


        if (channelInitializer == null) {
            channelInitializer = getDefaultChannelInit();
        }

        return channelInitializer;
    }


    public void setChannelInitializer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }


    public ClientChannelHandler getClientChannelHandler() {
        return clientChannelHandler;
    }


    public void setClientChannelHandler(ClientChannelHandler clientChannelHandler) {
        this.clientChannelHandler = clientChannelHandler;
    }


    public boolean isEnd() {
        return isEnd;
    }


    public boolean isRun() {
        return isRun;
    }

    public void notRun() {
        isRun = false;
    }


    public String getServerIP() {
        return ServerIP;
    }


    public void setServerIP(String serverIP) {
        ServerIP = serverIP;
    }


    public int getServerPort() {
        return ServerPort;
    }


    public void setServerPort(int serverPort) {
        ServerPort = serverPort;
    }


    public String getNodeName() {
        return netClientNode.getNodeName();
    }


    public NetClientNode getNetClientNode() {
        return netClientNode;
    }

    public NetResources getNetResources() {
        return netResources;
    }

    public void setNetResources(NetResources netResources) {
        this.netResources = netResources;
    }


    public boolean isConnection() {
        return isConnection;
    }

    public void setConnection(boolean connection) {
        isConnection = connection;
    }


    public boolean isRetry() {
        return isRetry;
    }


    public void setRetry(boolean retry) {
        isRetry = retry;
    }
}