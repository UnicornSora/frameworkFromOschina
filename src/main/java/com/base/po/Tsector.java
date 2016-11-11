package com.base.po;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: po 类
 */
@Entity
@Table(name="t_sector")
public class Tsector implements Serializable {

    private String tdepartmentid;
    private String tsectorid;
    private String tsectorname;
    private String linkman;
    private String phone;


    public String getTdepartmentid(){
        return tdepartmentid;
    }

    public void setTdepartmentid(String tdepartmentid){
        this.tdepartmentid = tdepartmentid;
    }

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    public String getTsectorid(){
        return tsectorid;
    }

    public void setTsectorid(String tsectorid){
        this.tsectorid = tsectorid;
    }

    public String getTsectorname(){
        return tsectorname;
    }

    public void setTsectorname(String tsectorname){
        this.tsectorname = tsectorname;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}