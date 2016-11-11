package com.common.security.serviceImpl;
import com.base.dao.BaseDao;
import com.base.po.Tpermission;
import com.base.po.Trole;
import com.base.service.BaseService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 16-2-15
 * Time: 上午11:07
 * To change this template use File | Settings | File Templates.
 */

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:applicationContext-springmvc.xml"})
public class ParameterServiceImplTest {
    @Resource(name="baseService")
    private BaseService baseService;

    public BaseService getBaseService() {
        return baseService;
    }

    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }

    @Test
    public void test(){
        Tpermission t = baseService.get(Tpermission.class,"0");

    }
}
