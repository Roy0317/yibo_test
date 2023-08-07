package com.yibo.yiboapp.data;

import java.util.List;

/**
 * Created by johnson on 2017/9/26.
 */

public class PeilvData {
    String tagName;
    String postTagName;//下注时提交的号码前缀名称
    boolean appendTag;//号码是否要加上tagName
    boolean appendTagToTail;//号码是否要加上tagName,且添加到尾部
    List<PeilvPlayData> subData;

    public boolean isAppendTagToTail() {
        return appendTagToTail;
    }

    public void setAppendTagToTail(boolean appendTagToTail) {
        this.appendTagToTail = appendTagToTail;
    }

    public String getPostTagName() {
        return postTagName;
    }

    public void setPostTagName(String postTagName) {
        this.postTagName = postTagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<PeilvPlayData> getSubData() {
        return subData;
    }

    public void setSubData(List<PeilvPlayData> subData) {
        this.subData = subData;
    }

    public boolean isAppendTag() {
        return appendTag;
    }

    public void setAppendTag(boolean appendTag) {
        this.appendTag = appendTag;
    }
}
