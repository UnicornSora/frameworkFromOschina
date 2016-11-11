package com.base.po;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: football98
 * @createTime:: 16-5-14
 * @classDescription: po 类
 */
@Entity
@Table(name="t_role")
public class Trole  implements Serializable {

    private String troleid;
    private String rolename;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    public String getTroleid() {
        return troleid;
    }

    public void setTroleid(String troleid) {
        this.troleid = troleid;
    }

    public String getRolename() {
        return this.rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
