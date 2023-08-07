package com.example.anuo.immodule.bean;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :24/06/2019
 * Desc  :com.example.anuo.immodule.bean
 */
public class AuthorityBean {

    /**
     * success : true
     * accessToken : 8b9a696d-1b0e-4c8d-9a04-4e7a777f43e9
     * content : {"appChatDomain":"https://api-chatha12iis.yibochat.com","fileChatDomain":"https://file-chatfile06.yibochat.com","encrypted":"WQNaunXZD2gjgzBusR6I%2BkJmv5lAvMIo%2FLnCVVG6saA%2BFU6inA6MwKAU%2BERt4qI70Atq%2BiCM6ygWdHeXuQObCrTXs6GPchUHG8LDRNokFJ65w6rnnnHOGdzHo0YMCjKTGFUJp5W7YEoV2Zou5AyjDinz0n%2B9d9DvY016wAeZ%2F4BDR%2BBc1dfz8SeCabYRXWI%2BWMDQUX5pIRQQfBXFHYmpgRuMIexcoj9ZuMID2V%2FIW2I%3D","sign":"d2caecd87b59b1ce47ca3d570bb8fbba","clusterId":"a082"}
     */

    private boolean success;
    private String accessToken;
    private ContentBean content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * appChatDomain : https://api-chatha12iis.yibochat.com
         * fileChatDomain : https://file-chatfile06.yibochat.com
         * encrypted : WQNaunXZD2gjgzBusR6I%2BkJmv5lAvMIo%2FLnCVVG6saA%2BFU6inA6MwKAU%2BERt4qI70Atq%2BiCM6ygWdHeXuQObCrTXs6GPchUHG8LDRNokFJ65w6rnnnHOGdzHo0YMCjKTGFUJp5W7YEoV2Zou5AyjDinz0n%2B9d9DvY016wAeZ%2F4BDR%2BBc1dfz8SeCabYRXWI%2BWMDQUX5pIRQQfBXFHYmpgRuMIexcoj9ZuMID2V%2FIW2I%3D
         * sign : d2caecd87b59b1ce47ca3d570bb8fbba
         * clusterId : a082
         */

        private String appChatDomain;
        private String fileChatDomain;
        private String encrypted;
        private String sign;
        private String clusterId;

        public String getAppChatDomain() {
            return appChatDomain;
        }

        public void setAppChatDomain(String appChatDomain) {
            this.appChatDomain = appChatDomain;
        }

        public String getFileChatDomain() {
            return fileChatDomain;
        }

        public void setFileChatDomain(String fileChatDomain) {
            this.fileChatDomain = fileChatDomain;
        }

        public String getEncrypted() {
            return encrypted;
        }

        public void setEncrypted(String encrypted) {
            this.encrypted = encrypted;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getClusterId() {
            return clusterId;
        }

        public void setClusterId(String clusterId) {
            this.clusterId = clusterId;
        }
    }
}
