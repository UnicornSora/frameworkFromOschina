package com.common.security.service;

import com.base.service.BaseService;
import com.common.security.vo.TreeNew;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:权限管理service接口
 */
public interface PermissionService extends BaseService {
    /**
     * 查询权限tree
     * @param pid 父节点id
     * @return 权限tree
     */
    public List<TreeNew> getTree(String pid);
    /**
     * 判断权限名称是否重复
     * @param permissionname 系统参数id
     * @return 是/否
     */
    public boolean getTpermissionByPermissionname(String permissionname);
    /**
     * 判断权限名称是否重复
     * @param tpermissionid 权限id
     * @param permissionname 系统参数id
     * @return 是/否
     */
    public boolean getByPermissionname(String permissionname, String tpermissionid);
    /**
     * 判读是否存在使用该权限的角色
     * @param tpermissionid 权限id
     * @return 是/否
     */
    public boolean getTroleTpermission(String tpermissionid);
    /**
     * 判读该权限是否存在子权限
     * @param tpermissionid 权限id
     * @return 是/否
     */
    public boolean getSunTpermissionCount(String tpermissionid);
}
