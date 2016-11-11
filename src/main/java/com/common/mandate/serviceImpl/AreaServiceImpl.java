package com.common.mandate.serviceImpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import com.base.po.Tarea;
import com.base.po.Tdepartment;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.mandate.service.AreaService;
import com.common.mandate.vo.TreeDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:行政区划模块service类
 */
@Service("areaService")
public class AreaServiceImpl extends BaseServiceImpl implements AreaService {
    /**
     * 通过父区划id查询行政区划树
     * @param areaid 父节点区划id
     * @return 行政区划树信息
     */
    public TreeDate getAreaTree(String areaid) {
        Session session = this.getSession();
        Query q = session.createQuery("from Tarea t where t.parentcode = :parentcode order by t.code ");
        List<Tarea> list = q.setString("parentcode",areaid).list();
        TreeDate tree = new TreeDate();
        List<TreeDate> children = new ArrayList<TreeDate>();
        //判断是否存在children
        if(list!=null&&!list.isEmpty()){
            for(Tarea p : list){
                TreeDate t = this.getAreaTree(p.getCode());
                children.add(t);
            }
        }
        //进行赋值
        Tarea p =(Tarea)session.createQuery("from Tarea t where t.code = :code  ").setString("code",areaid).uniqueResult();
        tree.setId(p.getCode());
        tree.setText(p.getName());
        tree.setChildren(children);
        return tree ;
    }
    /**
     * 行政区划dategrid信息总数查询
     * @param parentcode 父级行政区划代码
     * @return 总数
     */
    public int queryCount(String parentcode){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(t.areaid)  ");
        hql.append("   from Tarea t  where t.istop = 0  ");
        if(parentcode!= null&&!parentcode.equals("")){
            hql.append(" and t.parentcode = '"+parentcode+"' ");
        }
        return Integer.parseInt(session.createQuery(hql.toString()).uniqueResult()+"");
    }
    /**
     * 行政区划dategrid信息查询
     * @param parentcode 父级行政区划代码
     * @param p dategrid分页信息
     * @return 行政区划dategrid信息
     */
    public List<Tarea> queryList(String parentcode,Page p){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append("   from Tarea t  where t.istop = 0  ");
        if(parentcode!= null&&!parentcode.equals("")){
            hql.append(" and  t.parentcode = '"+parentcode+"' ");
        }
        Query q = session.createQuery(hql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        return q.list();
    }

    /**
     * 判断行政区划是否重复
     * @param code 行政区划编码
     * @param name 行政区划名称
     * @param areaid id
     * @return 是/否
     */
    public boolean getPtareaByCodeNameId(String code,String name,String areaid){
        Session session = this.getSession();
        List<Tarea> p = session.createQuery("from Tarea t where areaid != :areaid and (t.code = :code or t.name= :name)  ")
                .setString("areaid",areaid)
                .setString("code",code)
                .setString("name",name)
                .list();
        if(p!=null&&p.size()>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断行政区划是否重复
     * @param code 行政区划编码
     * @param name 行政区划名称
     * @return 是/否
     */
    public boolean getPtareaByCodeOrName(String code,String name){
        Session session = this.getSession();
        List<Tarea> p = session.createQuery("from Tarea t where t.code = :code or t.name= :name  ")
                .setString("code",code)
                .setString("name",name)
                .list();
        if(p!=null&&p.size()>0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断行政区划下是否存在单位
     * @param areacode 行政区划编码
     * @return 是/否
     */
    public boolean getDepartmentByAreaCode(String areacode){
        Session session = this.getSession();
        List<Tdepartment> p = session.createQuery("from Tdepartment t where t.areacode = :areacode  ")
                .setString("areacode",areacode)
                .list();
        if(p!=null&&p.size()>0){
            return true;
        }else{
            return false;
        }
    }

}
