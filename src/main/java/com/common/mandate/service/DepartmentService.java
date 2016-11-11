package com.common.mandate.service;

import com.base.po.Tdepartment;
import com.base.service.BaseService;
import com.base.utils.Page;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:单位管理模块service接口
 */
public interface DepartmentService extends BaseService {
    /**
     * 查询单位dategrid信息总数
     * @param areacode 所属行政区划编码
     * @param name 单位名称
     * @return 总数
     */
    public int queryCount(String areacode, String name);
    /**
     * 单位dategrid信息查询
     * @param areacode 所属行政区划编码
     * @param name 单位名称
     * @param p dategrid分页信息
     * @return 单位dategrid信息
     */
    public List<Tdepartment> queryList(String areacode, String name, Page p);
    /**
     * 判断单位是否重复
     * @param areacode 所属行政区划编码
     * @param name 单位名称
     * @return 是/否
     */
    public boolean getByAreaCodeAndName(String areacode, String name);
    /**
     * 判断单位是否重复
     * @param areacode 所属行政区划编码
     * @param name 单位名称
     * @param departmentid 单位id
     * @return 是/否
     */
    public boolean getByAreaCodeAndNameAndId(String areacode, String name, String departmentid);
    /**
     * 判断单位下是否存在部门
     * @param departmentid 单位id
     * @return 是/否
     */
    public boolean getSectorById(String departmentid);
}
