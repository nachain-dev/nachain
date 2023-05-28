package org.nachain.core.networks.p2p.iserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.NetServerNode;
import org.nachain.core.networks.p2p.isc.factory.ISCAction;
import org.nachain.core.networks.p2p.isc.global.ISCConfig;
import org.nachain.core.networks.p2p.iserver.global.IServerConfig;


@Slf4j
public class IServer extends Thread {


    protected boolean isRun = false;


    protected boolean isEnd = true;


    private int ServerPort;


    private NetServerNode serverNetNode = IServerConfig.defaultServerNode;


    private NetResources netResources = new NetResources();


    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap = new ServerBootstrap();


    private ServerChannelHandler serverChannelHandler;


    private ChannelInitializer channelInitializer;


    public IServer(ISCAction iAction) {
        this.ServerPort = serverNetNode.getPort();


        serverChannelHandler = new ServerChannelHandler(iAction, netResources);
    }


    public IServer(int ServerPort, ISCAction iAction) {
        this.ServerPort = ServerPort;


        serverChannelHandler = new ServerChannelHandler(iAction, netResources);


        serverNetNode.setPort(ServerPort);
    }


    public IServer(int ServerPort, ISCAction iAction, ChannelInitializer channelInitializer) {
        this(ServerPort, iAction);


        setChannelInitializer(channelInitializer);
    }


    private void initServer() {
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 100);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.childHandler(getChannelInitializer());
    }


    @Override
    public void run() {

        synchronized (this) {

            if (isRun) {


                log.warn("Warning: This thread has been started and cannot be run more than once. For data security, this thread actively stopped the task running!", new Exception("Actively throws the stack tracker call location"));
                return;
            } else {

                isRun = true;

                isEnd = false;
            }
        }

        log.info("IServer start,IP:" + serverNetNode.getIP() + ", port:" + ServerPort + ", server version:" + IServerConfig.AppVersion + ", server path:" + IServerConfig.AppPath);


        try {

            netResources.setIServerClient(this);


            initServer();

            ChannelFuture f = serverBootstrap.bind(this.ServerPort).sync();

            netResources.asReady();

            log.info("IServer server running...");


            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("IServer server error:", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


        isEnd = true;

        log.debug("IServer server stop!");
    }


    public void stopServer() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


    private ChannelInitializer getDefaultChannelInit() {
        return getDelimiterChannelInit();
    }


    private ChannelInitializer getDelimiterChannelInit() {
        ChannelInitializer channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ByteBuf delimiter = Unpooled.copiedBuffer(ISCConfig.DELIMITER.getBytes());


                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(IServerConfig.maxFrameLength, delimiter));

                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(getServerChannelHandler());
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


    public ServerChannelHandler getServerChannelHandler() {
        return serverChannelHandler;
    }


    public void setServerChannelHandler(ServerChannelHandler serverChannelHandler) {
        this.serverChannelHandler = serverChannelHandler;
    }


    public boolean isEnd() {
        return isEnd;
    }


    public boolean isRun() {
        return isRun;
    }


    public void waitReady() {
        while (!netResources.isReady()) {

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }


    public NetServerNode getNetServerNode() {
        return serverNetNode;
    }


    public void setServerNetNode(NetServerNode serverNetNode) {
        this.serverNetNode = serverNetNode;
    }


    public String getNodeName() {
        return serverNetNode.getNodeName();
    }


    public int getServerPort() {
        return ServerPort;
    }


    public void setServerPort(int serverPort) {
        ServerPort = serverPort;
    }

    public NetResources getNetResources() {
        return netResources;
    }

    public void setNetResources(NetResources netResources) {
        this.netResources = netResources;
    }
}