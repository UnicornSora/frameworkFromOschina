package com.common.workspace.serviceImpl;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.workspace.service.DoneService;
import com.common.workspace.vo.DoneVo;
import com.common.workspace.vo.NodeVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-6-12
 * @classDescription:已办业务service类
 */
@Service("doneService")
public class DoneServiceImpl extends BaseServiceImpl implements DoneService {
    /**
     * 查询已办业务总数
     * @param loginname 登录名
     * @return 总数
     */
    public int queryCount(String loginname){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(executionid) ");
        sql.append("   from v_done  ");
        sql.append("  where assignee = :loginname ");
        return Integer.parseInt(session.createSQLQuery(sql.toString()).setString("loginname",loginname).uniqueResult()+"");
    }
    /**
     * 查询已办业务信息
     * @param loginname 登录名
     * @param p 分页信息
     * @return 已办业务信息
     */
    public List<DoneVo> queryList(String loginname,Page p){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select executionid ,procinstid,actname ,endtime");
        sql.append("   from v_done  ");
        sql.append("  where assignee = :loginname ");
        Query q = session.createSQLQuery(sql.toString());
        q.setString("loginname",loginname);
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        List<Object[]> list = q.list();
        List<DoneVo> listVo = new ArrayList<DoneVo>();
        for(Object[] o : list){
            DoneVo v = new DoneVo();
            v.setExecutionid(o[0].toString());
            v.setProcinstid(o[1].toString());
            v.setActname(o[2].toString());
            v.setEndtime((Date) o[3]);
            listVo.add(v);
        }
        return listVo;
    }
    /**
     * 获取流程走过的线
     * @param processDefinitionEntity   ProcessDefinitionEntity
     * @param historicActivityInstances  List<HistoricActivityInstance>
     * @return  线信息
     */
    public List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
        List<String> highFlows = new ArrayList();//用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
            List<ActivityImpl> sameStartTimeNodes = new ArrayList();// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());// 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {// 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2= processDefinitionEntity.findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {// 有不相同跳出循环
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {// 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }
    /**
     * 查询业务审批明细
     * @param executionid executionid
     * @return 业务审批明细
     */
    public List<NodeVo> queryListNode(String executionid){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.actname,t.assignee,t.agree,t.opinion ,date_format(t.starttime,'%Y-%c-%d %h:%i:%s'),date_format(t.endtime,'%Y-%c-%d %h:%i:%s') ");
        sql.append("   from v_node t   ");
        sql.append("  where t.executionid = :executionid ");
        sql.append("  order by t.starttime ");

        Query q = session.createSQLQuery(sql.toString()).setString("executionid",executionid);
        List<Object[]> list = q.list();
        List<NodeVo> listVo = new ArrayList<NodeVo>();
        for(Object[] o : list){
            NodeVo v = new NodeVo();
            v.setActname(o[0].toString());
            if(o[1] != null && !o[1].toString().equals("")){
                v.setAssignee(o[1].toString());
            }else{
                v.setAssignee("");
            }
            if(o[2] != null && !o[2].toString().equals("")){
                v.setAgree(o[2].toString());
            }else{
                v.setAgree("");
            }
            if(o[3] != null && !o[3].toString().equals("")){
                v.setOpinion(o[3].toString());
            }else{
                v.setOpinion("");
            }
            v.setStarttime(o[4].toString());
            if(o[5] != null && !o[5].toString().equals("")){
                v.setEndtime(o[5].toString());
            }else{
                v.setEndtime("");
            }
            listVo.add(v);
        }
        return listVo;
    }
}
