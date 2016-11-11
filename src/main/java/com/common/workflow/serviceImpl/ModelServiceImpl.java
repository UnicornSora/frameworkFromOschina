package com.common.workflow.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.workflow.service.ModelService;
import com.common.workflow.vo.ModelVo;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:流程service接口
 */
@Service("modelService")
public class ModelServiceImpl extends BaseServiceImpl implements ModelService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;
    /**
     * 查询流程模型总数
     * @param key KEY
     * @param name 名称
     * @param metainfo 元数据
     * @return 总数
     */
    public int queryCount(String key,String name,String metainfo){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(t.ID_) ");
        sql.append("   from act_re_model t  ");
        sql.append("  where 1 = 1 ");
        if(key!= null&&!key.equals("")){
            sql.append("and t.KEY_ = '"+key+"' ");
        }
        if(name!= null&&!name.equals("")){
            sql.append("and t.NAME_ = '"+name+"' ");
        }
        if(metainfo!= null&&!metainfo.equals("")){
            sql.append("and t.META_INFO_ like '%"+metainfo+"%' ");
        }
        return Integer.parseInt(session.createSQLQuery(sql.toString()).uniqueResult()+"");
    }
    /**
     * 查询流程模型
     * @param key KEY
     * @param name 名称
     * @param metainfo 元数据
     * @param p 分页信息
     * @return 流程模型List
     */
    public List<ModelVo> queryList(String key,String name,String metainfo,Page p){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.ID_,t.KEY_,t.NAME_,t.CREATE_TIME_,t.LAST_UPDATE_TIME_,t.META_INFO_ ");
        sql.append("   from act_re_model t  ");
        sql.append("  where 1 = 1 ");
        if(key!= null&&!key.equals("")){
            sql.append("and t.KEY_ = '"+key+"' ");
        }
        if(name!= null&&!name.equals("")){
            sql.append("and t.NAME_ = '"+name+"' ");
        }
        if(metainfo!= null&&!metainfo.equals("")){
            sql.append("and t.META_INFO_ like '%"+metainfo+"%' ");
        }
        Query q = session.createSQLQuery(sql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        List<Object[]> list = q.list();
        List<ModelVo> listvo = new ArrayList<ModelVo>();
        for (int i = 0; i < list.size(); i++) {
            Object[] o = list.get(i);
            ModelVo v = new ModelVo();
            v.setId(o[0].toString());
            v.setKey(o[1].toString());
            v.setName(o[2].toString());
            v.setCreatetime((Date)o[3]);
            v.setLastupdatetime((Date)o[4]);
            v.setMetainfo(o[5].toString());
            listvo.add(v);
        }
        return listvo;
    }
    /**
     * 创建模型
     * @param key KEY
     * @param name 名称
     * @param metaInfo 元数据
     * @return 模型ID
     */
    public String createModel(String name, String key, String metaInfo) throws UnsupportedEncodingException {
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.putPOJO("stencilset", stencilSetNode);
        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put("name", name);
        modelObjectNode.put("description", metaInfo);
        Model modelData = repositoryService.newModel();
        modelData.setName(name);
        modelData.setKey(key);
        modelData.setMetaInfo(modelObjectNode.toString());
        //保存模型
        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
        return modelData.getId();
    }
    /**
     * 删除模型
     * @param id ID
     * @return
     */
    public void deleteModel(String id) {//删除模型
        repositoryService.deleteModel(id);
    }
    /**
     * 更新模型
     * @param id ID
     * @param key KEY
     * @param name 名称
     * @param metaInfo 元数据
     * @return
     */
    public void updateModel(String name, String key, String metaInfo, String id) {
        Model model = repositoryService.getModel(id);
        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put("name", name);
        modelObjectNode.put("description", metaInfo);
        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelObjectNode.toString());
        repositoryService.saveModel(model);
    }

    /**
     * 部署模型
     * @param id ID
     * @return
     */
    public void deployModel(String id) throws IOException {//通过模型Id部署流程
        Model modelData = repositoryService.getModel(id);
        ObjectNode modelNode = (ObjectNode) objectMapper.readTree(repositoryService.getModelEditorSource(modelData.getId()));
        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        String processName = modelData.getName() + ".bpmn20.xml";
        repositoryService.createDeployment().addBpmnModel(processName, model).deploy();
    }

}
