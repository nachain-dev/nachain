package org.nachain.core.networks.p2p.iserver.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.iserver.global.IServerConfig;


@Slf4j
public class WebSocketServer {


    EventLoopGroup parentGroup;
    EventLoopGroup workGroup;


    ChannelInitializer childHandler;


    private NetResources netResources = new NetResources();

    public void run() {
        parentGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        childHandler = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();


                pipeline.addLast("http-codec", new HttpServerCodec());

                pipeline.addLast("aggregator", new HttpObjectAggregator(65536));

                pipeline.addLast("http-chunked", new ChunkedWriteHandler());

                pipeline.addLast("handler", new WebSocketHandler(netResources));
            }
        };


        int ServerPort = IServerConfig.defaultServerNode.getPort();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, workGroup);
            b.channel(NioServerSocketChannel.class).handler(new LoggingHandler(IServerConfig.nettyLog));
            b.childHandler(childHandler);


            log.info("WSServer server starts waiting for client connection, port:" + ServerPort);


            Channel ch = b.bind(ServerPort).sync().channel();


            new Runnable() {
                @Override
                public void run() {

                    WebSocketService.executeReadyGo(netResources);
                }
            };


            log.debug("The WSServer server is started");


            ch.closeFuture().sync();
        } catch (Exception e) {

            log.error("The WSServer failed to start the WebSocket server:", e);
        } finally {
            parentGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    public void shutDown() {
        workGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();

        WebSocketHandler.stop();
    }


    public static void main(String[] args) {
        new WebSocketServer().run();
    }

}