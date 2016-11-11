package com.common.shiro.serviceImpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import com.base.serviceImpl.BaseServiceImpl;
import com.common.shiro.service.ShiroService;
import java.util.List;


/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:shiro service类
 */
@Service("shiroService")
public class ShiroServiceImpl extends BaseServiceImpl implements ShiroService {
    /**
     * 判断用户名是否存在
     * @param loginname 登录名
     * @return 是/否
     */
    public boolean findLoginname(String loginname){
        Session session = this.getSession();
        Query q = session.createQuery("select  count(tloginid) from Tlogin where loginname = :loginname ");
        q.setString("loginname",loginname);
        String count = q.uniqueResult()+"";
        if(count != null && count.equals("1")){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 判断登录名、密码是否正确
     * @param loginname 登录名
     * @param password 密码
     * @return 是/否
     */
    public boolean getUserByLoginname(String loginname,String password){
        Session session = this.getSession();
        Query q = session.createQuery("select  count(tloginid) from Tlogin where loginname = :loginname and password = :password");
        q.setString("loginname",loginname);
        q.setString("password",password);
        String count = q.uniqueResult()+"";
        if(count != null && count.equals("1")){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 获取用户权限列表
     * @param loginname 登录名
     * @return 用户权限列表
     */
    public List<String> getPermissionsByLoginname(String loginname) {
        //根据登录名获取用户角色id
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.troleid ");
        sql.append("   from t_logintrole t ");
        sql.append("   left join t_login a on  t.tloginid = a.tloginid ");
        sql.append("  where a.loginname = :loginname ");
        String troleid = this.getSession().createSQLQuery(sql.toString()).setString("loginname",loginname).uniqueResult()+"";
        //根据角色id获取用户权限url
        StringBuffer sql2 = new StringBuffer();
        sql2.append(" select distinct t.url  ");
        sql2.append("   from t_permission t  ");
        sql2.append("  where t.tpermissionid in (select a.tpermissionid from t_roletpermission a where a.troleid = :troleid) ");
        List<String> list = this.getSession().createSQLQuery(sql2.toString()).setString("troleid",troleid).list();
        return list;
    }
}
