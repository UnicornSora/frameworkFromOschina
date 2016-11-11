package com.generator.template;

import com.base.po.${class?cap_first};
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("${class}Service")
public class ${class?cap_first}ServiceImpl extends BaseServiceImpl implements ${class?cap_first}Service {
    public int queryCount(){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(*) ");
        hql.append("   from ${class?cap_first} t   ");
        return Integer.parseInt(session.createQuery(hql.toString()).uniqueResult()+"");
    }

    public List<${class?cap_first}> queryList(Page p){
        Session session = this.getSession();
        StringBuffer hql = new StringBuffer();
        hql.append("   from ${class?cap_first} t   ");
        Query q = session.createQuery(hql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        return q.list();
    }
}