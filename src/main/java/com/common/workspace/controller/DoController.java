package com.common.workspace.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.common.security.vo.LoginUserVO;
import com.common.workspace.service.DoService;
import com.common.workspace.vo.DoVo;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-6-12
 * @classDescription:待办业务controller类
 */
@Controller
@RequestMapping("doController")
public class DoController {

    @Autowired
    private DoService doService;

    /**
     * 查询待办业务信息
     * @param request HttpServletRequest
     * @return 待办业务信息
     */
    @RequestMapping(value = "gridform.do")
    @ResponseBody
    @RequiresPermissions("doController/gridform.do")
    public Object gridform(HttpServletRequest request) {
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        List<DoVo> list = doService.queryList(vo.getLoginname());
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("rows",list) ;
        return result;
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
    @RequestMapping(value = "complete.do")
    @ResponseBody
    @RequiresPermissions("doController/complete.do")
    @Log(name="流程审批")
    public Object complete(String id_dlg,String task_def_key_dlg , String agree_dlg,String opinion_dlg,String key_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "审批成功！";
        boolean success = true;
        try {
            doService.complete(id_dlg,task_def_key_dlg,agree_dlg,opinion_dlg,key_dlg);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "审批失败！";
            success = false;
        }
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }
}
