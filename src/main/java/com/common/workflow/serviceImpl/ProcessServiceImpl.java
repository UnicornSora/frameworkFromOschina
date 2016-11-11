package com.common.workflow.serviceImpl;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.workflow.service.ProcessService;
import com.common.workflow.vo.ActProcessDefinition;
import java.util.*;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:流程service接口
 */
@Service("processService")
public class ProcessServiceImpl extends BaseServiceImpl implements ProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService ;

    @Autowired
    private TaskService taskService ;

    /**
     * 查询流程总数
     * @param key KEY
     * @param name 名称
     * @param suspended 状态
     * @return 总数
     */
    public int queryCount(String key,String name,String suspended){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(t.ID_) ");
        sql.append("   from act_re_procdef t ");
        sql.append("  where 1 = 1 ");
        if(key!= null&&!key.equals("")){
            sql.append("and t.KEY_ = '"+key+"' ");
        }
        if(name!= null&&!name.equals("")){
            sql.append("and t.NAME_ like '%"+name+"%' ");
        }
        if(suspended!= null&&!suspended.equals("")){
            sql.append("and t.SUSPENSION_STATE_ = "+suspended+" ");
        }
        return Integer.parseInt(session.createSQLQuery(sql.toString()).uniqueResult()+"");
    }
    /**
     * 查询流程信息
     * @param key KEY
     * @param name 名称
     * @param suspended 状态
     * @param p 分页信息
     * @return 流程信息list
     */
    public List<ActProcessDefinition> queryList(String key,String name,String suspended,Page p){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.ID_,t.DEPLOYMENT_ID_,t.DGRM_RESOURCE_NAME_,t.RESOURCE_NAME_,t.KEY_,t.NAME_,t.VERSION_,a.DEPLOY_TIME_,t.SUSPENSION_STATE_ ");
        sql.append("   from act_re_procdef t  ");
        sql.append("   left join act_re_deployment a on a.ID_ = t.DEPLOYMENT_ID_ ");
        sql.append("  where 1 = 1 ");
        if(key!= null&&!key.equals("")){
            sql.append("and t.KEY_ = '"+key+"' ");
        }
        if(name!= null&&!name.equals("")){
            sql.append("and t.NAME_ like '%"+name+"%' ");
        }
        if(suspended!= null&&!suspended.equals("")){
            sql.append("and t.SUSPENSION_STATE_ = "+suspended+" ");
        }
        Query q = session.createSQLQuery(sql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        List<Object[]> list = q.list();
        List<ActProcessDefinition> listvo = new ArrayList<ActProcessDefinition>();
        for (int i = 0; i < list.size(); i++) {
            Object[] o = list.get(i);
            ActProcessDefinition v = new ActProcessDefinition();
            v.setId(o[0].toString());
            v.setDeploymentId(o[1].toString());
            v.setDiagramResourceName(o[2].toString());
            v.setResourceName(o[3].toString());
            v.setKey(o[4].toString());
            v.setName(o[5].toString());
            v.setVersion((int)o[6]);
            v.setDeploymentTime((Date)o[7]);
            v.setSuspended(o[8].toString());
            listvo.add(v);
        }
        return listvo;
    }
    /**
     * 更新流程
     * @param id ID
     * @param name 名称
     * @param suspended 流程状态
     * @return
     */
    public void updateProcess(String id, String name , String suspended){
        //更新流程名称
        Session session = this.getSession();
        Query q = session.createSQLQuery(" update act_re_procdef t set t.name_ = :name_ where t.id_ =:id_ ");
        q.setString("name_",name);
        q.setString("id_",id);
        q.executeUpdate();
        ProcessDefinition p =  repositoryService.getProcessDefinition(id);
        if(p.isSuspended()){
            if(suspended.equals("false")){
                repositoryService.activateProcessDefinitionById(id, true, null);
            }
        }else{
            if(suspended.equals("true")){
                repositoryService.suspendProcessDefinitionById(id, true, null);
            }
        }
    }
    /**
     * 删除流程
     * @param deploymentId 流程部署ID
     * @return
     */
    public void deleteProcess(String deploymentId){
        repositoryService.deleteDeployment(deploymentId);
    }

    /**
     * 测试流程
     * @return 操作信息
     */
    public void testProcess(String key ,String loginname ){
        Session session = this.getSession();
        //启动流程
        Map<String, Object> variables = new HashMap<String, Object>() ;
        variables.put("applyUserId",loginname);
        ProcessInstance p = runtimeService.startProcessInstanceByKey(key,variables);
        //用户签收
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(p.getProcessInstanceId()).list();
        taskService.claim(tasks.get(0).getId(),loginname);
        //更新startEvent，申请人
        StringBuffer sql = new StringBuffer();
        sql.append(" update act_hi_actinst t ");
        sql.append("   set t.ASSIGNEE_ = ( select a.TEXT_ from  act_hi_varinst a where a.EXECUTION_ID_ = t.EXECUTION_ID_ and a.NAME_ = 'applyUserId')  ");
        sql.append(" where t.PROC_INST_ID_ = :proc_inst_id and t.ACT_TYPE_ = 'startEvent' and t.ASSIGNEE_ is null ");
        session.createSQLQuery(sql.toString()).setString("proc_inst_id",p.getProcessInstanceId()).executeUpdate();
    }
}
