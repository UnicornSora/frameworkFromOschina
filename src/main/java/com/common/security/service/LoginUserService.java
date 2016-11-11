package com.common.security.service;

import com.base.po.Tlogin;
import com.base.service.BaseService;
import com.common.security.vo.LoginUserVO;
import com.common.security.vo.TreeNew;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:用户登录service接口
 */
public interface LoginUserService extends BaseService {
    /**
     * 通过登录名，获取用户对象
     * @param loginname 登录名
     * @return 用户对象
     */
    public LoginUserVO loginUser(String loginname);
    /**
     * 获取用户权限树
     * @param rolename 角色名
     * @param pid 父ID
     * @return 用户权限树
     */
    public List<TreeNew> getUserTree(String rolename, String pid);
    /**
     * 获取用户对象
     * @param loginname 登录名
     * @param password_old 密码
     * @return 用户对象
     */
    public Tlogin getPasssWordOld(String loginname, String password_old);
}
