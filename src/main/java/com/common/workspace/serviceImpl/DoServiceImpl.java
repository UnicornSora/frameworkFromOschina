package com.common.workspace.serviceImpl;

import org.activiti.engine.TaskService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.base.serviceImpl.BaseServiceImpl;
import com.common.workspace.service.DoService;
import com.common.workspace.vo.DoVo;
import java.util.*;

/**
 * @author: football98
 * @createTime: 16-6-12
 * @classDescription:待办业务service类
 */
@Service("doService")
public class DoServiceImpl extends BaseServiceImpl implements DoService {

    @Autowired
    private TaskService taskService;
    /**
     * 查询待办业务信息
     * @param loginname 登录名
     * @return 待办业务信息
     */
    public List<DoVo> queryList(String loginname){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.id_,t.execution_id_,t.proc_inst_id_,t.name_,t.create_time_,t.user_id_,t.task_def_key,proc_def_id_ ");
        sql.append("   from v_do t  ");
        sql.append("  where t.user_id_ = :loginname ");
        sql.append("  order by t.create_time_ ");
        Query q = session.createSQLQuery(sql.toString());
        q.setString("loginname",loginname);
        List<Object[]> list = q.list();
        List<DoVo> listVo = new ArrayList<DoVo>();
        for(Object[] o : list){
            DoVo v = new DoVo();
            v.setId(o[0].toString());
            v.setExecution_id(o[1].toString());
            v.setProc_inst_id(o[2].toString());
            v.setName(o[3].toString());
            v.setCreate_time((Date) o[4]);
            v.setUser_id(o[5].toString());
            v.setTask_def_key(o[6].toString());
            String[] keys = o[7].toString().split(":");
            v.setKey(keys[0]);
            listVo.add(v);
        }
        return listVo;
    }
    /**
     * 待办业务审批
     * @param id_dlg 业务id
     * @param task_def_key_dlg 流程名称
     * @param agree_dlg   审批结果
     * @param opinion_dlg 审批意见
     * @param key_dlg KEY
     * @return 操作信息
     */
    public void complete(String id_dlg,String task_def_key_dlg,String agree_dlg,String opinion_dlg,String key_dlg){
        if(key_dlg.equals("testprocess")){
            completeGet(id_dlg,task_def_key_dlg ,agree_dlg,opinion_dlg);
        }
    }
    /**
     * 测试审批
     * @param id_dlg 业务id
     * @param task_def_key_dlg 流程名称
     * @param agree_dlg   审批结果
     * @param opinion_dlg 审批意见
     * @return 操作信息
     */
    public void completeGet(String id_dlg,String task_def_key_dlg , String agree_dlg,String opinion_dlg){
        Map<String, Object> variables = new HashMap<String, Object>();
        //判断当前审批节点
        /*if(agree_dlg.equals("yes")){
            if(task_def_key_dlg.indexOf("Last") >= 0){
                //完成业务
            }
        }else{
            //结束业务
        }*/
        variables.put(task_def_key_dlg+"Opinion",opinion_dlg );
        variables.put(task_def_key_dlg+"Result",agree_dlg);
        taskService.complete(id_dlg, variables);
    }
}
