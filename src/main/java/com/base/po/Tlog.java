package com.base.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: po 类
 */
@Entity
@Table(name="t_log")
public class Tlog implements Serializable {

    private int tlogid;
    private String tlogmethod;
    private String tlogname;
    private String tloguser;
    private String tlogip;
    private Date tlogdate;
    private String tlogtime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getTlogid() {
        return tlogid;
    }

    public void setTlogid(int tlogid) {
        this.tlogid = tlogid;
    }

    public String getTlogmethod() {
        return tlogmethod;
    }

    public void setTlogmethod(String tlogmethod) {
        this.tlogmethod = tlogmethod;
    }

    public String getTlogname() {
        return tlogname;
    }

    public void setTlogname(String tlogname) {
        this.tlogname = tlogname;
    }

    public String getTloguser() {
        return tloguser;
    }

    public void setTloguser(String tloguser) {
        this.tloguser = tloguser;
    }

    public String getTlogip() {
        return tlogip;
    }

    public void setTlogip(String tlogip) {
        this.tlogip = tlogip;
    }

    public Date getTlogdate() {
        return tlogdate;
    }

    public void setTlogdate(Date tlogdate) {
        this.tlogdate = tlogdate;
    }

    public String getTlogtime() {
        return tlogtime;
    }

    public void setTlogtime(String tlogtime) {
        this.tlogtime = tlogtime;
    }
}