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
@Table(name="t_parameter")
public class Tparameter implements Serializable {

    private String tparameterid;
    private String area;
    private String type;
    private String value;
    private String remarks;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    public String getTparameterid() {
        return tparameterid;
    }
    public void setTparameterid(String tparameterid) {
        this.tparameterid = tparameterid;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}