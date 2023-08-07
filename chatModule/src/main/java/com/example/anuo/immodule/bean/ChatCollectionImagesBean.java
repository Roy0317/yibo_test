package com.example.anuo.immodule.bean;

import com.example.anuo.immodule.bean.base.BaseBean;

import java.util.List;

/**
 * @author soxin
 * 2019年12月07日21:13:53
 */
public class ChatCollectionImagesBean extends BaseBean {


    /**
     * source : {"collectImageArray":["1912ca2c96ac5c1947559bf8f95160eac354"]}
     */


    private SourceBean source;

    public SourceBean getSource() {
        return source;
    }


    public void setSource(SourceBean source) {
        this.source = source;
    }

    public static class SourceBean {
        private String option; //1---查询用户收藏的图片 2--收藏图片 3--删除收藏的图片
        private List<String> collectImageArray;


        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public List<String> getCollectImageArray() {
            return collectImageArray;
        }

        public void setCollectImageArray(List<String> collectImageArray) {
            this.collectImageArray = collectImageArray;
        }
    }
}
