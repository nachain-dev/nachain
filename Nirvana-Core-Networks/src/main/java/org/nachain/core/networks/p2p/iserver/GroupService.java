package org.nachain.core.networks.p2p.iserver;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SCService;
import org.nachain.core.networks.p2p.isc.bean.Form;
import org.nachain.core.networks.p2p.isc.global.Const;
import org.nachain.core.networks.p2p.isc.global.ISCConfig;


@Slf4j
public class GroupService {


    public static void joinGroup(NetResources netResources, String uri, Channel channel) {

        Form form = SCService.buildForm(uri);


        String cmd = form.getString(Const.CMD);

        if (cmd.equalsIgnoreCase(Const.CMD_JOIN_GROUP)) {
            String groupName = form.getString(Const.GROUP_NAME);

            netResources.newChannelToGrouping(groupName, channel);
            log.debug("Join group");
        }
    }


    public static void joinGroup(NetResources netResources, NetData netData, Channel channel) {

        int cmdValue = netData.getCmdValue();
        String groupName = (String) netData.getData();

        if (cmdValue == ISCConfig.CmdValue.JOIN_GROUP) {

            netResources.newChannelToGrouping(groupName, channel);
            log.debug("Join group");
        }
    }


    public static void exitGroup(NetResources netResources, NetData netData, Channel channel) {

        int cmdValue = netData.getCmdValue();

        if (cmdValue == ISCConfig.CmdValue.EXIT_GROUP) {

            netResources.removeChannelToGrouping(channel);
            log.debug("Exit group");
        }
    }

}
