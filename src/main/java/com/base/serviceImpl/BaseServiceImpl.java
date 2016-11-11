package com.base.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import com.base.dao.BaseDao;
import com.base.po.Tarea;
import com.base.po.Tparameter;
import com.base.service.BaseService;
import com.base.vo.ComboBox;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: 基本服务类
 */
@Service("baseService")
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseDao baseDao;

    public Session getSession(){
        return baseDao.getSession();
    }

    /**
     * 获取字典信息
     * @param type 字典类别
     * @param isnull 是否添加空值
     * @return List<ComboBox> 字典信息
     */
    public List<ComboBox> getComboBox(String type,String isnull){
        Query q = this.getSession().createSQLQuery(" select t.code,t.value from v_tdictionary t where t.type = :type  order  by t.code   ");
        q.setString("type",type );
        List<Object[]> list = q.list();
        List<ComboBox> listComboBox = new ArrayList<ComboBox>();
        //添加空值
        if(isnull != null&&isnull.equals("true")){
            ComboBox c2 = new ComboBox();
            c2.setId("");
            c2.setText("请选择");
            listComboBox.add(c2);
        }

        for(Object[] o : list){
            ComboBox c = new ComboBox();
            c.setId(o[0].toString());
            c.setText(o[1].toString());
            listComboBox.add(c);
        }

        return listComboBox;
    }

    /**
     * 获取系统参数信息
     * @param type 参数类别
     * @return 系统参数信息
     */
    public Tparameter getTparameter(String type){
        Query q = this.getSession().createQuery("from Tparameter t where t.type = :type ");
        q.setString("type", type);
        return (Tparameter)q.uniqueResult();
    }
    /**
     * 保存方法
     * @param t po
     * @return
     */
    public <T> void save(T t){
        Session session = this.getSession();
        session.save(t);
        session.flush();
    }
    /**
     * 更新方法
     * @param t po
     * @return
     */
    public <T> void update(T t){
        Session session = this.getSession();
        session.update(t);
        session.flush();
    }
    /**
     * 删除方法
     * @param t po
     * @return
     */
    public <T> void delete(T t){
        Session session = this.getSession();
        session.delete(t);
        session.flush();
    }
    /**
     * 通过class、序列号id删除
     * @param objectClass 类.class
     * @param id 序列号id
     * @return
     */
    public <T> void delete(Class<T> objectClass, Serializable id){
        Session session = this.getSession();
        T t = (T) this.get(objectClass, id);
        session.delete(t);
        session.flush();
    }
    /**
     * 通过class、序列号id获取po对象
     * @param objectClass 类.class
     * @param id 序列号id
     * @return T po对象
     */
    public <T> T get(Class<T> objectClass, Serializable id){
        Session session = this.getSession();
        return (T) session.get(objectClass, (Serializable) id);
    }

    /**
     * 获取顶级区划对象
     * @param
     * @return Tarea po对象
     */
    public Tarea getTopArea(){
        Session session = this.getSession();
        Tarea p = (Tarea) session.createQuery("from Tarea t where t.istop = 1 ").uniqueResult();
        return p;
    }
}

