package com.common.workflow.controller;

import org.activiti.engine.RepositoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.utils.Page;
import com.common.security.vo.LoginUserVO;
import com.common.workflow.service.ProcessService;
import com.common.workflow.vo.ActProcessDefinition;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:流程controller类
 */
@Controller
@RequestMapping("processController")
public class ProcessController {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessService processService;

    /**
     * 查询流程信息
     * @param key KEY
     * @param name 名称
     * @param suspended 流程状态
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return 流程信息
     */
    @RequestMapping(value = "gridform.do")
    @ResponseBody
    public Object gridform(String key,String name,String suspended, int page, int rows) {
        //总数
        int sum = processService.queryCount(key, name, suspended);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<ActProcessDefinition> list = processService.queryList(key, name, suspended, p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list) ;
        return result;
    }

    /**
     * 更新流程
     * @param id_dlg KEY
     * @param name_dlg 名称
     * @param suspended_dlg 流程状态
     * @return 操作信息
     */
    @RequestMapping(value = "updateProcess")
    @ResponseBody
    @RequiresPermissions("processController/updateProcess.do")
    @Log(name="更新流程")
    public Object updateProcess(String id_dlg, String name_dlg , String suspended_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            processService.updateProcess(id_dlg,name_dlg,suspended_dlg);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "修改失败！";
            success = false;
        }
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }

    /**
     * 删除流程
     * @param deploymentId 部署ID
     * @return 操作信息
     */
    @RequestMapping(value = "deleteProcess.do")
    @ResponseBody
    @RequiresPermissions("processController/deleteProcess.do")
    @Log(name="删除流程")
    public Object deleteProcess(String deploymentId) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            processService.deleteProcess(deploymentId);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "删除失败！";
            success = false;
        }
        //返回结果
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }

    @RequestMapping(value = "resourceRead")
    public void resourceRead(String processDefinitionId,String resourceName, HttpServletResponse response) throws Exception {
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinitionId, resourceName);
        byte[] buffer = new byte[4 * 1024];
        int length;
        OutputStream os =  response.getOutputStream();
        while ((length = resourceAsStream.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        os.close();
    }

    /**
     * 测试流程
     * @return 操作信息
     */
    @RequestMapping(value = "testProcess.do")
    @ResponseBody
    @Log(name="测试流程")
    public Object testProcess(HttpServletRequest request) {
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "测试流程已启动！";
        boolean success = true;
        try {
            processService.testProcess("testprocess",vo.getLoginname());
        } catch (Exception e) {
            e.printStackTrace();
            msg = "测试流程启动失败！";
            success = false;
        }
        //返回结果
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }
}
