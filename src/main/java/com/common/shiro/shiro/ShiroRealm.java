package com.common.shiro.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import com.base.utils.MD5;
import com.common.shiro.service.ShiroService;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:shiro service接口
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private ShiroService shiroService;

    /***
     * 获取认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) at;
        // 通过表单接收的用户名
        MD5 md5 = new MD5();
        String username = token.getUsername();
        String password = md5.getMD5ofStr(String.valueOf(token.getPassword()));

        if(!shiroService.findLoginname(username)){
            throw new UnknownAccountException(); //如果用户名错误
        }
        if (!shiroService.getUserByLoginname(username,password)) {
            throw new IncorrectCredentialsException(); //如果密码错误
        }
        return new SimpleAuthenticationInfo(username, token.getPassword(), getName());
    }
    /***
     * 获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        // 根据自己系统规则的需要编写获取授权信息，这里为了快速入门只获取了用户对应角色的资源url信息
        String loginname = (String) pc.fromRealm(getName()).iterator().next();
        if (loginname != null) {
            List<String> pers = shiroService.getPermissionsByLoginname(loginname);
            if (pers != null && !pers.isEmpty()) {
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                for (String each : pers) {
                    // 将权限资源添加到用户信息中
                    info.addStringPermission(each);
                }
                return info;
            }
        }
        return null;
    }
}