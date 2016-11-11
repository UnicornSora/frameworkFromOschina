package com.common.workspace.controller;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.utils.Page;
import com.common.security.vo.LoginUserVO;
import com.common.workspace.service.DoneService;
import com.common.workspace.vo.DoneVo;
import com.common.workspace.vo.NodeVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-6-12
 * @classDescription:已办业务controller类
 */
@Controller
@RequestMapping("doneController")
public class DoneController {

    @Autowired
    private DoneService doneService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    /**
     * 查询已办业务信息
     * @param page 当前页
     * @param rows 每页显示多少条
     * @param request HttpServletRequest
     * @return 已办业务信息
     */
    @RequestMapping(value = "gridform.do")
    @ResponseBody
    @RequiresPermissions("doneController/gridform.do")
    public Object gridform(int page, int rows,HttpServletRequest request) {
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        //总数
        int sum = doneService.queryCount(vo.getLoginname());
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<DoneVo> list = doneService.queryList(vo.getLoginname(), p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list) ;
        return result;
    }

    /**
     * 查询已办业务流程图
     * @param processInstanceId 流程id
     * @param response HttpServletResponse
     * @return 已办业务信息
     */
    @RequestMapping(value = "traceProcess.do")// 读取带跟踪的图片
    @RequiresPermissions("doneController/traceProcess.do")
    public void traceProcess(String processInstanceId, HttpServletResponse response){
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        List<String> activitiIds = new ArrayList();
        List<String> flowIds = new ArrayList();

        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
        flowIds = doneService.getHighLightedFlows(processDefinition, activityInstances);//获取流程走过的线

        for (HistoricActivityInstance hai : activityInstances) {
            activitiIds.add(hai.getActivityId());//获取流程走过的节点
        }
        try ( //打印流程图
            InputStream imageStream = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator().generateDiagram(bpmnModel, "PNG", activitiIds, flowIds,
                      processEngineConfiguration.getActivityFontName(),
                      processEngineConfiguration.getLabelFontName(),
                      processEngineConfiguration.getClassLoader(), 1.0)) {
            byte[] buffer = new byte[4 * 1024];
            int length;
            while ((length = imageStream.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, length);
            }
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询业务审批明细
     * @param executionid executionid
     * @return 业务审批明细
     */
    @RequestMapping(value = "gridformnode.do")
    @ResponseBody
    @RequiresPermissions("doneController/gridformnode.do")
    public Object gridformnode(String executionid) {
        //查询信息
        List<NodeVo> list = doneService.queryListNode(executionid) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("rows",list) ;
        return result;
    }
}
