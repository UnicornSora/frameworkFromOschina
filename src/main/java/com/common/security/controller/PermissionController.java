package com.common.security.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.controller.BaseController;
import com.base.po.Tpermission;
import com.common.security.service.PermissionService;
import com.common.security.vo.TreeNew;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:权限管理模块controller类
 */
@Controller
@RequestMapping("permissionController")
public class PermissionController extends BaseController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 查询权限tree
     * @param id 父节点id
     * @return 权限tree
     */
    @RequestMapping(value="getUserTree.do")
    @ResponseBody
    @RequiresPermissions("permissionController/getUserTree.do")
    public Object getUserTree(String id) {
        List<TreeNew> list ;
        if(id != null&&!id.equals("")){
            list = permissionService.getTree(id);
        }else{
            list = permissionService.getTree("0");
        }
        return list;
    }

    /**
     * 获取权限对象
     * @param id 权限id
     * @return 权限对象
     */
    @RequestMapping(value = "getPermission.do")
    @ResponseBody
    @RequiresPermissions("permissionController/getPermission.do")
    public Object getPermission(String id) {
        Tpermission t = permissionService.get(Tpermission.class, id);
        return t;
    }

    /**
     * 添加权限
     * @param  tpermissionid  父权限id
     * @param  permissionname  权限名称
     * @param  action 动作,目录，页面，按钮
     * @param  url 动作的URL
     * @param  orders 序号
     * @return 操作信息
     */
    @RequestMapping(value = "addPermission.do")
    @ResponseBody
    @RequiresPermissions("permissionController/addPermission.do")
    @Log(name="添加权限")
    public Object addPermission(String tpermissionid,String permissionname,String action,String url,String orders) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //判断重复
            if(permissionService.getTpermissionByPermissionname(permissionname)){
                msg = "权限名重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tpermission t = new Tpermission();
            t.setParentid(tpermissionid);
            t.setPermissionname(permissionname);
            t.setAction(Integer.parseInt((action)));
            t.setUrl(url);
            t.setOrders(Integer.parseInt((orders)));
            //保存
            permissionService.save(t);
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
     * 更新权限
     * @param  tpermissionid  权限id
     * @param  permissionname  权限名称
     * @param  action 动作,目录，页面，按钮
     * @param  url 动作的URL
     * @param  orders 序号
     * @return 操作信息
     */
    @RequestMapping(value = "updatePermission.do")
    @ResponseBody
    @RequiresPermissions("permissionController/updatePermission.do")
    @Log(name="更新权限")
    public Object updatePermission(String tpermissionid,String permissionname,int action,String url,int orders) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //判断重复
            if(permissionService.getByPermissionname(permissionname, tpermissionid)){
                msg = "权限名重复！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tpermission t = permissionService.get(Tpermission.class,tpermissionid);
            t.setPermissionname(permissionname);
            t.setAction(action);
            t.setUrl(url);
            t.setOrders(orders);
            //保存
            permissionService.update(t);
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
     * 删除权限
     * @param  tpermissionid 权限id
     * @return 操作信息
     */
    @RequestMapping(value = "deletePermission.do")
    @ResponseBody
    @RequiresPermissions("permissionController/deletePermission.do")
    @Log(name="删除权限")
    public Object deletePemission(String tpermissionid) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            //判断是否存在角色使用该权限
            if(permissionService.getTroleTpermission(tpermissionid)){
                msg = "存在使用该权限的角色，不能删除!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //判断是否存在子权限
            if(permissionService.getSunTpermissionCount(tpermissionid)){
                msg = "该权限存在子权限，不能删除!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            permissionService.delete(Tpermission.class,tpermissionid);
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