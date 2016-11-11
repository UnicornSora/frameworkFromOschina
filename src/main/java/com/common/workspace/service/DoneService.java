package com.common.workspace.service;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import com.base.service.BaseService;
import com.base.utils.Page;
import com.common.workspace.vo.DoneVo;
import com.common.workspace.vo.NodeVo;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-6-12
 * @classDescription:已办业务service接口
 */
public interface DoneService extends BaseService {
    /**
     * 查询已办业务总数
     * @param loginname 登录名
     * @return 总数
     */
    public int queryCount(String loginname);

    /**
     * 查询已办业务信息
     * @param loginname 登录名
     * @param p 分页信息
     * @return 已办业务信息
     */
    public List<DoneVo> queryList(String loginname, Page p);
    /**
     * 获取流程走过的线
     * @param processDefinitionEntity   ProcessDefinitionEntity
     * @param historicActivityInstances  List<HistoricActivityInstance>
     * @return  线信息
     */
    public List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances);
    /**
     * 查询业务审批明细
     * @param executionid executionid
     * @return 业务审批明细
     */
    public List<NodeVo> queryListNode(String executionid);

}
