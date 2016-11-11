package com.common.security.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.po.Tdictionary;
import com.base.utils.Page;
import com.common.security.service.DictionaryService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:字典管理模块controller类
 */
@Controller
@RequestMapping("dictionaryController")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 字典信息dategrid查询
     * @param type 类别
     * @param code 值
     * @param value 含义
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return 字典信息
     */
    @RequestMapping(value = "gridform1.do")
    @ResponseBody
    @RequiresPermissions("dictionaryController/gridform1.do")
    public Object gridform1(String type,String code,String value, int page, int rows) {
        //总数
        int sum = dictionaryService.queryCount(type, code, value);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<Tdictionary> list = dictionaryService.queryList(type, code, value, p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list) ;
        return result;
    }

    /**
     * 添加字典
     * @param  type_dlg  类别
     * @param  code_dlg  值
     * @param  value_dlg 含义
     * @return 操作信息
     */
    @RequestMapping(value = "save.do")
    @ResponseBody
    @RequiresPermissions("dictionaryController/save.do")
    @Log(name="添加字典")
    public Object save(String type_dlg ,String code_dlg,String value_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //判断重复
            if(dictionaryService.getDictionaryByType(type_dlg,code_dlg,value_dlg)){
                //返回结果
                msg = "字典重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //保存对象
            Tdictionary t = new Tdictionary();
            t.setType(type_dlg);
            t.setCode(code_dlg);
            t.setValue(value_dlg);
            dictionaryService.save(t);
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
     * 更新字典
     * @param  type_dlg  类别
     * @param  code_dlg  值
     * @param  value_dlg 含义
     * @param  tdictionaryid_dlg  id
     * @return 操作信息
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    @RequiresPermissions("dictionaryController/update.do")
    @Log(name="更新字典")
    public Object update(String type_dlg ,String code_dlg,String value_dlg,int tdictionaryid_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //判断重复
            if(dictionaryService.getDictionaryByType(type_dlg,code_dlg,value_dlg,tdictionaryid_dlg)){
                msg = "角色名重复！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //更新对象
            Tdictionary t = new Tdictionary();
            t.setType(type_dlg);
            t.setCode(code_dlg);
            t.setValue(value_dlg);
            t.setTdictionaryid(tdictionaryid_dlg);
            dictionaryService.update(t);
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
     * 删除字典
     * @param  tdictionaryid 字典id
     * @return 操作信息
     */
    @RequestMapping(value = "delete.do")
    @ResponseBody
    @RequiresPermissions("dictionaryController/delete.do")
    @Log(name="删除字典")
    public Object delete(int tdictionaryid) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            dictionaryService.delete(Tdictionary.class,tdictionaryid);
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