package com.common.workspace.vo;

import java.util.Date;

/**
 * @author: football98
 * @createTime: 16-6-16
 * @classDescription:vo
 */
public class DoneVo {
    private String executionid;
    private String procinstid;
    private String actname;
    private Date   endtime;

    public String getExecutionid() {
        return executionid;
    }

    public void setExecutionid(String executionid) {
        this.executionid = executionid;
    }

    public String getProcinstid() {
        return procinstid;
    }

    public void setProcinstid(String procinstid) {
        this.procinstid = procinstid;
    }

    public String getActname() {
        return actname;
    }

    public void setActname(String actname) {
        this.actname = actname;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }
}
