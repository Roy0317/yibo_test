package com.yibo.yiboapp.entify;

import java.io.Serializable;

public class ClientIpBean implements Serializable {

    /**
     * ip2 : 61.222.27.151
     * ip1 : *.*.*.*
     */

    private String ip2;
    private String ip1;

    public String getIp2() {
        return ip2;
    }

    public void setIp2(String ip2) {
        this.ip2 = ip2;
    }

    public String getIp1() {
        return ip1;
    }

    public void setIp1(String ip1) {
        this.ip1 = ip1;
    }
}
