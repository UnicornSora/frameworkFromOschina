package com.common.security.service;

import com.base.po.Tparameter;
import com.base.service.BaseService;
import com.base.utils.Page;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:系统参数管理service接口
 */
public interface ParameterService extends BaseService {
    /**
     * 查询系统参数总数
     * @param type 类别
     * @return 总数
     */
    public int queryCount(String type);
    /**
     * 查询系统参数信息
     * @param type 类别
     * @param p 分页信息
     * @return 系统参数信息
     */
    public List<Tparameter> queryList(String type, Page p);
    /**
     * 判断系统参数是否重复
     * @param area 所属行政区划
     * @param type 类别
     * @return 是/否
     */
    public boolean getParameterByType(String area, String type);
    /**
     * 判断系统参数是否重复
     * @param tparameterid 系统参数id
     * @param area 所属行政区划
     * @param type 类别
     * @return 是/否
     */
    public boolean getParameterByType(String area, String type, String tparameterid);
}