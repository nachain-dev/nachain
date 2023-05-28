package org.nachain.core.oracle.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.config.ChainConfig;
import org.nachain.core.oracle.Prediction;
import org.nachain.core.oracle.PredictionService;
import org.nachain.core.oracle.UnverifiedPredictionDAO;
import org.nachain.core.token.CoreTokenEnum;
import org.nachain.core.util.CommUtils;
import org.nachain.core.util.JsonUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@Slf4j
public class NacPriceService {


    public static final long COLLECT_BLOCK_CYCLE = 30;


    public static NacPrice toNacPrice(String jsonData) {
        return JsonUtils.jsonToPojo(jsonData, NacPrice.class);
    }


    public static NacPrice newNacPrice(BigDecimal price) {
        return newNacPrice(price, 0);
    }


    public static NacPrice newNacPrice(BigDecimal price, long blockHeight) {
        NacPrice priceData = new NacPrice();
        priceData.setPrice(price);
        priceData.setTimestamp(CommUtils.currentTimeMillis());
        priceData.setBlockHeight(blockHeight);

        return priceData;
    }


    private static double getNacPrice() {
        String url = ChainConfig.ORACLE_GATEWAY_DOMAIN + "/api/v1/getPrice";
        String coin = "NAC";

        String json = "";
        Double price = 0D;

        try {


            HttpsUrlValidator.retrieveResponseFromServer(url);


            Connection.Response executeResult = Jsoup.connect(url)
                    .ignoreHttpErrors(true).followRedirects(true)
                    .ignoreContentType(true)

                    .method(Connection.Method.GET).timeout(30000)
                    .execute();

            json = executeResult.body();


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            JsonNode data = root.get("data");

            for (int i = 0; i < data.size(); i++) {
                JsonNode item = data.get(i);

                String coinName = item.get("coinName").asText();
                Double coinPrice = item.get("coinPrice").asDouble();


                if (StringUtils.equals(coinName, coin)) {
                    price = coinPrice;
                    break;
                }
            }

        } catch (IOException e) {
            log.error("getNacPrice error : ", e);
        }

        return price;
    }


    public static BigDecimal calcPrediction(List<BigDecimal> list) {
        return CommUtils.countVotes(list);
    }


    public static Prediction doNacPrediction(String superNode, long blockHeight) throws Exception {

        BigDecimal price = BigDecimal.valueOf(NacPriceService.getNacPrice());

        log.debug("doNacPrediction(String superNode={}, long blockHeight={}), price={}", superNode, blockHeight, price);


        NacPrice nacPriceED = NacPriceService.newNacPrice(price, blockHeight);


        Prediction prediction = PredictionService.newPrediction(CoreInstanceEnum.APPCHAIN.id, CoreTokenEnum.NAC.id, OracleEventType.NAC_PRICE, nacPriceED, superNode);


        UnverifiedPredictionDAO unverifiedPredictionDAO = new UnverifiedPredictionDAO(CoreInstanceEnum.APPCHAIN.id);
        unverifiedPredictionDAO.add(prediction);

        return prediction;
    }


}
