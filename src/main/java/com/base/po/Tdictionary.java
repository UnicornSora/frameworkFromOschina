package com.base.po;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: po 类
 */
@Entity
@Table(name="t_dictionary")
public class Tdictionary implements Serializable {
    private int tdictionaryid;
    private String type;
    private String code;
    private String value;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getTdictionaryid() {
        return tdictionaryid;
    }
    public void setTdictionaryid(int tdictionaryid) {
        this.tdictionaryid = tdictionaryid;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
