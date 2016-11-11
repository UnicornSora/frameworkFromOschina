package com.generator.template;

import com.base.po.${class?cap_first};
import com.base.service.BaseService;
import com.base.utils.Page;
import java.util.List;

public interface ${class?cap_first}Service extends BaseService {
    public int queryCount();
    public List<${class?cap_first}> queryList(Page p);
}

