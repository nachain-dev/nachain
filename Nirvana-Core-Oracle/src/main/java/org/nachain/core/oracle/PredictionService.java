package org.nachain.core.oracle;

import org.nachain.core.oracle.events.IEventData;
import org.nachain.core.oracle.events.OracleEventType;
import org.nachain.core.util.JsonUtils;

public class PredictionService {


    public static Prediction newPrediction(long instance, long token, OracleEventType eventType, IEventData eventData, String superNode) throws Exception {
        Prediction pr = new Prediction();
        pr.setInstance(instance);
        pr.setToken(token);
        pr.setEventType(eventType.id);
        pr.setEventData(JsonUtils.objectToJson(eventData));
        pr.setSuperNode(superNode);
        pr.setHash(pr.encodeHashString());
        pr.setMinedSign(pr.toMinedSignString());

        return pr;
    }
}
