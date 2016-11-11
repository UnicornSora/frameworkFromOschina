package com.common.workspace.service;

import com.base.service.BaseService;
import com.common.workspace.vo.DoVo;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-6-12
 * @classDescription:待办业务service接口
 */
public interface DoService extends BaseService {
    /**
     * 查询待办业务信息
     * @param loginname 登录名
     * @return 待办业务信息
     */
    public List<DoVo> queryList(String loginname);
    /**
     * 待办业务审批
     * @param id_dlg 业务id
     * @param task_def_key_dlg 流程名称
     * @param agree_dlg   审批结果
     * @param opinion_dlg 审批意见
     * @param key_dlg KEY
     * @return 操作信息
     */
    public void complete(String id_dlg, String task_def_key_dlg, String agree_dlg, String opinion_dlg, String key_dlg);
}
