package com.common.security.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.controller.BaseController;
import com.base.po.Trole;
import com.base.utils.Page;
import com.common.security.service.RoleService;
import com.common.security.vo.TreeNew;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:角色管理模块controller类
 */
@Controller
@RequestMapping("roleController")
public class RoleController  extends BaseController {
    @Autowired
    private RoleService roleService;

    /**
     * 查询角色信息
     * @param rolename 角色名
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return 角色信息
     */
    @RequestMapping(value = "gridform1.do")
    @ResponseBody
    @RequiresPermissions("roleController/gridform1.do")
    public Object gridform1(String rolename, int page, int rows) {
        //总数
        int sum = roleService.queryCount(rolename);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<Trole> list = roleService.queryList(rolename, p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list) ;
        return result;
    }

    /**
     * 添加角色
     * @param rolename_dlg 角色名
     * @return 操作信息
     */
    @RequestMapping(value = "save.do")
    @ResponseBody
    @RequiresPermissions("roleController/save.do")
    @Log(name="添加角色")
    public Object save(String rolename_dlg ) {
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //判断重复
            if(roleService.getTroleByRolename(rolename_dlg)){
                //返回结果
                msg = "角色名重复！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //保存对象
            roleService.roleSave(rolename_dlg);
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
     * 更新角色
     * @param rolename_dlg 角色名
     * @param troleid_dlg 角色id
     * @return 操作信息
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    @RequiresPermissions("roleController/update.do")
    @Log(name="更新角色")
    public Object update(String rolename_dlg ,String troleid_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //如果有用户使用该角色，不允许修改
            if(roleService.getTlogintrole(troleid_dlg)){
                msg = "存在使用该角色的用户，不允许修改！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //判断重复
            if(roleService.getTroleByRolename(rolename_dlg,troleid_dlg)){
                msg = "角色名重复！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //更新对象
            roleService.roleUpdate(troleid_dlg,rolename_dlg);
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
     * 删除角色
     * @param troleid 角色id
     * @return 操作信息
     */
    @RequestMapping(value = "delete.do")
    @ResponseBody
    @RequiresPermissions("roleController/delete.do")
    @Log(name="删除角色")
    public Object delete(String troleid) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            //如果有用户使用该角色，不允许删除
            if(roleService.getTlogintrole(troleid)){
                msg = "存在使用该角色的用户，不允许删除！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            roleService.deleteTrole(troleid);
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
     * 查询权限树
     * @param request HttpServletRequest
     * @return 权限树
     */
    @RequestMapping(value="getUserTree.do")
    @ResponseBody
    @RequiresPermissions("roleController/getUserTree.do")
    public Object getUserTree(HttpServletRequest request) {
        String pid = request.getParameter("id");
        String roleid = request.getParameter("roleid");
        List<TreeNew> list ;
        if(roleid == null||roleid.equals("")){
            roleid = "";
        }
        if(pid != null&&!pid.equals("")){
            list = roleService.getTree(pid,roleid);
        }else{
            list = roleService.getTree("0",roleid);
        }
        return list;
    }

    /**
     * 角色赋权限
     * @param role_tree_text 角色权限字符串
     * @param role_id 角色id
     * @return 操作信息
     */
    @RequestMapping(value = "role_save.do")
    @ResponseBody
    @RequiresPermissions("roleController/role_save.do")
    @Log(name="角色赋权限")
    public Object role_save(String role_tree_text,String role_id) {
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            role_tree_text.substring(0,role_tree_text.length()-1);
            roleService.role_Save(role_tree_text,role_id);
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
