package com.common.security.service;

import com.base.po.Tlogin;
import com.base.po.TloginTrole;
import com.base.service.BaseService;
import com.base.utils.Page;
import com.common.mandate.vo.TreeDate;
import com.common.security.vo.TloginVo;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:用户管理service接口
 */
public interface UserService extends BaseService {
    /**
     * 查询用户总数
     * @param loginname 登录名
     * @param username  用户名
     * @param status 用户状态
     * @param trole 用户角色
     * @param sector 所属部门
     * @return 总数
     */
    public int getTloginSum(String loginname, String username, String status, String trole, String sector);
    /**
     * 查询用户信息
     * @param loginname 登录名
     * @param username  用户名
     * @param status 用户状态
     * @param trole 用户角色
     * @param sector 所属部门
     * @return 用户信息
     */
    public List<TloginVo> listTlogin(String loginname, String username, String status, String trole, String sector, Page p);
    /**
     * 保存用户信息
     * @param t 用户对象
     * @param tr  用户角色关联对象
     * @return
     */
    public void saveTlogin(Tlogin t, TloginTrole tr);
    /**
     * 更新用户信息
     * @param t 用户对象
     * @param troleid  角色id
     * @return
     */
    public void updateTlogin(Tlogin t, String troleid);
    /**
     * 删除用户
     * @param t 用户对象
     * @return
     */
    public void deleteTlogin(Tlogin t);
    /**
     * 判断用户信息是否重复
     * @param loginname 登录名
     * @return 是/否
     */
    public boolean getUserByLoginname(String loginname);
    /**
     * 判断用户是否存在子用户
     * @param registrationuser 登录名
     * @return 是/否
     */
    public boolean findByRegistrationuser(String registrationuser);
    /**
     * 获取部门树信息
     * @param code 父节点编码
     * @return 部门树信息
     */
    public TreeDate getSectorTree(String code);
    /**
     * 判断所属部门是否存在
     * @param sectorid 部门id
     * @return 是/否
     */
    public boolean getTsectorById(String sectorid);
}
