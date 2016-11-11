package com.common.shiro.service;

import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:shiro service接口
 */
public interface ShiroService {
    /**
     * 判断用户名是否存在
     * @param loginname 登录名
     * @return 是/否
     */
    public boolean findLoginname(String loginname);
    /**
     * 判断登录名、密码是否正确
     * @param loginname 登录名
     * @param password 密码
     * @return 是/否
     */
    public boolean getUserByLoginname(String loginname, String password);
    /**
     * 获取用户权限列表
     * @param loginname 登录名
     * @return 用户权限列表
     */
    public List<String> getPermissionsByLoginname(String loginname);
}
