package com.common.workflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.base.annotation.Log;
import com.base.utils.Page;
import com.common.workflow.service.ModelService;
import com.common.workflow.vo.ModelVo;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:模型controller类
 */
@Controller
@RequestMapping("modelController")
public class ModelController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    @Autowired
    private ModelService modelService;

    /**
     * 查询模型信息
     * @param key KEY
     * @param name 名称
     * @param metainfo 元数据
     * @param page 当前页
     * @param rows 每页显示多少条
     * @return 模型信息
     */
    @RequestMapping(value = "gridform.do")
    @ResponseBody
    public Object gridform(String key,String name,String metainfo, int page, int rows) {
        //总数
        int sum = modelService.queryCount(key, name, metainfo);
        //分页信息
        Page p = new Page();
        p.setIntPage(page);
        p.setPageInfoCount(rows);
        //查询信息
        List<ModelVo> list = modelService.queryList(key, name, metainfo, p) ;
        //返回结果
        Map<String, Object> result = new HashMap<String, Object>() ;
        result.put("total",sum);
        result.put("rows",list) ;
        return result;
    }

    /**
     * 创建模型
     * @param key_dlg KEY
     * @param name_dlg 名称
     * @param metaInfo_dlg 元数据
     * @return 操作信息
     */
    @RequestMapping(value = "createModel.do")
    @ResponseBody
    @RequiresPermissions("modelController/createModel.do")
    @Log(name="创建流程模型")
    public Object createModel(String name_dlg,String key_dlg,String metaInfo_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "保存成功！";
        boolean success = true;
        try {
            result.put("modelId",modelService.createModel(name_dlg, key_dlg, metaInfo_dlg));
        } catch (Exception e) {
            e.printStackTrace();
            msg = "保存失败！";
            success = false;
        }
        //返回结果
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }

    /**
     * 删除模型
     * @param id 模型id
     * @return 操作信息
     */
    @RequestMapping(value = "deleteModel.do")
    @ResponseBody
    @RequiresPermissions("modelController/deleteModel.do")
    @Log(name="删除流程模型")
    public Object deleteModel(String id) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "删除成功！";
        boolean success = true;
        try {
            modelService.deleteModel(id);
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

    /**
     * 更新模型
     * @param name_dlg 名称
     * @param key_dlg KEY
     * @param metaInfo_dlg 元数据
     * @param id_dlg 模型id
     * @return 操作信息
     */
    @RequestMapping(value = "updateModel.do")
    @ResponseBody
    @RequiresPermissions("modelController/updateModel.do")
    @Log(name="更新流程模型")
    public Object updateModelName(String name_dlg,String key_dlg,String metaInfo_dlg, String id_dlg) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "修改成功！";
        boolean success = true;
        try {
            modelService.updateModel(name_dlg,key_dlg,metaInfo_dlg, id_dlg);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "修改失败！";
            success = false;
        }
        result.put("success",success);
        result.put("msg",msg);
        result.put("modelId",id_dlg);
        return result;
    }

    /**
     * 通过模型ID生成流程图片
     */
    @RequestMapping(value = "showPic.do")
    public void showPic(String id, HttpServletResponse response) throws Exception {
        Model modelData = repositoryService.getModel(id);
        ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        try ( //打印流程图
              InputStream imageStream = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator().generateDiagram(bpmnModel, "PNG",
                      processEngineConfiguration.getActivityFontName(),
                      processEngineConfiguration.getLabelFontName(),
                      processEngineConfiguration.getClassLoader(),
                      1.0)) {
            byte[] buffer = new byte[4 * 1024];
            int length;
            while ((length = imageStream.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.getOutputStream().close();
    }
    /**
     * 通过模型ID生成xml
     */
    @RequestMapping(value = "showXML.do")
    public void showXMLByModelId(String id, HttpServletResponse response) throws Exception {
        Model modelData = repositoryService.getModel(id);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bpmnBytes);
        byte[] buffer = new byte[4 * 1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            response.getOutputStream().write(buffer, 0, length);
        }
        response.getOutputStream().close();
    }

    /**
     * 部署模型
     * @param id 模型id
     * @return 操作信息
     */
    @RequestMapping(value = "deployModel.do")
    @ResponseBody
    @RequiresPermissions("modelController/deployModel.do")
    @Log(name="部署流程模型")
    public Object deployModel(String id) {
        Map<String, Object> result = new HashMap<String, Object>() ;
        String msg = "部署成功！";
        boolean success = true;
        try {
            modelService.deployModel(id);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "部署失败!";
            success = false;
        }
        result.put("success",success);
        result.put("msg",msg);
        return result;
    }
}
