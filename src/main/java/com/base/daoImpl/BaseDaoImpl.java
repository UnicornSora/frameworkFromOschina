package com.base.daoImpl;

import com.base.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;

/**
 * @author: football98
 * @createTime:: 16-9-28
 * @classDescription: 基础dao类
 */
@Repository("baseDao")
public class BaseDaoImpl implements BaseDao {

    @Autowired
    private HibernateTemplate template;

    /**
     * 获取Session对象
     * @param
     * @return Session
     */
    public Session getSession(){
        return template.getSessionFactory().getCurrentSession();
    }
}
