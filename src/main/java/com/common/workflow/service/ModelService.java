package com.common.workflow.service;

import com.base.service.BaseService;
import com.base.utils.Page;
import com.common.workflow.vo.ModelVo;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:模型service接口
 */
public interface ModelService extends BaseService {
    /**
     * 查询流程模型总数
     * @param key KEY
     * @param name 名称
     * @param metainfo 元数据
     * @return 总数
     */
    public int queryCount(String key, String name, String metainfo);
    /**
     * 查询流程模型
     * @param key KEY
     * @param name 名称
     * @param metainfo 元数据
     * @param p 分页信息
     * @return 流程模型List
     */
    public List<ModelVo> queryList(String key, String name, String metainfo, Page p);
    /**
     * 创建模型
     * @param key KEY
     * @param name 名称
     * @param metaInfo 元数据
     * @return 模型ID
     */
    public String createModel(String name, String key, String metaInfo) throws UnsupportedEncodingException;
    /**
     * 删除模型
     * @param id ID
     * @return
     */
    public void deleteModel(String id);
    /**
     * 更新模型
     * @param id ID
     * @param key KEY
     * @param name 名称
     * @param metaInfo 元数据
     * @return
     */
    public void updateModel(String name, String key, String metaInfo, String id);
    /**
     * 部署模型
     * @param id ID
     * @return
     */
    public void deployModel(String id) throws IOException;
}
