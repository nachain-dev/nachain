package org.nachain.core.networks.p2p.isc;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nachain.core.networks.p2p.isc.bean.Form;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.networks.p2p.isc.global.ISCConfig;
import org.nachain.libs.util.CommUtil;

import java.util.Map;


@Slf4j
public class SCService {


    public static ByteBuf toByteBuf(String data) {

        ByteBuf bb = Unpooled.copiedBuffer((data + ISCConfig.DELIMITER).getBytes());

        return bb;
    }


    public static IAction instanceAction(String actionClass) {
        IAction af = null;

        if (actionClass != null && (!actionClass.equals(""))) {
            try {
                af = (IAction) Class.forName(actionClass).newInstance();
            } catch (Exception e) {
                log.error("Action creation error" + actionClass, e);
            }
        }

        return af;
    }


    public static Form buildForm(String uri) {

        String url = "/" + CommUtil.contentSubstring(uri, 2, 0, "/", "");
        Form form = new Form();
        if (url.contains("?")) {
            String parameter = CommUtil.contentSubstring(url, "?", "");
            Map paraMap = CommUtil.analyzeParameter(parameter, "&");

            form.setTextElement(paraMap);
        } else {
            form.setTextElement(Maps.newHashMap());
        }

        return form;
    }


    public static Form buildFormByParam(String param) {

        Form form = new Form();

        if (StringUtils.isEmpty(param)) {
            form.setTextElement(Maps.newHashMap());
            return form;
        }

        String parameter = CommUtil.contentSubstring(param, "?", "");
        Map paraMap = CommUtil.analyzeParameter(parameter, "&");

        form.setTextElement(paraMap);

        return form;
    }


}