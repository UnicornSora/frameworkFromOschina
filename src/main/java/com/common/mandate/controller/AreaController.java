package com.common.mandate.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.po.Tarea;
import com.base.utils.Page;
import com.common.mandate.service.AreaService;
import com.common.mandate.vo.TreeDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:行政区划模块控制方法
 */
@Controller
@RequestMapping("areaController")
public class AreaController {

    @Autowired
    private AreaService areaService;

    /**
     * 行政区划dategrid信息查询
     * @param areatree 父级行政区划代码
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return 行政区划dategrid信息
     */
    @RequestMapping(value = "gridform1.do")
    @ResponseBody
    @RequiresPermissions("areaController/gridform1.do")
    public Object gridform1(String areatree, int page, int rows) {
        //总数
        int sum = areaService.queryCount(areatree);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<Tarea> list = areaService.queryList(areatree, p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list) ;
        return result;
    }

    /**
     * 获取行政区划树信息
     * @return 行政区划树信息
     */
    @RequestMapping(value="getAreaTree.do")
    @ResponseBody
    public Object getUserTree() {
        //获取顶级区划
        Tarea p =  areaService.getTopArea();
        String toparea = "";
        if(p != null){
            toparea = p.getCode();
        }
        TreeDate tree = areaService.getAreaTree(toparea);
        List<TreeDate> list = new ArrayList<TreeDate>();
        list.add(tree);
        return list;
    }

    /**
     * 添加行政区划
     * @param  parentcode_dlg 父区划代码
     * @param  name_dlg       行政区划名称
     * @param  code_dlg       行政区划代码
     * @param  areatype_dlg   行政区划类型：省、市、县、乡、村
     * @return 操作信息
     */
    @RequestMapping(value = "add.do")
    @ResponseBody
    @RequiresPermissions("areaController/add.do")
    @Log(name="添加行政区划")
    public Object add(String parentcode_dlg,String name_dlg,String code_dlg,String areatype_dlg) {
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //添加判断
            if(areaService.getPtareaByCodeOrName(code_dlg,name_dlg)){
                msg = "区划代码或区划名称重复！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tarea p = new Tarea();
            p.setParentcode(parentcode_dlg);
            p.setName(name_dlg);
            p.setCode(code_dlg);
            p.setAreatype(areatype_dlg);
            p.setIstop(0);
            areaService.save(p);
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
     * 修改行政区划
     * @param  parentcode_dlg 父区划代码
     * @param  name_dlg       行政区划名称
     * @param  code_dlg       行政区划代码
     * @param  areatype_dlg   行政区划类型：省、市、县、乡、村
     * @param  areaid_dlg     需要修改的行政区划id
     * @return 操作信息
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    @RequiresPermissions("areaController/update.do")
    @Log(name="修改行政区划")
    public Object update(String parentcode_dlg,String name_dlg,String code_dlg,String areatype_dlg,String areaid_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //判断重复
            if(areaService.getPtareaByCodeNameId(code_dlg,name_dlg,areaid_dlg)){
                msg = "区划代码或区划名称重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tarea p = new Tarea();
            p.setAreaid(areaid_dlg);
            p.setParentcode(parentcode_dlg);
            p.setName(name_dlg);
            p.setCode(code_dlg);
            p.setAreatype(areatype_dlg);
            p.setIstop(0);
            areaService.update(p);
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
     * 删除行政区划
     * @param  areaid 行政区划id
     * @return 操作信息
     */
    @RequestMapping(value = "delete.do")
    @ResponseBody
    @RequiresPermissions("areaController/delete.do")
    @Log(name="删除行政区划")
    public Object delete(String areaid) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            //添加判断，区划存在机构不能删除
            Tarea t = areaService.get(Tarea.class,areaid);
            if(areaService.getDepartmentByAreaCode(t.getCode())){
                msg = "行政区划下存在单位，无法删除!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //删除对象
            areaService.delete(t);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "删除失败！";
            success = false;
        }
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }

    /**
     * 获取顶级区划对象
     * @return 顶级区划对象
     */
    @RequestMapping(value = "getTopArea.do")
    @ResponseBody
    public Object getTopArea() {
        Tarea p =  areaService.getTopArea();
        Map<String, Object> result = new HashMap<String, Object>() ;
        //判断p是否存在
        if( p != null){
            result.put("p",p);
            result.put("msg","update");
        }else{
            result.put("msg","save");
        }
        return result;
    }

    /**
     * 更新顶级行政区划
     * @param  parentcode_toparea 父行政区划编码
     * @param  name_toparea 顶级行政名称
     * @param  code_toparea 顶级行政区划编码
     * @param  areatype_toparea 顶级行政区划类型
     * @param  areaid_toparea 顶级行政区划
     * @return 操作信息
     */
    @RequestMapping(value = "updateTopArea.do")
    @ResponseBody
    @RequiresPermissions("areaController/updateTopArea.do")
    @Log(name="更新顶级行政区划")
    public Object updateTopArea(String parentcode_toparea,String name_toparea,String code_toparea,String areatype_toparea,String areaid_toparea ) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //判断重复
            if(areaService.getPtareaByCodeNameId(code_toparea,name_toparea,areaid_toparea)){
                msg = "区划代码或区划名称重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tarea p = new Tarea();
            p.setAreaid(areaid_toparea);
            p.setParentcode(parentcode_toparea);
            p.setName(name_toparea);
            p.setCode(code_toparea);
            p.setAreatype(areatype_toparea);
            p.setIstop(1);
            areaService.update(p);
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
     * 新增顶级行政区划
     * @param  parentcode_toparea 父行政区划编码
     * @param  name_toparea 顶级行政名称
     * @param  code_toparea 顶级行政区划编码
     * @param  areatype_toparea 顶级行政区划类型
     * @return 操作信息
     */
    @RequestMapping(value = "saveTopArea.do")
    @ResponseBody
    @RequiresPermissions("areaController/saveTopArea.do")
    @Log(name="新增顶级行政区划")
    public Object saveTopArea(String parentcode_toparea,String name_toparea,String code_toparea,String areatype_toparea) {
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //添加判断
            if(areaService.getPtareaByCodeOrName(code_toparea,name_toparea)){
                msg = "区划代码或区划名称重复！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tarea p = new Tarea();
            p.setParentcode(parentcode_toparea);
            p.setName(name_toparea);
            p.setCode(code_toparea);
            p.setAreatype(areatype_toparea);
            p.setIstop(1);
            areaService.save(p);
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
}
