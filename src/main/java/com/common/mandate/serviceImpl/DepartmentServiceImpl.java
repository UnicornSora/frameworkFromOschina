package com.common.mandate.serviceImpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import com.base.po.Tdepartment;
import com.base.po.Tsector;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.mandate.service.DepartmentService;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:单位管理模块service类
 */
@Service("departmentService")
public class DepartmentServiceImpl extends BaseServiceImpl implements DepartmentService {
    /**
     * 查询单位dategrid信息总数
     * @param areacode 所属行政区划编码
     * @param name 单位名称
     * @return 总数
     */
    public int queryCount(String areacode,String name){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(t.departmentid)  ");
        hql.append("   from Tdepartment t  where 1 = 1  ");
        if(areacode!= null&&!areacode.equals("")){
            hql.append(" and t.areacode = '"+areacode+"' ");
        }
        if(name!= null&&!name.equals("")){
            hql.append(" and t.name = '"+name+"' ");
        }
        return Integer.parseInt(session.createQuery(hql.toString()).uniqueResult()+"");
    }
    /**
     * 单位dategrid信息查询
     * @param areacode 所属行政区划编码
     * @param name 单位名称
     * @param p dategrid分页信息
     * @return 单位dategrid信息
     */
    public List<Tdepartment> queryList(String areacode ,String name, Page p){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append("   from Tdepartment t  where 1 = 1  ");
        if(areacode!= null&&!areacode.equals("")){
            hql.append(" and t.areacode = '"+areacode+"' ");
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
     * 判断单位是否重复
     * @param areacode 所属行政区划编码
     * @param name 单位名称
     * @return 是/否
     */
    public boolean getByAreaCodeAndName(String areacode,String name){
        Session session = this.getSession();
        List<Tdepartment> d = session.createQuery("from Tdepartment t where t.areacode = :areacode and t.name= :name  ")
                .setString("areacode",areacode)
                .setString("name",name)
                .list();
        if(d!=null&&d.size()>0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断单位是否重复
     * @param areacode 所属行政区划编码
     * @param name 单位名称
     * @param departmentid 单位id
     * @return 是/否
     */
    public boolean getByAreaCodeAndNameAndId(String areacode,String name,String departmentid){
        Session session = this.getSession();
        List<Tdepartment> d = session.createQuery("from Tdepartment t where t.areacode = :areacode and t.name= :name and departmentid != :departmentid  ")
                .setString("areacode",areacode)
                .setString("name",name)
                .setString("departmentid",departmentid)
                .list();
        if(d!=null&&d.size()>0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断单位下是否存在部门
     * @param departmentid 单位id
     * @return 是/否
     */
    public boolean getSectorById(String departmentid){
        Session session = this.getSession();
        List<Tsector> p = session.createQuery("from Tsector t where t.tdepartmentid = :departmentid  ")
                .setString("departmentid",departmentid)
                .list();
        if(p!=null&&p.size()>0){
            return true;
        }else{
            return false;
        }
    }
}
