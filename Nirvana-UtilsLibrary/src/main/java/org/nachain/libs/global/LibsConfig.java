package org.nachain.libs.global;

import org.nachain.libs.config.ConfigFile;
import org.nachain.libs.config.ConfigSRC;
import org.nachain.libs.util.CommUtil;


public class LibsConfig extends ConfigSRC {

    public static String Md5PswSalt = "";


    private static LibsConfig ourInstance = new LibsConfig();

    public static final String CompanyName = "NaChain";

    public static final String AppName = "Utils-Libs";

    public static final String AppVersion = "1.0.0";


    public static final String LogName = "Libs";


    public static LibsConfig me() {
        return ourInstance;
    }


    @Override
    public void initConfig(ConfigFile configFile) {
        Md5PswSalt = CommUtil.null2String(configFile.getValue("Md5PswSalt", ""));
    }


    @Override
    public String getConfigFileName() {
        return "libs.config";
    }


}