package com.common.security.vo;

import java.util.Date;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:用户信息vo
 */
public class TloginVo {
    private String tloginid ;
    private String loginname ;
    private String username ;
    private String phone ;
    private String registrationuser ;
    private Date   registrationtime ;
    private String status ;
    private String troleid ;
    private String sector;

    public String getTloginid() {
        return tloginid;
    }
    public void setTloginid(String tloginid) {
        this.tloginid = tloginid;
    }
    public String getLoginname() {
        return loginname;
    }
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegistrationuser() {
        return registrationuser;
    }
    public void setRegistrationuser(String registrationuser) {
        this.registrationuser = registrationuser;
    }

    public Date getRegistrationtime() {
        return registrationtime;
    }

    public void setRegistrationtime(Date registrationtime) {
        this.registrationtime = registrationtime;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTroleid() {
        return troleid;
    }
    public void setTroleid(String troleid) {
        this.troleid = troleid;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
