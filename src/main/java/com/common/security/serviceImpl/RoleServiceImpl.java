package com.common.security.serviceImpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import com.base.po.Trole;
import com.base.po.TroleTpermission;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.security.service.RoleService;
import com.common.security.vo.TreeAttr;
import com.common.security.vo.TreeNew;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:角色管理service类
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {
    /**
     * 查询角色总数
     * @param rolename 角色名
     * @return 总数
     */
    public int queryCount(String rolename){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(t.troleid)  ");
        hql.append("   from Trole t   ");
        if(rolename!= null&&!rolename.equals("")){
            hql.append(" where t.rolename = '"+rolename+"' ");
        }
        return Integer.parseInt(session.createQuery(hql.toString()).uniqueResult()+"");
    }
    /**
     * 查询角色信息
     * @param rolename 角色名
     * @param p 分页信息
     * @return 角色信息
     */
    public List<Trole> queryList(String rolename,Page p){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append("   from Trole t   ");
        if(rolename!= null&&!rolename.equals("")){
            hql.append(" where  t.rolename = '"+rolename+"' ");
        }
        Query q = session.createQuery(hql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        return q.list();
    }
    /**
     * 判断角色信息是否重复
     * @param rolename 角色名
     * @return 是/否
     */
    public boolean getTroleByRolename(String rolename){
        Session session = this.getSession();
        Query q = session.createQuery("from Trole t where t.rolename = :rolename  ");
        q.setString("rolename", rolename);
        Trole t = (Trole) q.uniqueResult();
        if(t == null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 判断角色信息是否重复
     * @param rolename 角色名
     * @param troleid 角色id
     * @return 是/否
     */
    public boolean getTroleByRolename(String rolename,String troleid){
        Session session = this.getSession();
        Query q = session.createQuery("from Trole t where troleid != :troleid and t.rolename = :rolename  ");
        q.setString("troleid",troleid);
        q.setString("rolename", rolename);
        Trole t = (Trole) q.uniqueResult();
        if(t == null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 判断是否存在使用该角色的用户
     * @param troleid 角色id
     * @return 是/否
     */
    public boolean getTlogintrole(String troleid){
        Session session = this.getSession();
        Query q = session.createQuery("select count(t.tlogintroleid) from TloginTrole t where t.troleid = :troleid  ");
        q.setString("troleid", troleid);
        String count = q.uniqueResult()+"";
        if(count.equals("0")){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 删除角色
     * @param troleid 角色id
     * @return
     */
    public void deleteTrole(String troleid){
        Session session = this.getSession();
        //删除trole
        Trole t = (Trole) session.get(Trole.class, troleid);
        session.delete(t);
        //删除角色权限关联关系
        session.createQuery(" delete from TroleTpermission where troleid = :troleid  ").setString("troleid", troleid).executeUpdate();
        //删除activiti user group
        session.createSQLQuery(" delete from act_id_group where ID_ = :rolename  ").setString("rolename", t.getRolename()).executeUpdate();
    }
    /**
     * 获取角色权限树
     * @param pid 父id
     * @param roleid 角色id
     * @return 权限树
     */
    public List<TreeNew> getTree(String pid,String roleid) {
        StringBuffer sql = new StringBuffer();
        sql.append("  select a.tpermissionid,a.permissionname,a.action,a.url,a.parentid,b.troleid    ");
        sql.append(" from t_permission a ");
        sql.append(" left join t_roletpermission b ");
        sql.append(" on a.tpermissionid = b.tpermissionid and b.troleid = :troleid ");
        sql.append(" where a.parentid = :parentid ");
        sql.append(" order by a.orders  ");
        Session session = this.getSession();
        Query query = session.createSQLQuery(sql.toString());
        query.setString("troleid", roleid);
        query.setString("parentid", pid);
        List<Object[]> list = query.list();
        List<TreeNew> list_p = new ArrayList<TreeNew>();
        for(int i = 0;i<list.size();i++){
            Object[] o= list.get(i);
            TreeNew v = new TreeNew();
            v.setId(o[0].toString());
            v.setText(o[1].toString());
            //判断是否存在下级菜单，如果存在则将state设为closed,如果不存在设为空
            Query q2 = session.createQuery(" select count(tpermissionid) from Tpermission t where t.parentid = :parentid ");
            q2.setString("parentid",o[0].toString()) ;
            String count = q2.uniqueResult()+"";
            if(count.equals("0")){
                v.setState("");
            }else{
                v.setState("closed");
            }
            v.setChecked(o[5] == null ? false : true);
            TreeAttr attr = new TreeAttr();
            attr.setIsleaf((o[2].toString()+"").equals("2") ? true : false);
            attr.setUrl(o[3].toString());
            attr.setParentid(o[4].toString());
            v.setAttributes(attr);
            list_p.add(v);
        }
        return list_p;
    }
    /**
     * 角色赋权限
     * @param role 权限树字符串
     * @param id 角色id
     * @return 权限树
     */
    public void role_Save(String role,String id){
        String [] tpermissionid = role.split(",");
        Session session = this.getSession();
        Query query = session.createQuery(" delete from TroleTpermission where troleid = :troleid ");
        query.setString("troleid", id);
        query.executeUpdate();
        for(int i= 0;i<tpermissionid.length;i++){
            TroleTpermission t = new TroleTpermission();
            t.setTroleid(id);
            t.setTpermissionid(tpermissionid[i]);
            session.save(t);
        }
    }
    /**
     * 添加角色
     * @param rolename 角色名称
     * @return
     */
    public void roleSave(String rolename){
        Session session = this.getSession();
        Trole t = new Trole();
        t.setRolename(rolename);
        session.save(t);
        session.createSQLQuery(" insert into  act_id_group(ID_) values (:rolename)").setString("rolename",rolename).executeUpdate();
    }
    /**
     * 更新角色
     * @param id 角色id
     * @param rolename 角色名称
     * @return
     */
    public void roleUpdate(String id,String rolename){
        Session session = this.getSession();
        Trole t =  (Trole) session.get(Trole.class,id);
        //修改activiti
        session.createSQLQuery(" update  act_id_group set ID_ =:rolename_new where ID_ =:rolename_old")
            .setString("rolename_new",rolename )
            .setString("rolename_old",t.getRolename())
            .executeUpdate();
        //保存对象
        t.setRolename(rolename);
        session.update(t);
    }


}