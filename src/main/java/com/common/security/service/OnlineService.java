package com.common.security.service;

import com.base.service.BaseService;
import com.common.security.vo.OnlineVo;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:在线用户service接口
 */
public interface OnlineService extends BaseService {
    /**
     * 查询在线用户信息
     * @return 在线用户信息
     */
    public List<OnlineVo> queryList();

}

