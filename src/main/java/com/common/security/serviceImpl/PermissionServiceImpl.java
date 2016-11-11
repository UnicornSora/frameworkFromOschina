package com.common.security.serviceImpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import com.base.po.Tpermission;
import com.base.serviceImpl.BaseServiceImpl;
import com.common.security.service.PermissionService;
import com.common.security.vo.TreeAttr;
import com.common.security.vo.TreeNew;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:权限管理service类
 */

@Service("permissionService")
public class PermissionServiceImpl extends BaseServiceImpl implements PermissionService {
    /**
     * 查询权限tree
     * @param pid 父节点id
     * @return 权限tree
     */
    public List<TreeNew> getTree(String pid) {
        Session session = this.getSession();
        Query query = session.createQuery(" from Tpermission t where t.parentid = :parentid order by t.orders ");
        query.setString("parentid", pid);
        List<Tpermission> list = query.list();
        List<TreeNew> list_p = new ArrayList<TreeNew>();
        for(Iterator<?> iterator = list.iterator();iterator.hasNext();){
            Tpermission t = (Tpermission) iterator.next();
            TreeNew v = new TreeNew();
            v.setId(t.getTpermissionid());
            v.setText(t.getPermissionname());
            //判断是否存在下级菜单，如果存在则将state设为closed,如果不存在设为空
            Query q2 = session.createQuery(" select count(tpermissionid) from Tpermission t where t.parentid = :parentid ");
            q2.setString("parentid",t.getTpermissionid()) ;
            String count = q2.uniqueResult()+"";
            if(count.equals("0")){
                v.setState("");
            }else{
                v.setState("closed");
            }
            TreeAttr attr = new TreeAttr();
            //判断如果为按钮，则设为叶子
            if((t.getAction()+"").equals("2")){
                attr.setIsleaf(true);
            }else{
                attr.setIsleaf(false);
            }
            attr.setUrl(t.getUrl());
            attr.setParentid(t.getParentid());
            v.setAttributes(attr);
            list_p.add(v);
        }
        return list_p;
    }
    /**
     * 判断权限名称是否重复
     * @param permissionname 系统参数id
     * @return 是/否
     */
    public boolean getTpermissionByPermissionname(String permissionname){
        Session session = this.getSession();
        Query q = session.createQuery("select count(t.tpermissionid) from Tpermission t where t.permissionname = :permissionname  ");
        q.setString("permissionname", permissionname);
        String count = q.uniqueResult() + "";
        if (count.equals("0")) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 判断权限名称是否重复
     * @param tpermissionid 权限id
     * @param permissionname 系统参数id
     * @return 是/否
     */
    public boolean getByPermissionname(String permissionname,String tpermissionid){
        Session session = this.getSession();
        Query q = session.createQuery("select count(t.tpermissionid) from Tpermission t where t.permissionname = :permissionname  and t.tpermissionid != :tpermissionid ");
        q.setString("permissionname", permissionname);
        q.setString("tpermissionid", tpermissionid);
        String count = q.uniqueResult() + "";
        if (count.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判读是否存在使用该权限的角色
     * @param tpermissionid 权限id
     * @return 是/否
     */
    public boolean getTroleTpermission(String tpermissionid ){
        Session session = this.getSession();
        Query q = session.createQuery("select count(t.troleTpermissionid) from TroleTpermission t where t.tpermissionid = :tpermissionid  ");
        q.setString("tpermissionid", tpermissionid);
        String count = q.uniqueResult()+"";
        if(count.equals("0")){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 判读该权限是否存在子权限
     * @param tpermissionid 权限id
     * @return 是/否
     */
    public boolean getSunTpermissionCount(String tpermissionid ){
        Session session = this.getSession();
        Query q = session.createQuery(" select count(t.tpermissionid) from Tpermission t where t.parentid = :parentid ");
        q.setString("parentid", tpermissionid);
        String count = q.uniqueResult()+"";
        if(count.equals("0")){
            return false;
        }else{
            return true;
        }
    }
}