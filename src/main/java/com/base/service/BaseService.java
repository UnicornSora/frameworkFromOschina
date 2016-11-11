package com.base.service;

import com.base.po.Tarea;
import com.base.po.Tparameter;
import com.base.vo.ComboBox;
import java.io.Serializable;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: 基本服务接口
 */
public interface BaseService {
    /**
     * 获取字典信息
     * @param type 字典类别
     * @param isnull 是否添加空值
     * @return List<ComboBox> 字典信息
     */
    public List<ComboBox> getComboBox(String type,String isnull);
    /**
     * 获取系统参数信息
     * @param type 参数类别
     * @return 系统参数信息
     */
    public Tparameter getTparameter(String type);
    /**
     * 保存方法
     * @param t po
     * @return
     */
    public <T> void save(T t);
    /**
     * 更新方法
     * @param t po
     * @return
     */
    public <T> void update(T t);
    /**
     * 删除方法
     * @param t po
     * @return
     */
    public <T> void delete(T t);
    /**
     * 通过class、序列号id删除
     * @param objectClass 类.class
     * @param id 序列号id
     * @return
     */
    public <T> void delete(Class<T> objectClass, Serializable id);
    /**
     * 通过class、序列号id获取po对象
     * @param objectClass 类.class
     * @param id 序列号id
     * @return T po对象
     */
    public <T> T get(Class<T> objectClass, Serializable id);
    /**
     * 获取顶级区划对象
     * @param
     * @return Tarea po对象
     */
    public Tarea getTopArea();
}
