package com.common.security.vo;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:权限树
 */
public class TreeAttr {
    private String url;
    private boolean isleaf;
    private String parentid;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean isIsleaf() {
        return isleaf;
    }
    public void setIsleaf(boolean isleaf) {
        this.isleaf = isleaf;
    }
    public String getParentid() {
        return parentid;
    }
    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

}