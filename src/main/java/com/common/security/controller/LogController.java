package com.common.security.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.controller.BaseController;
import com.base.po.Tlog;
import com.base.utils.Page;
import com.common.security.service.LogService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:字典管理模块controller类
 */
@Controller
@RequestMapping("logController")
public class LogController  extends BaseController {
    @Autowired
    private LogService logService;

    /**
     * 查询日志datagrid信息
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return datagrid信息
     */
    @RequestMapping(value = "gridform.do")
    @ResponseBody
    @RequiresPermissions("logController/gridform.do")
    public Object gridform(String tloguser,String tlogname,String start,String end,int page, int rows) {
        //总数
        int sum = logService.queryCount(tloguser,tlogname,start,end);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<Tlog> list = logService.queryList(tloguser,tlogname,start,end,p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list);
        return result;
    }

}
