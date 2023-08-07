package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.entify
 * @description: ${DESP}
 * @date: 2019/9/18
 * @time: 4:45 PM
 */
public class LongLonngBean {


    private List<OmmitQueueBean> ommitQueue;

    public List<OmmitQueueBean> getOmmitQueue() {
        return ommitQueue;
    }

    public void setOmmitQueue(List<OmmitQueueBean> ommitQueue) {
        this.ommitQueue = ommitQueue;
    }

    public static class OmmitQueueBean {
        /**
         * le : XJSSC
         * name : 仟位
         * names : 合
         * sortNum : 4
         */

        private String le;
        private String name;
        private String names;
        private int sortNum;

        public String getLe() {
            return le;
        }

        public void setLe(String le) {
            this.le = le;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNames() {
            return names;
        }

        public void setNames(String names) {
            this.names = names;
        }

        public int getSortNum() {
            return sortNum;
        }

        public void setSortNum(int sortNum) {
            this.sortNum = sortNum;
        }
    }
}
