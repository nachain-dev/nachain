package org.nachain.core.config;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class ProfileService {


    public static final String PROFILES_ACTIVE_KEY_NAME;

    static {
        PROFILES_ACTIVE_KEY_NAME = "project.profiles.active";


        String profileName = "";
        try {
            Properties properties = ConfigService.getProperties("profile.properties");
            profileName = properties.getProperty(PROFILES_ACTIVE_KEY_NAME, "");

            active(profileName);
        } finally {
            log.info("{}, profileName={}", PROFILES_ACTIVE_KEY_NAME, profileName);
        }
    }


    public static final String getProfile() {
        String profile = System.getProperty(PROFILES_ACTIVE_KEY_NAME, "");
        if (!profile.isEmpty()) {
            profile = "-" + profile;
        }
        return profile;
    }


    public static final String getConfigProfile() {
        return "config" + ProfileService.getProfile();
    }


    @Deprecated
    public static boolean switchProfile(String profileName) {
        active(profileName);
        return true;
    }


    public static boolean active(String profileName) {
        System.setProperty(PROFILES_ACTIVE_KEY_NAME, profileName);
        return true;
    }
}
