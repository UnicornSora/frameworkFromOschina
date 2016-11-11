package com.common.security.service;

import com.base.po.Trole;
import com.base.service.BaseService;
import com.base.utils.Page;
import com.common.security.vo.TreeNew;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:角色管理service接口
 */
public interface RoleService extends BaseService {
    /**
     * 查询角色总数
     * @param rolename 角色名
     * @return 总数
     */
    public int queryCount(String rolename);
    /**
     * 查询角色信息
     * @param rolename 角色名
     * @param p 分页信息
     * @return 角色信息
     */
    public List<Trole> queryList(String rolename, Page p);
    /**
     * 判断角色信息是否重复
     * @param rolename 角色名
     * @return 是/否
     */
    public boolean getTroleByRolename(String rolename);
    /**
     * 判断角色信息是否重复
     * @param rolename 角色名
     * @param troleid 角色id
     * @return 是/否
     */
    public boolean getTroleByRolename(String rolename, String troleid);
    /**
     * 判断是否存在使用该角色的用户
     * @param troleid 角色id
     * @return 是/否
     */
    public boolean getTlogintrole(String troleid);
    /**
     * 删除角色
     * @param troleid 角色id
     * @return
     */
    public void deleteTrole(String troleid);
    /**
     * 获取角色权限树
     * @param pid 父id
     * @param roleid 角色id
     * @return 权限树
     */
    public List<TreeNew> getTree(String pid, String roleid);
    /**
     * 角色赋权限
     * @param role 权限树字符串
     * @param id 角色id
     * @return 权限树
     */
    public void role_Save(String role, String id);
    /**
     * 添加角色
     * @param rolename 角色名称
     * @return
     */
    public void roleSave(String rolename);
    /**
     * 更新角色
     * @param id 角色id
     * @param rolename 角色名称
     * @return
     */
    public void roleUpdate(String id, String rolename);
}

