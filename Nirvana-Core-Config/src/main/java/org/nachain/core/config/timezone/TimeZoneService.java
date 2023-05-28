package org.nachain.core.config.timezone;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nachain.core.config.Constants;

import java.util.TimeZone;

@Slf4j
public class TimeZoneService {


    public static final String IS_DISABLE_TIME_ZONE = "NaChain.IsDisableTimeZone";


    public static void disabled() {

        System.setProperty(IS_DISABLE_TIME_ZONE, "true");
    }


    public static void enabled() {

        System.setProperty(IS_DISABLE_TIME_ZONE, "false");

        setDefault();
    }


    public static void setDefault() {

        String isDisableTimeZone = System.getProperty(IS_DISABLE_TIME_ZONE);


        if (StringUtils.isEmpty(isDisableTimeZone) || Boolean.FALSE.toString().equals(isDisableTimeZone.toLowerCase())) {

            TimeZone.setDefault(TimeZone.getDefault().getTimeZone(Constants.DEFAULT_TIMEZONE));

            log.info("Default Time zone={}", Constants.DEFAULT_TIMEZONE);
        }
    }

}
