package com.common.mandate.serviceImpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import com.base.po.Tlogin;
import com.base.po.Tsector;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.mandate.service.SectorService;
import com.common.mandate.vo.TreeDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:部门管理模块service类
 */
@Service("sectorService")
public class SectorServiceImpl extends BaseServiceImpl implements SectorService {
    /**
     * 获取单位信息tree
     * @param code 行政区划编码
     * @return 单位信息tree
     */
    public TreeDate getTree(String code) {
        Session session = this.getSession();
        Query q = session.createSQLQuery("select t.code,t.name,t.parentcode from  v_departmenttree t where t.parentcode = :parentcode order by t.code desc ");
        List<Object[]> list = q.setString("parentcode",code).list();
        TreeDate tree = new TreeDate();
        List<TreeDate> children = new ArrayList<TreeDate>();
        //判断是否存在children
        if(list!=null&&!list.isEmpty()){
            for(Object[] p : list){
                TreeDate t = this.getTree(p[0]+"");
                children.add(t);
            }
        }
        //进行赋值
        Object[] p =(Object[])session.createSQLQuery("select t.code,t.name,t.parentcode from  v_departmenttree t where t.code = :code  ").setString("code",code).uniqueResult();
        tree.setId(p[0]+"");
        tree.setText(p[1]+"");
        tree.setChildren(children);
        return tree ;
    }
    /**
     * 查询部门dategrid信息总数
     * @param departmenttree 所属单位
     * @param name 部门名称
     * @return 总数
     */
    public int queryCount(String departmenttree,String name){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(tsectorid) ");
        hql.append("   from Tsector t   where 1 = 1 ");
        if(departmenttree!= null&&!departmenttree.equals("")){
            hql.append("and t.tdepartmentid = '"+departmenttree+"' ");
        }
        if(name!= null&&!name.equals("")){
            hql.append(" and t.name = '"+name+"' ");
        }
        return Integer.parseInt(session.createQuery(hql.toString()).uniqueResult()+"");
    }
    /**
     * 查询部门dategrid信息
     * @param departmenttree 所属单位
     * @param name 部门名称
     * @param p dategrid分页信息
     * @return 部门dategrid信息
     */
    public List<Tsector> queryList(String departmenttree ,String name,Page p){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append("    from Tsector t   where 1 = 1 ");
        if(departmenttree!= null&&!departmenttree.equals("")){
            hql.append(" and t.tdepartmentid = '"+departmenttree+"' ");
        }
        if(name!= null&&!name.equals("")){
            hql.append(" and t.name = '"+name+"' ");
        }
        Query q = session.createQuery(hql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        return q.list();
    }
    /**
     * 判断部门是否重复
     * @param departmentid 所属单位id
     * @param name 部门名称
     * @return 是/否
     */
    public boolean getBydepartmenName(String departmentid,String name){
        Session session = this.getSession();
        List<Tsector> p = session.createQuery(" from Tsector t where t.tdepartmentid = :departmentid and t.tsectorname = :name  ")
                .setString("departmentid", departmentid)
                .setString("name",name)
                .list();
        if(p!=null&&p.size()>0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断部门是否重复
     * @param departmentid 所属单位id
     * @param name 部门名称
     * @param sectorid 部门id
     * @return 是/否
     */
    public boolean getBydepartmenNameAndId(String departmentid,String name,String sectorid){
        Session session = this.getSession();
        List<Tsector> d = session.createQuery("from Tsector t where t.tdepartmentid = :departmentid and t.tsectorname= :name and t.tsectorid != :sectorid  ")
                .setString("departmentid",departmentid)
                .setString("name",name)
                .setString("sectorid",sectorid)
                .list();
        if(d!=null&&d.size()>0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断部门下是否存在人员
     * @param sectorid 部门id
     * @return 是/否
     */
    public boolean getUserById(String sectorid){
        Session session = this.getSession();
        List<Tlogin> p = session.createQuery("from Tlogin t where t.sector = :sectorid  ")
                .setString("sectorid",sectorid)
                .list();
        if(p!=null&&p.size()>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断所属单位是否存在
     * @param departmentid 单位id
     * @return 是/否
     */
    public boolean getDepartmentById(String departmentid){
        Session session = this.getSession();
        List<Tlogin> p = session.createQuery("from Tdepartment t where t.departmentid = :departmentid  ")
                .setString("departmentid",departmentid)
                .list();
        if(p!=null&&p.size()>0){
            return true;
        }else{
            return false;
        }
    }
}