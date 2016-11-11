package com.common.security.serviceImpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import com.base.po.Tlogin;
import com.base.po.TloginTrole;
import com.base.po.Trole;
import com.base.po.Tsector;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.MD5;
import com.base.utils.Page;
import com.common.mandate.vo.TreeDate;
import com.common.security.service.UserService;
import com.common.security.vo.TloginVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:用户管理service类
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {
    /**
     * 查询用户总数
     * @param loginname 登录名
     * @param username  用户名
     * @param status 用户状态
     * @param trole 用户角色
     * @param sector 所属部门
     * @return 总数
     */
    public int getTloginSum(String loginname,String username,String status,String trole,String sector){
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(t.tloginid) ");
        sql.append("  from t_login t  ");
        sql.append("  left join t_logintrole a on a.tloginid = t.tloginid ");
        sql.append(" where 1 = 1 ");
        if(loginname!= null&&!loginname.equals("")){
            sql.append(" and t.loginname = '"+loginname+"' ");
        }
        if(username!= null&&!username.equals("")){
            sql.append(" and t.username = '"+username+"' ");
        }
        if(trole!= null&&!trole.equals("")){
            sql.append(" and a.troleid = "+trole+" ");
        }
        if(status!= null&&!status.equals("")){
            sql.append(" and t.status = "+status+" ");
        }
        if(sector!= null&&!sector.equals("")){
            sql.append(" and t.sector = "+sector+" ");
        }
        return Integer.parseInt(this.getSession().createSQLQuery(sql.toString()).uniqueResult()+"");
    }
    /**
     * 查询用户信息
     * @param loginname 登录名
     * @param username  用户名
     * @param status 用户状态
     * @param trole 用户角色
     * @param sector 所属部门
     * @return 用户信息
     */
    public List<TloginVo> listTlogin(String loginname,String username,String status,String trole,String sector,Page p){
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.tloginid,t.loginname,t.username,t.phone,t.registrationtime,t.status,a.troleid,t.sector ");
        sql.append("  from t_login t  ");
        sql.append("  left join t_logintrole a on a.tloginid = t.tloginid ");
        sql.append(" where 1 = 1 ");
        if(loginname!= null&&!loginname.equals("")){
            sql.append(" and t.loginname = '"+loginname+"' ");
        }
        if(username!= null&&!username.equals("")){
            sql.append(" and t.username = '"+username+"' ");
        }
        if(trole!= null&&!trole.equals("")){
            sql.append(" and a.troleid = "+trole+" ");
        }
        if(status!= null&&!status.equals("")){
            sql.append(" and t.status = "+status+" ");
        }
        if(sector!= null&&!sector.equals("")){
            sql.append(" and t.sector = "+sector+" ");
        }
        sql.append(" order by t.tloginid ");
        Query q = this.getSession().createSQLQuery(sql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        List<Object[]> listobj = q.list();
        List<TloginVo> list = new ArrayList();
        for(Object[] obj : listobj){
            TloginVo vo = new TloginVo();
            vo.setTloginid(obj[0].toString());
            vo.setLoginname(obj[1].toString());
            vo.setUsername(obj[2].toString());
            if(obj[3] != null&&!obj[3].toString().equals("")){
                vo.setPhone(obj[3]+"");
            }else{
                vo.setPhone("");
            }
            vo.setRegistrationtime((Date)obj[4]);
            vo.setStatus(obj[5].toString());
            vo.setTroleid(obj[6].toString());
            vo.setSector(obj[7].toString());
            list.add(vo);
        }
        return list;
    }
    /**
     * 保存用户信息
     * @param t 用户对象
     * @param tr  用户角色关联对象
     * @return
     */
    public void saveTlogin(Tlogin t,TloginTrole tr){
        Session session = this.getSession();
        MD5 m = new MD5();
        t.setPassword(m.getMD5ofStr(t.getPassword()));
        session.save(t);
        tr.setTloginid(t.getTloginid());
        session.save(tr);
        //保存activiti user
        session.createSQLQuery(" insert into  act_id_user(ID_) values (:username)").setString("username",t.getLoginname()).executeUpdate();
        //保存activiti act_id_membership
        Trole r = (Trole) session.get(Trole.class,tr.getTroleid());
        session.createSQLQuery(" insert into  act_id_membership(USER_ID_,GROUP_ID_) values (:username,:rolename)").setString("username", t.getLoginname()).setString("rolename",r.getRolename()).executeUpdate();
    }
    /**
     * 更新用户信息
     * @param t 用户对象
     * @param troleid  角色id
     * @return
     */
    public void updateTlogin(Tlogin t,String troleid){
        Session session = this.getSession();
        Tlogin t1 = (Tlogin) session.get(Tlogin.class, t.getTloginid());
        t1.setUsername(t.getUsername());
        t1.setStatus(t.getStatus());
        t1.setSector(t.getSector());
        t1.setPhone(t.getPhone());
        session.update(t1);
        Query q = session.createQuery(" update  TloginTrole t set t.troleid = :troleid where t.tloginid = :tloginid  ");
        q.setString("troleid", troleid);
        q.setString("tloginid", t.getTloginid());
        q.executeUpdate();
        Trole r = (Trole) session.get(Trole.class,troleid);
        session.createSQLQuery("  update act_id_membership set GROUP_ID_ =:rolename where USER_ID_ =:username ").setString("rolename",r.getRolename()).setString("username", t1.getLoginname()).executeUpdate();
    }
    /**
     * 删除用户
     * @param t 用户对象
     * @return
     */
    public void deleteTlogin(Tlogin t){
        Session session = this.getSession();
        //删除关联数据
        Query q = session.createQuery(" delete from TloginTrole t  where t.tloginid = :tloginid ");
        q.setString("tloginid", t.getTloginid());
        q.executeUpdate();
        //删除用户对象
        session.delete(t);
        //保存activiti act_id_membership
        session.createSQLQuery(" delete from  act_id_membership where USER_ID_ = :username ").setString("username", t.getLoginname()).executeUpdate();
        //删除activiti user
        session.createSQLQuery(" delete from  act_id_user where ID_ = :username ").setString("username",t.getLoginname()).executeUpdate();
    }
    /**
     * 判断用户信息是否重复
     * @param loginname 登录名
     * @return 是/否
     */
    public boolean getUserByLoginname(String loginname){
        Session session = this.getSession();
        Query q = session.createQuery("from Tlogin t where t.loginname = :loginname ");
        q.setString("loginname", loginname);
        Tlogin t = (Tlogin) q.uniqueResult();
        if(t==null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 判断用户是否存在子用户
     * @param registrationuser 登录名
     * @return 是/否
     */
    public boolean findByRegistrationuser(String registrationuser){
        Query  q = this.getSession().createQuery("select count(t.tloginid) from Tlogin t where t.registrationuser =:registrationuser ");
        q.setString("registrationuser", registrationuser);
        String count =  q.uniqueResult()+"";
        if(count.equals("0")){
            return false;
        }else{
            return  true;
        }
    }
    /**
     * 获取部门树信息
     * @param code 父节点编码
     * @return 部门树信息
     */
    public TreeDate getSectorTree(String code) {
        Session session = this.getSession();
        Query q = session.createSQLQuery("select t.code,t.name,t.parentcode from  v_sectortree t where t.parentcode = :parentcode order by t.code desc ");
        List<Object[]> list = q.setString("parentcode",code).list();
        TreeDate tree = new TreeDate();
        List<TreeDate> children = new ArrayList<TreeDate>();
        //判断是否存在children
        if(list!=null&&!list.isEmpty()){
            for(Object[] p : list){
                TreeDate t = this.getSectorTree(p[0] + "");
                children.add(t);
            }
        }
        //进行赋值
        Object[] p =(Object[])session.createSQLQuery("select t.code,t.name,t.parentcode from  v_sectortree t where t.code = :code  ").setString("code",code).uniqueResult();
        tree.setId(p[0]+"");
        tree.setText(p[1]+"");
        tree.setChildren(children);
        return tree ;
    }
    /**
     * 判断所属部门是否存在
     * @param sectorid 部门id
     * @return 是/否
     */
    public boolean getTsectorById(String sectorid){
        Session session = this.getSession();
        List<Tsector> d = session.createQuery("from Tsector t where t.tsectorid = :sectorid  ")
                .setString("sectorid", sectorid)
                .list();
        if(d!=null&&d.size()>0){
            return true;
        }else{
            return false;
        }
    }
}
