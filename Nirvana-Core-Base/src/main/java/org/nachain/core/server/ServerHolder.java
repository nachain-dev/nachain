package org.nachain.core.server;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ServerHolder {


    private static Map<String, AbstractServer> holder = new ConcurrentHashMap<>();


    public static Map<String, AbstractServer> getHolder() {
        return holder;
    }


    public static AbstractServer get(Class serverClass) {
        return get(serverClass.getName());
    }


    public static void register(AbstractServer abstractServer) {
        holder.put(abstractServer.getName(), abstractServer);
    }


    public static void register(String serverName, AbstractServer abstractServer) {
        holder.put(serverName, abstractServer);
    }


    public static AbstractServer get(String serverName) {
        return holder.get(serverName);
    }


    public static int count() {
        return holder.size();
    }


    public static List<AbstractServer> runningServers() {
        List<AbstractServer> runningServers = Lists.newArrayList();
        Set<Map.Entry<String, AbstractServer>> entrySet = holder.entrySet();
        for (Map.Entry<String, AbstractServer> entry : entrySet) {
            AbstractServer server = entry.getValue();

            if (!server.isEnd()) {
                runningServers.add(server);
            }
        }
        return runningServers;
    }

}
