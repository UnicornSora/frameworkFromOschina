package com.common.security.service;

import com.base.po.Tlog;
import com.base.service.BaseService;
import com.base.utils.Page;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:系统日志service接口
 */
public interface LogService extends BaseService {
    /**
     * 查询日志信息总数
     * @return 总数
     */
    public int queryCount(String tloguser, String tlogname, String start, String end);
    /**
     * 查询日志信息
     * @param p 分页对象
     * @return 日志信息
     */
    public List<Tlog> queryList(String tloguser, String tlogname, String start, String end, Page p);
}

