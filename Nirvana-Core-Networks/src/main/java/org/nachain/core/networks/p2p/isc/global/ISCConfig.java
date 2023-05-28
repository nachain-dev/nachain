package org.nachain.core.networks.p2p.isc.global;


public class ISCConfig {


    public static final String LogName = "iSCCore";


    public static final String DELIMITER = "{$}";


    public static class ResponseInfo {


        public static int Response_Success_Code = 1;
        public static String Response_Success_Msg = "success";


        public static int Response_Fail_Code = -1;
        public static String Response_Fail_Msg = "failure";

    }


    public static class CmdValue {


        public static final int NetEnd = -1;


        public static final int QUERY_PASSWORD = 1000001;


        public static final int LOGIN = 1000002;


        public static final int REPLY_PASSWORD_ERROR = 1000003;


        public static final int REPLY_LOGIN_SUCCESSFUL = 1000004;


        public static final int LOGOUT = 1000005;


        public static final int REPLY_LOGIN_OFFLINE = 1000006;


        public static final int JOIN_GROUP = 1000010;


        public static final int EXIT_GROUP = 1000011;
    }

}