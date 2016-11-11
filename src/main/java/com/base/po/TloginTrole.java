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
@Table(name="t_logintrole")
public class TloginTrole  implements Serializable {

    private String tlogintroleid;
    private String tloginid;
    private String troleid;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    public String getTlogintroleid() {
        return tlogintroleid;
    }
    public void setTlogintroleid(String tlogintroleid) {
        this.tlogintroleid = tlogintroleid;
    }
    public String getTloginid() {
        return tloginid;
    }
    public void setTloginid(String tloginid) {
        this.tloginid = tloginid;
    }
    public String getTroleid() {
        return troleid;
    }
    public void setTroleid(String troleid) {
        this.troleid = troleid;
    }

}
