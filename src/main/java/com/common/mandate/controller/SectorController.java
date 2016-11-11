package com.common.mandate.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.controller.BaseController;
import com.base.po.Tarea;
import com.base.po.Tsector;
import com.base.utils.Page;
import com.common.mandate.service.SectorService;
import com.common.mandate.vo.TreeDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:部门管理模块controller类
 */
@Controller
@RequestMapping("sectorController")
public class SectorController  extends BaseController {

    @Autowired
    private SectorService sectorService;

    /**
     * 获取单位信息tree
     * @return 单位信息tree
     */
    @RequestMapping(value="getTree.do")
    @ResponseBody
    public Object getUserTree() {
        //获取顶级区划
        Tarea p =  sectorService.getTopArea();
        String toparea = "";
        if(p != null){
            toparea = p.getCode();
        }
        TreeDate tree = sectorService.getTree(toparea);
        List<TreeDate> list = new ArrayList<TreeDate>();
        list.add(tree);
        return list;
    }

    /**
     * 部门信息dategrid查询
     * @param departmenttree 所属单位
     * @param name 部门名称
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return 部门信息
     */
    @RequestMapping(value = "gridform.do")
    @ResponseBody
    @RequiresPermissions("sectorController/gridform.do")
    public Object gridform(String departmenttree,String name ,int page, int rows) {
        //总数
        int sum = sectorService.queryCount(departmenttree,name);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<Tsector> list = sectorService.queryList(departmenttree,name,p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list);
        return result;
    }

    /**
     * 添加部门
     * @param  departmenttree_dlg  所属单位
     * @param  sectorname_dlg      单位名称
     * @param  linkman_dlg         单位联系人
     * @param  phone_dlg           联系人电话
     * @return 操作信息
     */
    @RequestMapping(value = "save.do")
    @ResponseBody
    @RequiresPermissions("sectorController/save.do")
    @Log(name="添加部门")
    public Object save(String departmenttree_dlg,String sectorname_dlg,String linkman_dlg,String phone_dlg) {
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //判断所属单位是否存在
            if(!sectorService.getDepartmentById(departmenttree_dlg)){
                msg = "所属单位不存在!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //同一单位内不允许出现相同部门名称
            if(sectorService.getBydepartmenName(departmenttree_dlg,sectorname_dlg)){
                msg = "相同单位下部门名称重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //保存对象
            Tsector t = new Tsector();
            t.setTdepartmentid(departmenttree_dlg);
            t.setTsectorname(sectorname_dlg);
            t.setLinkman(linkman_dlg);
            t.setPhone(phone_dlg);
            sectorService.save(t);
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
     * 修改部门
     * @param  departmenttree_dlg  所属单位
     * @param  sectorname_dlg      单位名称
     * @param  sectorid_dlg        单位id
     * @param  linkman_dlg         单位联系人
     * @param  phone_dlg           联系人电话
     * @return 操作信息
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    @RequiresPermissions("sectorController/update.do")
    @Log(name="修改部门")
    public Object update(String departmenttree_dlg ,String sectorid_dlg ,String sectorname_dlg,String linkman_dlg,String phone_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //判断所属单位是否存在
            if(!sectorService.getDepartmentById(departmenttree_dlg)){
                msg = "所属单位不存在!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //同一单位内不允许出现相同部门名称
            if(sectorService.getBydepartmenNameAndId(departmenttree_dlg,sectorname_dlg,sectorid_dlg)){
                msg = "相同单位下部门名称重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //获取对象
            Tsector t = sectorService.get(Tsector.class, sectorid_dlg );
            //更新对象
            t.setTdepartmentid(departmenttree_dlg);
            t.setTsectorname(sectorname_dlg);
            t.setLinkman(linkman_dlg);
            t.setPhone(phone_dlg);
            sectorService.update(t);
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
     * 删除部门
     * @param  tsectorid 部门id
     * @return 操作信息
     */
    @RequestMapping(value = "delete.do")
    @ResponseBody
    @RequiresPermissions("sectorController/delete.do")
    @Log(name="删除部门")
    public Object delete(String tsectorid) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            //判断是否存在部门，如果存在部门，则不能删除。
            if(sectorService.getUserById(tsectorid)){
                msg = "部门下存在用户，无法删除!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tsector t = sectorService.get(Tsector.class, tsectorid );
            //删除对象
            sectorService.delete(t);
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
