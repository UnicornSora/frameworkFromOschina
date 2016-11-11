package com.common.mandate.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.po.Tdepartment;
import com.base.utils.Page;
import com.common.mandate.service.DepartmentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:单位管理模块controller类
 */
@Controller
@RequestMapping("departmentController")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 单位信息dategrid查询
     * @param areatree 所属行政区划
     * @param name 单位名称
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return 单位信息
     */
    @RequestMapping(value = "gridform1.do")
    @ResponseBody
    @RequiresPermissions("departmentController/gridform1.do")
    public Object gridform1(String areatree ,String name, int page, int rows) {
        //总数
        int sum = departmentService.queryCount(areatree,name);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<Tdepartment> list = departmentService.queryList(areatree,name, p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list) ;
        return result;
    }

    /**
     * 添加单位
     * @param  areacode_dlg  所属区划
     * @param  name_dlg      单位名称
     * @param  address_dlg   单位地址
     * @return 操作信息
     */
    @RequestMapping(value = "add.do")
    @ResponseBody
    @RequiresPermissions("departmentController/add.do")
    @Log(name="添加单位")
    public Object add(String areacode_dlg,String name_dlg,String address_dlg) {
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //添加判断
            if(departmentService.getByAreaCodeAndName(areacode_dlg,name_dlg)){
                msg = "相同行政区划下单位名称重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tdepartment d = new Tdepartment();
            d.setAreacode(areacode_dlg);
            d.setName(name_dlg);
            d.setAddress(address_dlg);
            departmentService.save(d);
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
     * 更新单位
     * @param  departmentid_dlg 单位id
     * @param  areacode_dlg  所属区划
     * @param  name_dlg      单位名称
     * @param  address_dlg   单位地址
     * @return 操作信息
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    @RequiresPermissions("departmentController/update.do")
    @Log(name="更新单位")
    public Object update(String departmentid_dlg,String areacode_dlg,String name_dlg,String address_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //判断重复
            if(departmentService.getByAreaCodeAndNameAndId(areacode_dlg,name_dlg,departmentid_dlg)){
                msg = "相同行政区划下单位名称重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tdepartment d = new Tdepartment();
            d.setDepartmentid(departmentid_dlg);
            d.setAreacode(areacode_dlg);
            d.setName(name_dlg);
            d.setAddress(address_dlg);
            departmentService.update(d);
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
     * 删除单位
     * @param  departmentid 单位id
     * @return 操作信息
     */
    @RequestMapping(value = "delete.do")
    @ResponseBody
    @RequiresPermissions("departmentController/delete.do")
    @Log(name="删除单位")
    public Object delete(String departmentid) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            //判断是否存在部门，如果存在部门，则不能删除。
            if(departmentService.getSectorById(departmentid)){
                msg = "单位下存在部门，无法删除!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //删除对象
            departmentService.delete(Tdepartment.class,departmentid);
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
