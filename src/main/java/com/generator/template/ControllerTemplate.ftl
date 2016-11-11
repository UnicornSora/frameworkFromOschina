package com.generator.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.controller.BaseController;
import com.base.po.${class?cap_first};
import com.base.utils.Page;
import com.common.security.service.${class?cap_first}Service;

@Controller
@RequestMapping("${class}Controller")
public class ${class?cap_first}Controller  extends BaseController {
    @Autowired
    private ${class?cap_first}Service ${class}Service;

    @RequestMapping(value = "gridform.do")
    @ResponseBody
    @RequiresPermissions("${class}Controller/gridform.do")
    public Object gridform(int page, int rows) {
        //获取参数
        //总数
        int sum = ${class}Service.queryCount();
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<${class?cap_first}> list = ${class}Service.queryList(p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list);
        return result;
    }

    @RequestMapping(value = "save.do")
    @ResponseBody
    @RequiresPermissions("${class?cap_first}Controller/save.do")
    public Object save(HttpServletRequest request,HttpServletResponse response) {
        //获取参数
        <#list properties as prop>
            <#if prop.isPk != "true">
        ${prop.type} ${prop.name} = request.getParameter("${prop.name}_dlg");
            </#if>
        </#list>
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //保存对象
            ${class?cap_first} t = new ${class?cap_first}();
            <#list properties as prop>
                <#if prop.isPk != "true">
                t.set${prop.name?cap_first}(${prop.name});
                </#if>
            </#list>
            ${class}Service.save(t);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "保存失败！";
            success = false;
        }
        //返回结果
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }

    @RequestMapping(value = "update.do")
    @ResponseBody
    @RequiresPermissions("${class}Controller/update.do")
    public Object update(HttpServletRequest request,HttpServletResponse response) {
        //获取参数
    <#list properties as prop>
        ${prop.type} ${prop.name} = request.getParameter("${prop.name}_dlg");
    </#list>
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //获取对象
<#list properties as prop>
    <#if prop.isPk == "true">
             ${class?cap_first} t = ${class}Service.get(${class?cap_first}.class, ${prop.name} );
    </#if>
</#list>
            //更新对象
<#list properties as prop>
    <#if prop.isPk != "true">
            t.set${prop.name?cap_first}(${prop.name});
    </#if>
</#list>
            ${class}Service.update(t);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "修改失败！";
            success = false;
        }
        //返回结果
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }

    @RequestMapping(value = "delete.do")
    @ResponseBody
    @RequiresPermissions("${class}Controller/delete.do")
    public Object delete(HttpServletRequest request,HttpServletResponse response) {
        //获取对象ID
    <#list properties as prop>
        <#if prop.isPk == "true">
        ${prop.type} ${prop.name} = request.getParameter("${prop.name}");
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            //获取对象
            ${class?cap_first} t = ${class}Service.get(${class?cap_first}.class,${prop.name} );
        </#if>
    </#list>
            //删除对象
            ${class}Service.delete(t);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "删除失败！";
            success = false;
        }
        //返回结果
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }
}
