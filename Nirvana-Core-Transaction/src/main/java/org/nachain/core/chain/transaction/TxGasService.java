package org.nachain.core.chain.transaction;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.config.PricingSystem;
import org.nachain.core.chain.structure.instance.ChainTypeEnum;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.oracle.config.OracleConfigDAO;
import org.nachain.core.oracle.events.NacPrice;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;


@Slf4j
public class TxGasService {


    private static ChainTypeEnum getChainType(long instanceId) {
        ChainTypeEnum chainTypeEnum;


        if (instanceId == CoreInstanceEnum.APPCHAIN.id) {
            chainTypeEnum = ChainTypeEnum.PoWF;
        } else {
            chainTypeEnum = ChainTypeEnum.DPoS;
        }

        return chainTypeEnum;
    }


    public static BigInteger calcGasAmount(ChainTypeEnum chainType) throws RocksDBException, IOException, ExecutionException {
        return calcGasAmount(chainType, false);
    }


    public static BigInteger calcGasAmount(ChainTypeEnum chainType, boolean isMinimum) throws RocksDBException, IOException, ExecutionException {
        BigInteger rtv = BigInteger.ZERO;

        switch (chainType) {
            case PoWF:
                rtv = calcNacGasAmountByPoWF(isMinimum);
                break;
            case DPoS:
                rtv = calcNacGasAmountByDPoS(isMinimum);
                break;
            case DAS:
                rtv = calcNacGasAmountByDAS(isMinimum);
                break;
        }

        return rtv;
    }


    public static BigInteger calcGasAmount(long instanceId) throws RocksDBException, IOException, ExecutionException {
        return calcGasAmount(instanceId, false);
    }


    public static BigInteger calcGasAmount(long instanceId, boolean isMinimum) throws RocksDBException, IOException, ExecutionException {

        ChainTypeEnum chainTypeEnum = getChainType(instanceId);

        return calcGasAmount(chainTypeEnum, isMinimum);
    }


    private static BigInteger calcNacGasAmountByPoWF(boolean isMinimum) throws RocksDBException, IOException, ExecutionException {
        return calcNacGasEstimatedAmount(PricingSystem.Gas.GAS_POWF_USDN, isMinimum);
    }


    private static BigInteger calcNacGasAmountByDPoS(boolean isMinimum) throws RocksDBException, IOException, ExecutionException {
        return calcNacGasEstimatedAmount(PricingSystem.Gas.GAS_DPOS_USDN, isMinimum);
    }


    private static BigInteger calcNacGasAmountByDAS(boolean isMinimum) throws RocksDBException, IOException, ExecutionException {
        return calcNacGasEstimatedAmount(PricingSystem.Gas.GAS_DAS_USDN, isMinimum);
    }


    private static BigInteger calcNacGasEstimatedAmount(double gasUSDN, boolean isMinimum) throws RocksDBException, IOException, ExecutionException {

        BigDecimal usdnAmount = BigDecimal.valueOf(gasUSDN);


        OracleConfigDAO configDAO = new OracleConfigDAO(CoreInstanceEnum.APPCHAIN.id);


        NacPrice price;


        if (isMinimum) {

            price = configDAO.getOracleNacPrice();
        } else {

            price = configDAO.getOracleMinimumNacPrice();
        }

        if (price == null) {
            return BigInteger.ZERO;
        }


        BigDecimal nacPrice = price.getPrice();


        BigDecimal estimatedAmount = BigDecimal.ONE.divide(nacPrice, Unit.NAC.exp, Amount.ROUNDING_MODE);


        estimatedAmount = estimatedAmount.multiply(usdnAmount);


        BigInteger estimatedAmountUnit = Amount.of(estimatedAmount, Unit.NAC).toBigInteger();

        log.debug("Estimated amount unit:" + estimatedAmountUnit + ",nacPrice=" + nacPrice + ",usdnAmount=" + usdnAmount + "," + new BigDecimal(estimatedAmountUnit).multiply(nacPrice));

        return estimatedAmountUnit;
    }


}
