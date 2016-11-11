package com.common.mandate.service;

import com.base.po.Tarea;
import com.base.service.BaseService;
import com.base.utils.Page;
import com.common.mandate.vo.TreeDate;

import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:行政区划模块service接口
 */
public interface AreaService extends BaseService {
    /**
     * 通过父区划id查询行政区划树
     * @param areaid 父节点区划id
     * @return 行政区划树信息
     */
    public TreeDate getAreaTree(String areaid);
    /**
     * 行政区划dategrid信息总数查询
     * @param parentcode 父级行政区划代码
     * @return 总数
     */
    public int queryCount(String parentcode);
    /**
     * 行政区划dategrid信息查询
     * @param parentcode 父级行政区划代码
     * @param p dategrid分页信息
     * @return 行政区划dategrid信息
     */
    public List<Tarea> queryList(String parentcode, Page p);
    /**
     * 判断行政区划是否重复
     * @param code 行政区划编码
     * @param name 行政区划名称
     * @param areaid id
     * @return 是/否
     */
    public boolean getPtareaByCodeNameId(String code, String name, String areaid);
    /**
     * 判断行政区划是否重复
     * @param code 行政区划编码
     * @param name 行政区划名称
     * @return 是/否
     */
    public boolean getPtareaByCodeOrName(String code, String name);
    /**
     * 判断行政区划下是否存在单位
     * @param code 行政区划编码
     * @return 是/否
     */
    public boolean getDepartmentByAreaCode(String code);
}
