package org.nachain.core.chain.structure.instance;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.exception.ChainException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class InstanceManager {


    private final Map<String, Instance> instanceByHashMap = new ConcurrentHashMap<>();

    private final Map<Long, String> instanceHashByIdMap = new ConcurrentHashMap<>();


    private final Map<String, Long> instanceIdByAppAddressMap = new ConcurrentHashMap<>();

    private InstanceDAO instanceDAO;

    public InstanceManager() {
        try {
            instanceDAO = new InstanceDAO();
        } catch (Exception e) {
            log.error("new InstanceManager() error", e);
        }

        loadDB();
    }


    private void loadDB() {

        try {
            List<Instance> instanceList = instanceDAO.findAll();

            for (Instance instance : instanceList) {
                addToMap(instance);
            }
            log.info("Initialize Instance:" + instanceList.size());
        } catch (Exception e) {
            log.error("Initialize InstanceManager error", e);
        }
    }


    public Map<String, Instance> getInstanceByHashMap() {
        return instanceByHashMap;
    }

    public Map<Long, String> getInstanceHashByIdMap() {
        return instanceHashByIdMap;
    }

    public Map<String, Long> getInstanceIdByAppAddressMap() {
        return instanceIdByAppAddressMap;
    }


    public List<Instance> getInstanceList() {

        Collection<Instance> instances = instanceByHashMap.values();


        List<Instance> instanceList = Lists.newArrayList(instances);


        InstanceService.sortByInstanceId(instanceList);

        return instanceList;
    }


    public void addToMap(Instance instance) {
        String instanceHash = instance.getHash();
        long instanceId = instance.getId();

        if (instanceHash == null) {
            throw new ChainException("instance.getHash() is null. instance=%s", instance);
        }


        if (!instanceByHashMap.containsKey(instanceHash)) {
            instanceByHashMap.put(instanceHash, instance);
            instanceHashByIdMap.put(instanceId, instanceHash);

            instanceIdByAppAddressMap.put(instance.getAppAddress(), instanceId);
        }
    }


    public void remove(String hash) {
        if (instanceByHashMap.containsKey(hash)) {
            Instance instance = instanceByHashMap.get(hash);

            instanceByHashMap.remove(hash);
            instanceHashByIdMap.remove(instance.getId());
            instanceIdByAppAddressMap.remove(instance.getAppAddress());
        }
    }


    public Instance getInstance(String hash) {
        if (hash == null) {
            throw new ChainException("getInstance(String hash) is error, hash is null");
        }

        Instance instance = instanceByHashMap.get(hash);
        if (instance == null) {

            loadDB();

            instance = instanceByHashMap.get(hash);
        }

        return instance;
    }


    public Instance getInstance(long instanceId) {
        if (instanceHashByIdMap.containsKey(instanceId)) {
            String hash = instanceHashByIdMap.get(instanceId);
            return getInstance(hash);
        }

        return null;
    }


    public int count() {
        return instanceByHashMap.size();
    }

}

