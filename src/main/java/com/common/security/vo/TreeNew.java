package com.common.security.vo;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:权限树
 */
public class TreeNew {
    private String id;
    private String text;
    private String state;
    private Object attributes;
    private boolean checked;
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
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public Object getAttributes() {
        return attributes;
    }
    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}