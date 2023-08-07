package com.example.anuo.immodule.jsonmodel;

public class ChatCollectionImagesModel extends BaseJsonModel {

    private String option;//1---查询用户收藏的图片 2--收藏图片 3--删除收藏的图片
    private String imageIds;//对应字段 record 就是服务返回的url / / 最后面的
    private String imageId;
    private String userId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getImageIds() {
        return imageIds;
    }

    public void setImageIds(String imageIds) {
        this.imageIds = imageIds;
    }
}
