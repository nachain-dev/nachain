package org.nachain.core.dapp.internal.swap;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.Feedback;
import org.nachain.core.chain.FeedbackService;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.instanceprofile.InstanceProfile;
import org.nachain.core.chain.instanceprofile.InstanceProfileService;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxReservedWord;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.dapp.IAction;
import org.nachain.core.dapp.internal.swap.amm.*;
import org.nachain.core.dapp.internal.swap.amm.dto.BuyDTO;
import org.nachain.core.dapp.internal.swap.amm.dto.LiquidityAddDTO;
import org.nachain.core.dapp.internal.swap.amm.dto.SellDTO;
import org.nachain.core.dapp.internal.swap.amm.dto.SwapPairDeployDTO;
import org.nachain.core.dapp.internal.swap.coinage.Coinage;
import org.nachain.core.dapp.internal.swap.coinage.CoinageDAO;
import org.nachain.core.dapp.internal.swap.coinage.CoinagePriceType;
import org.nachain.core.dapp.internal.swap.coinage.CoinageService;
import org.nachain.core.dapp.internal.swap.coinage.dto.CoinageAddDTO;
import org.nachain.core.dapp.internal.swap.coinage.dto.CoinageBuyDTO;
import org.nachain.core.dapp.internal.swap.coinage.dto.CoinageDeployDTO;
import org.nachain.core.token.TokenSingleton;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


@Slf4j
public class SwapProcessor implements IAction {


    @Override
    public Feedback handler(Tx tx, TxContext txContext) {
        Feedback feedback = null;


        if (txContext.getEventType() == TxEventType.SWAP_ACTION) {

            long blockHeight = tx.getBlockHeight();
            long txTimestamp = tx.getTimestamp();

            ISwapAction swapAction = (ISwapAction) txContext.getData();
            switch (swapAction.getAction()) {

                case SWAP_PAIR_DEPLOY:

                    SwapPairDeployDTO pairDeployDTO = (SwapPairDeployDTO) txContext.getData();
                    feedback = SWAP_PAIR_DEPLOY(pairDeployDTO.getSwapPair());
                    break;


                case AMM_LIQUIDITY_ADD:

                    LiquidityAddDTO liquidityAddDTO = (LiquidityAddDTO) txContext.getData();
                    feedback = AMM_LIQUIDITY_ADD(liquidityAddDTO.getLiquidityProvider());
                    break;

                case AMM_LIQUIDITY_REMOVE:
                    feedback = AMM_LIQUIDITY_REMOVE(txContext);
                    break;

                case AMM_BUY:
                    BuyDTO buyDTO = (BuyDTO) txContext.getData();
                    feedback = AMM_BUY(buyDTO);
                    break;

                case AMM_SELL:
                    SellDTO sellDTO = (SellDTO) txContext.getData();
                    feedback = AMM_SELL(sellDTO);
                    break;


                case COINAGE_DEPLOY:
                    CoinageDeployDTO coinageDeployDTO = (CoinageDeployDTO) txContext.getData();
                    feedback = COINAGE_DEPLOY(coinageDeployDTO);
                    break;

                case COINAGE_ADD:
                    CoinageAddDTO coinageAddDTO = (CoinageAddDTO) txContext.getData();
                    feedback = COINAGE_ADD(tx.getFrom(), tx.getInstance(), tx.getToken(), coinageAddDTO, blockHeight, txTimestamp);
                    break;

                case COINAGE_BUY:
                    CoinageBuyDTO coinageBuyDTO = (CoinageBuyDTO) txContext.getData();
                    feedback = COINAGE_BUY(tx, coinageBuyDTO);
                    break;
                default:
                    feedback = FeedbackService.newFailFeedback(String.format("Find not action %s", swapAction.getAction()));
                    break;
            }
        }


        return feedback;
    }


    private Feedback SWAP_PAIR_DEPLOY(SwapPair swapPair) {

        try {

            String pairName = swapPair.getPairName();

            SwapType swapType = swapPair.getSwapType();

            SwapPairDAO swapPairDAO = new SwapPairDAO();

            boolean success = swapPairDAO.add(swapPair);
            if (success) {

                SwapPairSingleton.get().addToMap(swapPair);
            } else {
                return FeedbackService.newFailFeedback(String.format("Pair %s/%s failed or already exists.", pairName, swapType));
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FeedbackService.newFeedback();
    }


    private Feedback AMM_LIQUIDITY_ADD(LiquidityProvider liquidityProvider) {
        try {

            LiquidityProviderDAO liquidityProviderDAO = new LiquidityProviderDAO();
            liquidityProviderDAO.add(liquidityProvider);


        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return FeedbackService.newFeedback();
    }


    private Feedback AMM_LIQUIDITY_REMOVE(TxContext txContext) {

        return FeedbackService.newFeedback();
    }


    private Feedback AMM_BUY(BuyDTO buyDTO) {


        return FeedbackService.newFeedback();
    }


    private Feedback AMM_SELL(SellDTO sellDTO) {


        return FeedbackService.newFeedback();
    }


    private Feedback COINAGE_DEPLOY(CoinageDeployDTO deployDTO) {


        CoinagePriceType coinagePriceType = deployDTO.getCoinagePriceType();


        BigInteger fixedPrice = deployDTO.getFixedPrice();


        Coinage coinage = CoinageService.newCoinage(deployDTO.getBase(), deployDTO.getQuote(), deployDTO.getBuyFee(), coinagePriceType, fixedPrice);
        try {
            CoinageService.coinageDAO.add(coinage);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }

        return FeedbackService.newFeedback();
    }


    private Feedback COINAGE_ADD(String address, long instance, long token, CoinageAddDTO addDTO, long blockHeight, long txTimestamp) {

        String coinageName = addDTO.getCoinageName();
        String base = addDTO.getBase();
        BigInteger amount = addDTO.getAmount();


        if (!PairService.getBaseName(coinageName).equals(base)) {
            throw new ChainException(String.format("Parameter error: CoinageName=%s, baseName=%s, Not equals.", coinageName, base));
        }


        InstanceProfile instanceProfile;
        try {
            instanceProfile = InstanceProfileService.getInstanceProfile(address, instance, token);
            if (instanceProfile.getBalance().compareTo(amount) == -1) {
                throw new ChainException(String.format("Instance token not enough. CoinageAdd.amount=%s, Instance.balance=%s", amount, instanceProfile.getBalance()));
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {

            InstanceProfileService.transferInstanceProfile(instance, token, amount, address, TxReservedWord.INSTANCE.name, blockHeight, txTimestamp);


            Coinage coinage = CoinageService.coinageDAO.get(coinageName);
            coinage.setBaseAmount(coinage.getBaseAmount().add(amount));
            CoinageService.coinageDAO.edit(coinage);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FeedbackService.newFeedback();
    }


    private Feedback COINAGE_BUY(Tx tx, CoinageBuyDTO buyDTO) {

        String coinageName = buyDTO.getCoinageName();

        BigInteger payQuoteAmount = buyDTO.getPayQuoteAmount();

        BigInteger buyBaseAmount = buyDTO.getBuyBaseAmount();

        double slippage = buyDTO.getSlippage();


        long instanceId = CoreInstanceEnum.SWAP.id;
        String fromAddress = tx.getFrom();
        long blockHeight = tx.getBlockHeight();
        long txTimestamp = tx.getTimestamp();

        try {
            CoinageDAO coinageDAO = new CoinageDAO();
            Coinage coinage = coinageDAO.get(coinageName);
            if (coinage != null) {

                String baseName = PairService.getBaseName(coinageName);

                String quoteName = PairService.getQuoteName(coinageName);

                BigInteger txPayValue = tx.getValue();

                long baseTokenId = TokenSingleton.get().getTokenSymbolMap().get(baseName);
                long quoteTokenId = TokenSingleton.get().getTokenSymbolMap().get(quoteName);


                InstanceProfile instanceProfile = InstanceProfileService.getInstanceProfile(fromAddress, instanceId, quoteTokenId);
                if (instanceProfile == null) {
                    throw new ChainException(String.format("InstanceProfile is null. address=%s", fromAddress));
                }


                if (!InstanceProfileService.checkBalanceEnough(fromAddress, instanceId, quoteTokenId, payQuoteAmount)) {
                    throw new ChainException(String.format("InstanceProfile balance not enough. address=%s", fromAddress));
                }


                CoinagePriceType coinagePriceType = coinage.getCoinagePriceType();

                BigInteger lastPrice = BigInteger.ZERO;
                switch (coinagePriceType) {

                    case SWAP_AMM:

                        AmmManager ammManager = AmmHolder.getPair(coinageName);

                        lastPrice = ammManager.getAmm().getLastBasePrice().toBigInteger();
                        break;

                    case FIXED_PRICE:

                        lastPrice = coinage.getFixedPrice();
                        break;

                    case ENTRUST:

                        break;
                    default:
                }


                BigInteger baseAmount = txPayValue.divide(lastPrice);


                if (slippage > 0) {
                    log.debug("buyBaseAmount:" + buyBaseAmount + ", baseAmount:" + baseAmount);

                    if (buyBaseAmount.compareTo(baseAmount) == 1) {

                        BigDecimal differenceAmount = new BigDecimal(buyBaseAmount.subtract(baseAmount));

                        BigDecimal differencePercent = differenceAmount.divide(new BigDecimal(buyBaseAmount), AmmService.SCALE, AmmService.ROUNDING_MODE);
                        log.debug("differencePercent:" + differencePercent);

                        if (BigDecimal.valueOf(slippage).compareTo(differencePercent) == -1) {
                            throw new ChainException(String.format("The slippage value is exceeded. slippage=%f, differencePercent=%f", slippage, differencePercent));
                        }
                    }
                }


                if (baseAmount.compareTo(BigInteger.ZERO) == 1) {

                    InstanceProfileService.transferInstanceProfile(instanceId, quoteTokenId, payQuoteAmount, fromAddress, TxReservedWord.INSTANCE.name, blockHeight, txTimestamp);


                    InstanceProfileService.transferInstanceProfile(instanceId, baseTokenId, baseAmount, TxReservedWord.INSTANCE.name, fromAddress, blockHeight, txTimestamp);
                }
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FeedbackService.newFeedback();
    }

}
