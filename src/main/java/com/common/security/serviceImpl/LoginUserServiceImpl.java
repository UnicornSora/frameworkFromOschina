package com.common.security.serviceImpl;

import org.hibernate.Query;
import org.springframework.stereotype.Service;
import com.base.po.Tlogin;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.MD5;
import com.common.security.service.LoginUserService;
import com.common.security.vo.LoginUserVO;
import com.common.security.vo.TreeAttr;
import com.common.security.vo.TreeNew;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:用户登录service类
 */
@Service("loginUserService")
public class LoginUserServiceImpl extends BaseServiceImpl implements LoginUserService {
    /**
     * 查询日志信息总数
     * @return 总数
     */
    public LoginUserVO loginUser(String loginname){
        StringBuffer sql = new StringBuffer();
        sql = sql.append(" select t.tloginid,t.loginname,t.password,t.username, ");
        sql = sql.append("       (select t1.rolename ");
        sql = sql.append("        from   t_role t1 ");
        sql = sql.append("        where  t1.troleid =(select t2.troleid from t_logintrole t2 where  t2.tloginid = t.tloginid)) ");
        sql = sql.append(" from   t_login t ");
        sql = sql.append(" where  t.status = 1 and t.loginname = :loginname ");
        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setString("loginname", loginname);
        List<Object[]> list = query.list();

        Object[] u = list.get(0);
        LoginUserVO vo = new LoginUserVO();
        vo.setLoginid(u[0].toString());
        vo.setLoginname(u[1].toString());
        vo.setPassword(u[2].toString());
        vo.setUsername(u[3].toString());
        vo.setRolename(u[4].toString());
        return vo ;
    }
    /**
     * 获取用户权限树
     * @param rolename 角色名
     * @param pid 父ID
     * @return 用户权限树
     */
    public List<TreeNew> getUserTree(String rolename, String pid) {
        StringBuffer sql = new StringBuffer();
        sql = sql.append(" select a.tpermissionid,a.permissionname,a.action,a.url,a.orders,a.parentid ");
        sql = sql.append("   from t_roletpermission t ");
        sql = sql.append("   left join t_permission a on a.action != 2 and a.tpermissionid = t.tpermissionid ");
        sql = sql.append("  where t.troleid in (select b.troleid from t_role b where b.rolename = :rolename) and a.parentid = :parentid ");
        sql = sql.append("  order by a.orders ");
        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setString("rolename", rolename);
        query.setString("parentid", pid);
        List<Object[]> list = query.list();
        List<TreeNew> list_p = new ArrayList<TreeNew>();
        for (int i = 0; i < list.size(); i++) {
            Object[] o = list.get(i);
            TreeNew v = new TreeNew();
            TreeAttr attr = new TreeAttr();
            v.setId(o[0].toString());
            v.setText(o[1].toString());
            v.setState((o[2].toString()).equals("1") ? "" : "closed");
            attr.setIsleaf((o[2].toString()).equals("1") ? true : false);
            attr.setUrl(o[3].toString());
            attr.setParentid(o[5].toString());
            v.setAttributes(attr);
            list_p.add(v);
        }
        return list_p;
    }

    /**
     * 获取用户对象
     * @param loginname 登录名
     * @param password_old 密码
     * @return 用户对象
     */
    public Tlogin getPasssWordOld(String loginname , String password_old){
        MD5 md5 = new MD5();
        Query q = this.getSession().createQuery(" from Tlogin t where t.loginname = :loginname and t.password = :password ");
        q.setString("loginname", loginname);
        q.setString("password", md5.getMD5ofStr(password_old));
        return (Tlogin)q.uniqueResult();
    }

}
