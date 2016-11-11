package com.common.security.serviceImpl;

import org.hibernate.Query;
import org.springframework.stereotype.Service;
import com.base.po.Tparameter;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.security.service.ParameterService;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:系统参数管理service类
 */
@Service("parameterService")
public class ParameterServiceImpl extends BaseServiceImpl implements ParameterService {
    /**
     * 查询系统参数总数
     * @param type 类别
     * @return 总数
     */
    public int queryCount(String type){
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(t.tparameterid)  ");
        hql.append("   from Tparameter t   ");
        hql.append("  where 1 = 1  ");
        if(type!= null&&!type.equals("")){
            hql.append(" and t.type = '"+type+"' ");
        }
        hql.append("  order by t.type ");
        return Integer.parseInt(this.getSession().createQuery(hql.toString()).uniqueResult()+"");
    }
    /**
     * 查询系统参数信息
     * @param type 类别
     * @param p 分页信息
     * @return 系统参数信息
     */
    public List<Tparameter> queryList(String type,Page p){
        StringBuffer hql = new StringBuffer();
        hql.append("   from Tparameter t   ");
        hql.append("  where 1 = 1  ");
        if(type!= null&&!type.equals("")){
            hql.append(" and t.type = '"+type+"' ");
        }
        hql.append("  order by t.type ");

        Query q = this.getSession().createQuery(hql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        return q.list();
    }
    /**
     * 判断系统参数是否重复
     * @param area 所属行政区划
     * @param type 类别
     * @return 是/否
     */
    public boolean getParameterByType(String area,String type){
        Query q = this.getSession().createQuery("from Tparameter t where t.area = :area and t.type = :type ");
        q.setString("area", area);
        q.setString("type", type);
        Tparameter t = (Tparameter) q.uniqueResult();
        if(t == null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 判断系统参数是否重复
     * @param tparameterid 系统参数id
     * @param area 所属行政区划
     * @param type 类别
     * @return 是/否
     */
    public boolean getParameterByType(String area,String type,String tparameterid){
        Query q = this.getSession().createQuery("from Tparameter t where t.tparameterid <> :tparameterid and t.area = :area and t.type = :type ");
        q.setString("tparameterid", tparameterid);
        q.setString("area", area);
        q.setString("type", type);
        Tparameter t = (Tparameter) q.uniqueResult();
        if(t == null){
            return false;
        }else{
            return true;
        }
    }
}