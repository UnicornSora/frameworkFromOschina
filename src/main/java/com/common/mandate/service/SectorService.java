package com.common.mandate.service;

import com.base.po.Tsector;
import com.base.service.BaseService;
import com.base.utils.Page;
import com.common.mandate.vo.TreeDate;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:部门管理模块service接口
 */
public interface SectorService extends BaseService {
    /**
     * 获取单位信息tree
     * @param code 行政区划编码
     * @return 单位信息tree
     */
    public TreeDate getTree(String code);
    /**
     * 查询部门dategrid信息总数
     * @param departmenttree 所属单位
     * @param name 部门名称
     * @return 总数
     */
    public int queryCount(String departmenttree, String name);
    /**
     * 查询部门dategrid信息
     * @param departmenttree 所属单位
     * @param name 部门名称
     * @param p dategrid分页信息
     * @return 部门dategrid信息
     */
    public List<Tsector> queryList(String departmenttree, String name, Page p);
    /**
     * 判断部门是否重复
     * @param departmentid 所属单位id
     * @param name 部门名称
     * @return 是/否
     */
    public boolean getBydepartmenName(String departmentid, String name);
    /**
     * 判断部门是否重复
     * @param departmentid 所属单位id
     * @param name 部门名称
     * @param sectorid 部门id
     * @return 是/否
     */
    public boolean getBydepartmenNameAndId(String departmentid, String name, String sectorid);
    /**
     * 判断部门下是否存在人员
     * @param sectorid 部门id
     * @return 是/否
     */
    public boolean getUserById(String sectorid);
    /**
     * 判断所属单位是否存在
     * @param departmentid 单位id
     * @return 是/否
     */
    public boolean getDepartmentById(String departmentid);
}

