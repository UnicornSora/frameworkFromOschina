package com.common.security.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.po.Tparameter;
import com.base.utils.Page;
import com.common.security.service.ParameterService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:系统参数管理controller类
 */
@Controller
@RequestMapping("parameterController")
public class ParameterController {

    @Autowired
    private ParameterService parameterService;

    /**
     * 字典信息dategrid查询
     * @param type 类别
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return 字典信息
     */
    @RequestMapping(value = "gridform1.do")
    @ResponseBody
    @RequiresPermissions("parameterController/gridform1.do")
    public Object gridform1(String type, int page, int rows) {
        //总数
        int sum = parameterService.queryCount(type);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<Tparameter> list = parameterService.queryList(type, p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list) ;
        return result;
    }

    /**
     * 添加系统参数
     * @param  area_dlg  所属区划
     * @param  type_dlg  类型
     * @param  value_dlg 值
     * @param  remarks_dlg 备注
     * @return 操作信息
     */
    @RequestMapping(value = "save.do")
    @ResponseBody
    @RequiresPermissions("parameterController/save.do")
    @Log(name="添加系统参数")
    public Object save(String area_dlg,String type_dlg,String value_dlg,String remarks_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //判断重复
            if(parameterService.getParameterByType(area_dlg,type_dlg)){
                msg = "系统参数重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tparameter t = new Tparameter();
            t.setArea(area_dlg);
            t.setType(type_dlg);
            t.setValue(value_dlg);
            t.setRemarks(remarks_dlg);
            parameterService.save(t);
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

    /**
     * 更新系统参数
     * @param  tparameterid_dlg  id
     * @param  area_dlg  所属区划
     * @param  type_dlg  类型
     * @param  value_dlg 值
     * @param  remarks_dlg 备注
     * @return 操作信息
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    @RequiresPermissions("parameterController/update.do")
    @Log(name="更新系统参数")
    public Object update(String tparameterid_dlg,String area_dlg, String type_dlg,String value_dlg, String remarks_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //判断重复
            if(parameterService.getParameterByType(area_dlg,type_dlg,tparameterid_dlg)){
                msg = "系统参数重复！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //更新对象
            Tparameter t = new Tparameter();
            t.setArea(area_dlg);
            t.setType(type_dlg);
            t.setValue(value_dlg);
            t.setRemarks(remarks_dlg);
            t.setTparameterid(tparameterid_dlg);
            parameterService.update(t);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "修改失败！";
            success = false;
        }
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }

    /**
     * 删除系统参数
     * @param  tparameterid id
     * @return 操作信息
     */
    @RequestMapping(value = "delete.do")
    @ResponseBody
    @RequiresPermissions("parameterController/delete.do")
    @Log(name="删除系统参数")
    public Object delete(String tparameterid) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            parameterService.delete(Tparameter.class,tparameterid);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "删除失败！";
            success = false;
        }
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }
}