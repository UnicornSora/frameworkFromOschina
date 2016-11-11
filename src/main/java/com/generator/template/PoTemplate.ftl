package com.base.po;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
<#list classList as list>
import ${list}
</#list>

@Entity
public class ${class?cap_first} implements Serializable {

<#list properties as prop>
    private ${prop.type} ${prop.name};
</#list>

<#list properties as prop>

    <#if prop.isPk == "true">
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    </#if>
    public ${prop.type} get${prop.name?cap_first}(){
        return ${prop.name};
    }

    public void set${prop.name?cap_first}(${prop.type} ${prop.name}){
        this.${prop.name} = ${prop.name};
    }
</#list>

}