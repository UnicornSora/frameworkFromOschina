package com.base.dao;
import org.hibernate.Session;

/**
 * @author: football98
 * @createTime:: 16-9-28
 * @classDescription: 基础dao接口
 */
public interface BaseDao {
    /**
     * 获取Session对象
     * @param
     * @return Session
     */
    public Session getSession();
}
