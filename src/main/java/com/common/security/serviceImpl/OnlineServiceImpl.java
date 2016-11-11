package com.common.security.serviceImpl;

import org.springframework.stereotype.Service;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.SessionListener;
import com.common.security.service.OnlineService;
import com.common.security.vo.OnlineVo;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:在线用户service类
 */
@Service("onlineService")
public class OnlineServiceImpl extends BaseServiceImpl implements OnlineService {
    /**
     * 查询在线用户信息
     * @return 在线用户信息
     */
    public List<OnlineVo> queryList(){
        HashMap<String,HttpSession> sessionMap = SessionListener.sessionMap;
        List<OnlineVo> list = new ArrayList<OnlineVo>();
        for(String key : SessionListener.userMap.keySet()){
            OnlineVo v = new OnlineVo();
            v.setLoginname(key);
            list.add(v);
        }
        return  list;
    }
}