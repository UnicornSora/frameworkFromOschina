package com.common.security.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.security.service.OnlineService;
import com.common.security.vo.OnlineVo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:在线用户信息类
 */

@Controller
@RequestMapping("onlineController")
public class OnlineController {

    @Autowired
    private OnlineService onlineService;

    /**
     * 查询在线用户
     * @return 在线用户datagrid信息
     */
    @RequestMapping(value = "gridform.do")
    @ResponseBody
    @RequiresPermissions("onlineController/gridform.do")
    public Object gridform() {
        List<OnlineVo> list = onlineService.queryList();
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("rows",list) ;
        return result;
    }

}
