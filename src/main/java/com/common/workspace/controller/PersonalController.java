package com.common.workspace.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.po.Tlogin;
import com.common.security.vo.LoginUserVO;
import com.common.workspace.service.PersonalService;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-6-12
 * @classDescription:个人信息维护controller类
 */
@Controller
@RequestMapping("personalController")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    /**
     * 获取用户信息
     * @return 用户对象
     */
    @RequestMapping(value = "get.do")
    @ResponseBody
    @RequiresPermissions("personalController/get.do")
    public Object getPermission(HttpServletRequest request) {
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        Tlogin t =  personalService.get(Tlogin.class, vo.getLoginid());
        return t;
    }

    /**
     * 更新用户信息
     * @param  username  用户名
     * @param  phone     用户电话
     * @return 操作信息
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    @RequiresPermissions("personalController/update.do")
    @Log(name="更新用户信息")
    public Object updatePermission(String username,String phone,HttpServletRequest request) {
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            Tlogin t =  personalService.get(Tlogin.class, vo.getLoginid());
            t.setPhone(phone);
            t.setUsername(username);
            //保存
            personalService.update(t);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "保存失败！";
            success = false;
        }
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }
}
