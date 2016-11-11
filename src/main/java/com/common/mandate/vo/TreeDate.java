package com.common.mandate.vo;

import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: 单位信息树vo
 */
public class TreeDate {
    private String id ;
    private String text;
    private List<TreeDate> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TreeDate> getChildren() {
        return children;
    }

    public void setChildren(List<TreeDate> children) {
        this.children = children;
    }
}
