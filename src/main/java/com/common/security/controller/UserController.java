package com.common.security.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.controller.BaseController;
import com.base.po.Tarea;
import com.base.po.Tlogin;
import com.base.po.TloginTrole;
import com.base.po.Tparameter;
import com.base.utils.MD5;
import com.base.utils.Page;
import com.common.mandate.vo.TreeDate;
import com.common.security.service.UserService;
import com.common.security.vo.LoginUserVO;
import com.common.security.vo.TloginVo;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: football98
 * @createTime:16-9-28
 * @classDescription:用户管理模块controller类
 */
@Controller
@RequestMapping("userController")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    /**
     * 查询用户信息
     * @param loginname 登录名
     * @param username  用户名
     * @param status 用户状态
     * @param trole 用户角色
     * @param sector 所属部门
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return 用户信息
     */
    @RequestMapping(value = "gridform1.do")
    @ResponseBody
    @RequiresPermissions("userController/gridform1.do")
    public Object gridform1(String loginname,String username ,String status,String trole,String sector,int page, int rows) {
        //总数
        int sum = userService.getTloginSum(loginname,username,status,trole, sector);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<TloginVo> list = userService.listTlogin(loginname,username,status,trole,sector,p);
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list) ;
        return result;
    }

    /**
     * 添加用户
     * @param loginname_dlg 登录名
     * @param username_dlg  用户名
     * @param phone_dlg 联系电话
     * @param trole_dlg 用户角色
     * @param status_dlg 用户状态
     * @param sector_dlg 所属部门
     * @param request HttpServletRequest
     * @return 操作信息
     */
    @RequestMapping(value = "usersave.do")
    @ResponseBody
    @RequiresPermissions("userController/usersave.do")
    @Log(name="添加用户")
    public Object usersave(String loginname_dlg,String phone_dlg,String username_dlg ,String trole_dlg,int status_dlg,String sector_dlg,HttpServletRequest request) {
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            //判断所属部门是否存在
            if(!userService.getTsectorById(sector_dlg)){
                msg = "所属部门不存在!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //判断用户名是否重复
            if(userService.getUserByLoginname(loginname_dlg)){
                msg = "登录名重复!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //保存Tlogin,Tlogin_trole
            Tlogin t = new Tlogin();
            t.setLoginname(loginname_dlg);
            t.setUsername(username_dlg);
            t.setPhone(phone_dlg);
            //添加初始化密码
            Tparameter p = userService.getTparameter("PASSWORK");
            t.setPassword(p.getValue());
            t.setStatus(status_dlg);
            t.setRegistrationuser(vo.getLoginname());
            t.setRegistrationtime(new Date());
            t.setSector(sector_dlg);
            TloginTrole tr = new TloginTrole();
            tr.setTroleid(trole_dlg);
            userService.saveTlogin(t,tr);
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
     * 修改用户
     * @param id_dlg 用户id
     * @param username_dlg  用户名
     * @param phone_dlg 联系电话
     * @param trole_dlg 用户角色
     * @param status_dlg 用户状态
     * @param sector_dlg 所属部门
     * @return 操作信息
     */
    @RequestMapping(value = "userupdate.do")
    @ResponseBody
    @RequiresPermissions("userController/userupdate.do")
    @Log(name="修改用户")
    public Object userupdate(String id_dlg,String username_dlg,String phone_dlg,String trole_dlg,int status_dlg,String sector_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            //判断所属部门是否存在
            if(!userService.getTsectorById(sector_dlg)){
                msg = "所属部门不存在!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            //更新Tlogin
            Tlogin t = new Tlogin();
            t.setTloginid(id_dlg);
            t.setUsername(username_dlg);
            t.setPhone(phone_dlg);
            t.setStatus(status_dlg);
            t.setSector(sector_dlg);
            //保login
            userService.updateTlogin(t,trole_dlg);
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
     * 删除用户
     * @param id 用户id
     * @return 操作信息
     */
    @RequestMapping(value = "userdelete.do")
    @ResponseBody
    @RequiresPermissions("userController/userdelete.do")
    @Log(name="删除用户")
    public Object userdelete(String id) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            //获取用户对象
            Tlogin t = userService.get(Tlogin.class,id);
            //判断，如果此用户是否存在子用户，如果存在不可删除。
            if(userService.findByRegistrationuser(t.getLoginname())){
                msg = "存在子用户无法删除！";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            };
            //删除用户
            userService.deleteTlogin(t);
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
     * 获取部门树信息
     * @return 部门树信息
     */
    @RequestMapping(value="getSectorTree.do")
    @ResponseBody
    public Object getSectorTree() {
        //获取顶级区划
        Tarea p =  userService.getTopArea();
        String toparea = "";
        if(p != null){
            toparea = p.getCode();
        }
        TreeDate tree = userService.getSectorTree(toparea);
        List<TreeDate> list = new ArrayList<TreeDate>();
        list.add(tree);
        return list;
    }

    /**
     * 初始化用户密码
     * @param id 用户id
     * @return 操作信息
     */
    @RequestMapping(value = "initializePassword.do")
    @ResponseBody
    @RequiresPermissions("userController/initializePassword.do")
    @Log(name="初始化用户密码")
    public Object initializePassword(String id) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "密码初始化成功！";
        boolean success = true;
        try {
            Tparameter p = userService.getTparameter("PASSWORK");
            //获取用户对象
            Tlogin t = userService.get(Tlogin.class,id);
            MD5 m = new MD5();
            t.setPassword(m.getMD5ofStr(p.getValue()));
            //删除用户
            userService.update(t);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "密码初始化失败！";
            success = false;
        }
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }
}