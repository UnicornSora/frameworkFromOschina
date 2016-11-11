package com.common.security.serviceImpl;

import org.hibernate.Query;
import org.springframework.stereotype.Service;
import com.base.po.Tdictionary;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.security.service.DictionaryService;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:数据字典模块service类
 */
@Service("dictionaryService")
public class DictionaryServiceImpl extends BaseServiceImpl implements DictionaryService {
    /**
     * 查询字典信息总数
     * @param type 类别
     * @param code 值
     * @param value 含义
     * @return 总数
     */
    public int queryCount(String type,String code,String value){
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(t.tdictionaryid)  ");
        hql.append("   from Tdictionary t   ");
        hql.append("  where 1 = 1  ");
        if(type!= null&&!type.equals("")){
            hql.append(" and t.type = '"+type+"' ");
        }
        if(code!= null&&!code.equals("")){
            hql.append(" and t.code = '"+code+"' ");
        }
        if(value!= null&&!value.equals("")){
            hql.append(" and t.vaule = '"+value+"' ");
        }
        hql.append("  order by t.type,t.code ");
        return Integer.parseInt(this.getSession().createQuery(hql.toString()).uniqueResult()+"");
    }
    /**
     * 查询字典信息
     * @param type 类别
     * @param code 值
     * @param value 含义
     * @param p 分页信息
     * @return 字典信息
     */
    public List<Tdictionary> queryList(String type,String code,String value,Page p){
        StringBuffer hql = new StringBuffer();
        hql.append("   from Tdictionary t   ");
        hql.append("  where 1 = 1  ");
        if(type!= null&&!type.equals("")){
            hql.append(" and t.type = '"+type+"' ");
        }
        if(code!= null&&!code.equals("")){
            hql.append(" and t.code = '"+code+"' ");
        }
        if(value!= null&&!value.equals("")){
            hql.append(" and t.vaule = '"+value+"' ");
        }
        hql.append("  order by t.type,t.code ");

        Query q = this.getSession().createQuery(hql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        return q.list();
    }
    /**
     * 判断字典信息是否重复
     * @param type 类别
     * @param code 值
     * @param value 含义
     * @return 是/否
     */
    public boolean getDictionaryByType(String type,String code,String value){
        Query q = this.getSession().createQuery("from Tdictionary t where t.type = :type and t.code = :code and t.value= :value ");
        q.setString("type", type);
        q.setString("code", code);
        q.setString("value", value);
        Tdictionary t = (Tdictionary) q.uniqueResult();
        if(t == null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 判断字典信息是否重复
     * @param tdictionaryid 字典id
     * @param type 类别
     * @param code 值
     * @param value 含义
     * @return 是/否
     */
    public boolean getDictionaryByType(String type, String code, String value,int tdictionaryid){
        Query q = this.getSession().createQuery("from Tdictionary t where tdictionaryid != :tdictionaryid and (t.type = :type and t.code = :code and t.value= :value) ");
        q.setInteger("tdictionaryid",tdictionaryid);
        q.setString("type", type);
        q.setString("code", code);
        q.setString("value", value);
        Tdictionary t = (Tdictionary) q.uniqueResult();
        if(t == null){
            return false;
        }else{
            return true;
        }
    }

}
