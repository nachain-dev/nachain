package org.nachain.core.oracle;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.base.Amount;
import org.nachain.core.chain.config.PricingSystem;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceDetailService;
import org.nachain.core.config.miner.Mining;
import org.nachain.core.crypto.Key;
import org.nachain.core.oracle.events.IEventData;
import org.nachain.core.oracle.events.NacPrice;
import org.nachain.core.oracle.events.NacPriceService;
import org.nachain.core.oracle.events.OracleEventType;
import org.nachain.core.signverify.SignVerify;
import org.nachain.core.token.CoreTokenEnum;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
public class PredictedResultService {


    public static PredictedResult toPredictedResult(String json) {
        return JsonUtils.jsonToPojo(json, PredictedResult.class);
    }


    public static PredictedResult newPredictedResult(long instance, long token, OracleEventType eventType, IEventData resultData, List<String> predictedIDs, long blockHeight, Key minerKey) throws Exception {
        PredictedResult predictedResult = new PredictedResult();
        predictedResult.setInstance(instance);
        predictedResult.setToken(token);
        predictedResult.setEventType(eventType.id);
        predictedResult.setResultData(resultData.toJson());
        predictedResult.setPredictedIDs(predictedIDs);

        predictedResult.setBlockHeight(blockHeight);

        predictedResult.setMinedSign(SignVerify.signHexString(predictedResult, minerKey));

        return predictedResult;
    }


    public static long calcSchedule(long blockHeight) {

        long collectBlockCycle = NacPriceService.COLLECT_BLOCK_CYCLE;


        int cycleTotal = (int) (blockHeight / collectBlockCycle);


        long schedule = cycleTotal * NacPriceService.COLLECT_BLOCK_CYCLE;

        if (schedule == 0) {
            schedule = 1;
        }

        return schedule;
    }


    public static List<BigDecimal> getReferenceList(long instance, long blockHeight) throws RocksDBException, IOException {

        PredictedResultDAO predictedResultDAO = new PredictedResultDAO(instance);

        long prevBlockHeight = calcSchedule(blockHeight);

        List<BigDecimal> priceList = new ArrayList<>();

        for (int i = 1; i <= PricingSystem.Slippage.REFERENCE_NUMBER; i++) {

            PredictedResult pr = predictedResultDAO.get(prevBlockHeight);
            if (pr != null) {
                NacPrice np = NacPriceService.toNacPrice(pr.getResultData());

                priceList.add(np.getPrice());
            } else {

                break;
            }


            prevBlockHeight = prevBlockHeight - NacPriceService.COLLECT_BLOCK_CYCLE;
        }

        return priceList;
    }


    public static NacPrice minReferenceNacPrice(long blockHeight) throws RocksDBException, IOException {
        NacPrice nacPrice = null;

        List<BigDecimal> priceList = PredictedResultService.getReferenceList(CoreInstanceEnum.APPCHAIN.id, blockHeight);

        if (priceList.size() > 0) {

            Collections.sort(priceList);

            nacPrice = NacPriceService.newNacPrice(priceList.get(0), blockHeight);
        }

        return nacPrice;
    }


    public static NacPrice maxReferenceNacPrice(long blockHeight) throws RocksDBException, IOException {
        NacPrice nacPrice = null;

        List<BigDecimal> priceList = PredictedResultService.getReferenceList(CoreInstanceEnum.APPCHAIN.id, blockHeight);

        if (priceList.size() > 0) {

            Collections.sort(priceList);

            Collections.reverse(priceList);

            nacPrice = NacPriceService.newNacPrice(priceList.get(0), blockHeight);
        }

        return nacPrice;
    }


    public static NacPrice getNacPrice(long blockHeight) throws Exception {
        NacPrice nacPrice;

        PredictedResultDAO predictedResultDAO = new PredictedResultDAO(CoreInstanceEnum.APPCHAIN.id);


        long schedule = PredictedResultService.calcSchedule(blockHeight);


        PredictedResult predictedResult = predictedResultDAO.get(schedule);
        if (predictedResult != null) {
            nacPrice = NacPriceService.toNacPrice(predictedResult.getResultData());
        } else {
            nacPrice = NacPriceService.newNacPrice(BigDecimal.ZERO, blockHeight);
        }

        return nacPrice;
    }


    public static BigInteger calcNacAmount(long blockHeight, boolean isMin, long usd) throws Exception {
        BigInteger nacAmount = BigInteger.ZERO;


        NacPrice nacPrice;
        if (isMin) {

            nacPrice = maxReferenceNacPrice(blockHeight);
        } else {
            nacPrice = getNacPrice(blockHeight);
        }


        nacAmount = calcNacAmount(nacPrice.getPrice(), usd);

        return nacAmount;
    }


    public static BigInteger calcNacAmount(BigDecimal nacPrice, long usd) {
        BigInteger nacAmount = BigInteger.ZERO;


        if (nacPrice.compareTo(BigDecimal.ZERO) == 1) {
            nacAmount = new BigDecimal(usd).divide(nacPrice, Amount.SCALE, Amount.ROUNDING_MODE).toBigInteger();
        }

        return nacAmount;
    }


    public static BigInteger calcNacAmount(long usdPrice) throws Exception {
        long blockHeight = InstanceDetailService.getBlockHeight(CoreInstanceEnum.APPCHAIN.id);
        return calcNacAmount(blockHeight, false, usdPrice);
    }


    public static BigInteger calcDeployNacAmount() throws Exception {
        long blockHeight = InstanceDetailService.getBlockHeight(CoreInstanceEnum.APPCHAIN.id);
        return calcNacAmount(blockHeight, false, PricingSystem.AppChain.DEPLOY_INSTANCE_USDN_PRICE);
    }


    public static BigInteger calcMinDeployNacAmount() throws Exception {
        long blockHeight = InstanceDetailService.getBlockHeight(CoreInstanceEnum.APPCHAIN.id);
        return calcNacAmount(blockHeight, true, PricingSystem.AppChain.DEPLOY_INSTANCE_USDN_PRICE);
    }


    public static BigInteger calcTokenIconNacAmount() throws Exception {
        long blockHeight = InstanceDetailService.getBlockHeight(CoreInstanceEnum.APPCHAIN.id);
        return calcNacAmount(blockHeight, false, PricingSystem.AppChain.TOKEN_ICON_USDN_PRICE);
    }


    public static BigInteger calcMinTokenIconNacAmount() throws Exception {
        long blockHeight = InstanceDetailService.getBlockHeight(CoreInstanceEnum.APPCHAIN.id);
        return calcNacAmount(blockHeight, true, PricingSystem.AppChain.TOKEN_ICON_USDN_PRICE);
    }


    public static PredictedResult doPredictedResult(long blockHeight) throws Exception {
        PredictedResult predictedResult = null;


        Key minerKey = Mining.getKey();

        UnverifiedPredictionDAO unverifiedPredictionDAO = new UnverifiedPredictionDAO(CoreInstanceEnum.APPCHAIN.id);
        PredictionDAO predictionDAO = new PredictionDAO(CoreInstanceEnum.APPCHAIN.id);
        PredictedResultDAO predictedResultDAO = new PredictedResultDAO(CoreInstanceEnum.APPCHAIN.id);


        List<Prediction> unverifiedPredictionList = unverifiedPredictionDAO.db().findAll(Prediction.class);

        log.debug("Find UnverifiedPrediction list size:" + unverifiedPredictionList.size());


        List<BigDecimal> priceList = new ArrayList<>();
        List<String> predictedIDs = new ArrayList<>();

        for (Prediction unPre : unverifiedPredictionList) {
            if (unPre.getEventType() == OracleEventType.NAC_PRICE.id) {

                NacPrice nacPriceED = NacPriceService.toNacPrice(unPre.getEventData());


                priceList.add(nacPriceED.getPrice());

                predictedIDs.add(unPre.getHash());


                unverifiedPredictionDAO.delete(unPre.getHash());


                unPre.setMinedSign(SignVerify.signHexString(unPre, minerKey));


                predictionDAO.add(unPre);
            }
        }

        log.info("Find price list size:" + priceList.size());


        BigDecimal resultPrice = NacPriceService.calcPrediction(priceList);


        if (resultPrice.compareTo(BigDecimal.ZERO) == 1) {

            NacPrice nacPriceED = NacPriceService.newNacPrice(resultPrice, blockHeight);


            predictedResult = PredictedResultService.newPredictedResult(CoreInstanceEnum.APPCHAIN.id, CoreTokenEnum.NAC.id, OracleEventType.NAC_PRICE, nacPriceED, predictedIDs, blockHeight, minerKey);
            predictedResultDAO.add(predictedResult);

            log.info("predictedResult:" + predictedResult);
        }

        return predictedResult;
    }


    public static BigDecimal toNacPrice(PredictedResult predictedResult) {
        NacPrice priceED = NacPriceService.toNacPrice(predictedResult.getResultData());
        return priceED.getPrice();
    }
}
