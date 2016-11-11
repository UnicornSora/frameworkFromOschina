package com.common.workflow.service;

import com.base.service.BaseService;
import com.base.utils.Page;
import com.common.workflow.vo.ActProcessDefinition;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:流程service接口
 */
public interface ProcessService extends BaseService {
    /**
     * 查询流程总数
     * @param key KEY
     * @param name 名称
     * @param suspended 状态
     * @return 总数
     */
    public int queryCount(String key, String name, String suspended);
    /**
     * 查询流程信息
     * @param key KEY
     * @param name 名称
     * @param suspended 状态
     * @param p 分页信息
     * @return 流程信息list
     */
    public List<ActProcessDefinition> queryList(String key, String name, String suspended, Page p);
    /**
     * 更新流程
     * @param id ID
     * @param name 名称
     * @param suspended 流程状态
     * @return
     */
    public void updateProcess(String id, String name, String suspended);
    /**
     * 删除流程
     * @param deploymentId 流程部署ID
     * @return
     */
    public void deleteProcess(String deploymentId);
    /**
     * 测试流程
     * @return 操作信息
     */
    public void testProcess(String key, String loginname);
}
