package com.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.base.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: football98
 * @createTime:: 16-9-28
 * @classDescription: 基础控制类，包含公共的控制方法
 */
@RequestMapping("/baseController")
@Controller
public class BaseController {

    @Autowired
    private BaseService baseService;

    /**
     * 获取ComboBox列表信息
     * @param type 字典类别
     * @param isnull 是否添加空值
     * @return ComboBox列表信息
     */
    @RequestMapping(value="getComboBox.do")
    @ResponseBody
    public Object getComboBox(String type,String isnull) {
        return baseService.getComboBox(type,isnull);
    }

}

