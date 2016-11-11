package com.common.security.service;

import com.base.po.Tdictionary;
import com.base.service.BaseService;
import com.base.utils.Page;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:数据字典模块service接口
 */
public interface DictionaryService extends BaseService {
    /**
     * 查询字典信息总数
     * @param type 类别
     * @param code 值
     * @param value 含义
     * @return 总数
     */
    public int queryCount(String type, String code, String value);
    /**
     * 查询字典信息
     * @param type 类别
     * @param code 值
     * @param value 含义
     * @param p 分页信息
     * @return 字典信息
     */
    public List<Tdictionary> queryList(String type, String code, String value, Page p);
    /**
     * 判断字典信息是否重复
     * @param type 类别
     * @param code 值
     * @param value 含义
     * @return 是/否
     */
    public boolean getDictionaryByType(String type, String code, String value);
    /**
     * 判断字典信息是否重复
     * @param tdictionaryid 字典id
     * @param type 类别
     * @param code 值
     * @param value 含义
     * @return 是/否
     */
    public boolean getDictionaryByType(String type, String code, String value, int tdictionaryid);

}
