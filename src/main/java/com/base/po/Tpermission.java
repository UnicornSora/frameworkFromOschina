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
@Table(name="t_permission")
public class Tpermission  implements Serializable {

    private String tpermissionid;
    private String permissionname;
    private int    action;
    private String url;
    private String openicon;
    private String closeicon;
    private String parentid;
    private int    orders;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    public String getTpermissionid() {
        return tpermissionid;
    }

    public void setTpermissionid(String tpermissionid) {
        this.tpermissionid = tpermissionid;
    }

    public String getPermissionname() {
        return permissionname;
    }

    public void setPermissionname(String permissionname) {
        this.permissionname = permissionname;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOpenicon() {
        return openicon;
    }

    public void setOpenicon(String openicon) {
        this.openicon = openicon;
    }

    public String getCloseicon() {
        return closeicon;
    }

    public void setCloseicon(String closeicon) {
        this.closeicon = closeicon;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

}
