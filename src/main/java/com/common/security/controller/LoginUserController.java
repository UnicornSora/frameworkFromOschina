package com.common.security.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.po.Tlogin;
import com.base.utils.MD5;
import com.base.utils.SessionListener;
import com.common.security.service.LoginUserService;
import com.common.security.vo.LoginUserVO;
import com.common.security.vo.TreeNew;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:登录controller类
 */
@Controller
@RequestMapping("loginUserController")
public class LoginUserController  {
    @Autowired
    private LoginUserService loginUserService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 结果页面
     */
    @RequestMapping(value="login.do")
    @Log(name="用户登录")
    public String login(String username,String password,HttpServletRequest request) {
        //判断验证码是否正确
        /* Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
        String code = request.getParameter("code");
        if(!captcha.getAnswer().equals(code)){
            request.setAttribute("ERROR", "验证码不正确!");
            return "login";
        }*/
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if (subject.isAuthenticated()) {
                LoginUserVO vo = loginUserService.loginUser(username);
                request.getSession().setAttribute("LOGINUSER", vo);
                //用户登录成功后，更新session Map，如重复登录，强制之前session过期
                String sessionid = SessionListener.userMap.get(username);
                if(sessionid != null&&!sessionid.equals("")){
                    //注销在线用户,如果session id 相同，不销毁。
                    if(!sessionid.equals(request.getSession().getId())){
                        SessionListener.sessionMap.get(sessionid).invalidate();
                        SessionListener.userMap.put(username,request.getSession().getId());
                        SessionListener.sessionMap.put(request.getSession().getId(),request.getSession());
                    }
                }else{
                    if(SessionListener.sessionMap.containsKey(request.getSession().getId())){
                        SessionListener.sessionMap.remove(request.getSession().getId());
                        for(String key : SessionListener.userMap.keySet()){
                            if(SessionListener.userMap.get(key).equals(request.getSession().getId())){
                                SessionListener.userMap.remove(key);
                            }
                        }
                    }
                    SessionListener.userMap.put(username,request.getSession().getId());
                    SessionListener.sessionMap.put(request.getSession().getId(),request.getSession());
                }
                return "index";
            }
        } catch (UnknownAccountException e) {
            request.setAttribute("ERROR", "用户名错误！");
        } catch (IncorrectCredentialsException e) {
            request.setAttribute("ERROR", "密码错误!");
        }
        return "login";
    }

    /**
     * 获取用户页面树
     * @param id 父节点id
     * @return 页面树
     */
    @RequestMapping(value="getUserTree.do")
    @ResponseBody
    public Object getUserTree(String id , HttpServletRequest request) {
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        List<TreeNew> list ;
        if(id!=null&&!id.equals("")){
            list = loginUserService.getUserTree(vo.getRolename(),id);
        }else{
            list = loginUserService.getUserTree(vo.getRolename(),"d3600285-b3a8-42dc-a99e-e97ff15fcf7c");
        }
        return list;
    }

    /**
     * 修改密码
     * @param  password_old   旧密码
     * @param  password_new1  新密码
     * @param  password_new2  确认新密码
     * @return 操作信息
     */
    @RequestMapping(value = "updatePassWord.do")
    @ResponseBody
    @Log(name="修改密码")
    public Object updatePassWord(String password_old,String password_new1,String password_new2,HttpServletRequest request) {
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            if(!password_new1.equals(password_new2)){
                msg = "新密码与确认新密码不一致!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            Tlogin t  = loginUserService.getPasssWordOld(vo.getLoginname(),password_old);
            if(t == null){
                msg = "原密码不正确!";
                success = false;
                result.put("success",success);
                result.put("msg",msg);
                return result;
            }
            MD5 md5 = new MD5();
            t.setPassword(md5.getMD5ofStr(password_new1));
            loginUserService.update(t);
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
     * 退出
     * @return 登录页
     */
    @RequestMapping(value = "logout.do")
    @Log(name="退出登录")
    public String logout(HttpServletRequest request) {
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        SessionListener.userMap.remove(vo.getLoginname());
        SessionListener.sessionMap.remove(request.getSession().getId());
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

}
