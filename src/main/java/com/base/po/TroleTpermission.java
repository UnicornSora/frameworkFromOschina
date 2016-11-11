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
@Table(name="t_roletpermission")
public class TroleTpermission implements Serializable {

    private String troleTpermissionid;
    private String troleid;
    private String tpermissionid;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    public String getTroleTpermissionid() {
        return troleTpermissionid;
    }
    public void setTroleTpermissionid(String troleTpermissionid) {
        this.troleTpermissionid = troleTpermissionid;
    }
    public String getTroleid() {
        return troleid;
    }
    public void setTroleid(String troleid) {
        this.troleid = troleid;
    }
    public String getTpermissionid() {
        return tpermissionid;
    }
    public void setTpermissionid(String tpermissionid) {
        this.tpermissionid = tpermissionid;
    }
}
