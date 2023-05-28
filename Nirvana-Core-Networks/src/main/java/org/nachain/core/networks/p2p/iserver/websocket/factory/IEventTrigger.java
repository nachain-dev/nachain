package org.nachain.core.networks.p2p.iserver.websocket.factory;

import io.netty.channel.Channel;


public interface IEventTrigger {

    void active(Channel channel);

    void inactive(Channel channel);
}