package com.base.utils;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: dategrid 分页辅助对象
 */
public class Page {
    //当前页数
    private int intPage = 1;
    //总页数
    private int pageCount = 0;
    //每页要显示的记录数
    private int pageInfoCount = 10;
    //总记录数
    private int infoCount = 0;
    public int getIntPage() {
        return intPage;
    }
    public void setIntPage(int intPage) {
        this.intPage = intPage;
    }
    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public int getPageInfoCount() {
        return pageInfoCount;
    }
    public void setPageInfoCount(int pageInfoCount) {
        this.pageInfoCount = pageInfoCount;
    }
    public int getInfoCount() {
        return infoCount;
    }
    public void setInfoCount(int infoCount) {
        this.infoCount = infoCount;
    }
    //启始位置
    public int getStartInfo(){
        return pageInfoCount * (intPage - 1) ;
    }

}
