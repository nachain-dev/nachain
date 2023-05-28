package org.nachain.core.miner.schedule;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.das.TxDas;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.Instance;
import org.nachain.core.chain.structure.instance.InstanceSingleton;
import org.nachain.core.config.Constants;
import org.nachain.core.crypto.Key;
import org.nachain.core.miner.member.MinerMember;
import org.nachain.core.miner.member.MinerMemberService;
import org.nachain.core.miner.member.dpos.DPoSMinerHolder;
import org.nachain.core.miner.member.powf.PoWFMinerHolder;
import org.nachain.core.signverify.SignVerify;
import org.nachain.core.util.CommUtils;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class MiningScheduleService {

    private static MiningScheduleDAO miningScheduleDAO;

    static {
        try {
            miningScheduleDAO = new MiningScheduleDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static MiningSchedule newMiningSchedule(long instance, long schedule, List<String> miners, Key key) throws Exception {
        MiningSchedule miningSchedule = new MiningSchedule();
        miningSchedule.setInstance(instance);
        miningSchedule.setSchedule(schedule);
        miningSchedule.setTimestamp(CommUtils.currentTimeMillis());
        miningSchedule.setMiners(miners);
        miningSchedule.setHash(miningSchedule.encodeHashString());
        miningSchedule.setSign(Hex.encode0x(SignVerify.sign(miningSchedule.toSignString(), key)));
        return miningSchedule;
    }


    public static MiningSchedule toMiningSchedule(String json) {
        return JsonUtils.jsonToPojo(json, MiningSchedule.class);
    }


    public static long calcSchedule(long instance, long blockHeight) {

        long BLOCKS_PER_DAY = (instance == CoreInstanceEnum.APPCHAIN.id) ? Constants.PoWF_BLOCKS_PER_DAY : Constants.DPoS_BLOCKS_PER_DAY;


        long blockCycle = 0;

        long mod = blockHeight % BLOCKS_PER_DAY;
        if (mod > 0) {
            blockCycle = 1;
        }
        return blockCycle + blockHeight / BLOCKS_PER_DAY;
    }


    public static long calcNextSchedule(long instance, long blockHeight) {
        return calcSchedule(instance, blockHeight) + 1;
    }


    public static String getMinerAddress(long instance, long blockHeight) throws RocksDBException, IOException, ExecutionException {

        String minerAddress = null;


        long scheduleHeight = calcSchedule(instance, blockHeight);


        MiningScheduleDAO miningScheduleDAO = new MiningScheduleDAO();
        MiningSchedule miningSchedule = miningScheduleDAO.find(instance, scheduleHeight);


        if (miningSchedule == null) {
            miningSchedule = miningScheduleDAO.find(CoreInstanceEnum.NAC.id, scheduleHeight);
        }

        if (miningSchedule != null) {

            minerAddress = miningSchedule.calcMinerAddress(blockHeight);
        }

        log.debug("[instance={}] MiningScheduleService.getMiner(instance={},blockHeight={}), minerAddress={}, scheduleHeight={}, miningSchedule={}", instance, instance, blockHeight, minerAddress, scheduleHeight, miningSchedule);

        return minerAddress;
    }


    public static MinerMember getMiner(long instance, long blockHeight) throws RocksDBException, IOException, ExecutionException {
        MinerMember minerMember = null;


        String minerAddress = getMinerAddress(instance, blockHeight);


        if (minerAddress != null) {
            minerMember = MinerMemberService.getMinerMember(minerAddress);
        }

        return minerMember;
    }


    public static MinerMember getDWorker(TxDas txDas) throws RocksDBException, IOException, ExecutionException {

        long schedule = txDas.getSchedule();


        MiningScheduleDAO miningScheduleDAO = new MiningScheduleDAO();
        MiningSchedule miningSchedule = miningScheduleDAO.find(txDas.getInstance(), schedule);


        MinerMember minerMember = null;
        if (miningSchedule != null) {

            String minerAddress = miningSchedule.calcDWorker();

            minerMember = MinerMemberService.getMinerMember(minerAddress);
        }

        return minerMember;
    }


    public static MiningSchedule getMiningSchedule(long instance, long schedule) throws RocksDBException, ExecutionException {
        MiningSchedule miningSchedule = miningScheduleDAO.find(instance, schedule);
        return miningSchedule;
    }


    public static List<MiningSchedule> createSchedule(long blockHeight, Key minerKey) throws Exception {
        List<MiningSchedule> miningScheduleList = Lists.newLinkedList();


        List<Instance> instanceList = InstanceSingleton.get().getInstanceList();


        for (Instance instance : instanceList) {

            if (instance.getId() == CoreInstanceEnum.APPCHAIN.id) {

                miningScheduleList.add(MiningScheduleService.createPoWFSchedule(blockHeight, minerKey));
            } else {

                miningScheduleList.add(MiningScheduleService.createDPoSSchedule(instance.getId(), blockHeight, minerKey));
            }
        }

        return miningScheduleList;
    }


    public static MiningSchedule createPoWFSchedule(long blockHeight, Key minerKey) throws Exception {

        long nextSchedule = MiningScheduleService.calcNextSchedule(CoreInstanceEnum.APPCHAIN.id, blockHeight);


        List<String> powfNodes = PoWFMinerHolder.get().nodesAddress();


        MiningSchedule miningSchedule = MiningScheduleService.newMiningSchedule(CoreInstanceEnum.APPCHAIN.id, nextSchedule, powfNodes, minerKey);

        log.debug("createPoWFSchedule() miningSchedule={}", miningSchedule);


        MiningScheduleDAO miningScheduleDAO = new MiningScheduleDAO();
        miningScheduleDAO.put(miningSchedule);

        return miningSchedule;
    }


    public static MiningSchedule createDPoSSchedule(long instanceId, long blockHeight, Key minerKey) throws Exception {

        long nextSchedule = MiningScheduleService.calcNextSchedule(instanceId, blockHeight);

        List<String> dposNodes = DPoSMinerHolder.get(instanceId).superNodesAddress();

        if (dposNodes.size() == 0) {
            dposNodes = DPoSMinerHolder.get(CoreInstanceEnum.NAC).superNodesAddress();
        }


        MiningSchedule miningSchedule = MiningScheduleService.newMiningSchedule(instanceId, nextSchedule, dposNodes, minerKey);

        log.debug("createDPoSSchedule() miningSchedule={}", miningSchedule);


        MiningScheduleDAO miningScheduleDAO = new MiningScheduleDAO();
        miningScheduleDAO.put(miningSchedule);

        return miningSchedule;
    }


}
