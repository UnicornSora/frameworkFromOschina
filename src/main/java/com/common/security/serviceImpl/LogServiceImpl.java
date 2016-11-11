package com.common.security.serviceImpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import com.base.po.Tlog;
import com.base.serviceImpl.BaseServiceImpl;
import com.base.utils.Page;
import com.common.security.service.LogService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription:系统日志service类
 */
@Service("logService")
public class LogServiceImpl extends BaseServiceImpl implements LogService {
    /**
     * 查询日志信息总数
     * @return 总数
     */
    public int queryCount(String tloguser,String tlogname,String start,String end){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(t.tlogid) ");
        sql.append("   from t_log t  ");
        sql.append("   left join t_login a on  a.loginname = t.tloguser  ");
        sql.append("  where 1 = 1  ");
        if(tloguser!= null&&!tloguser.equals("")){
            sql.append("and a.username = '"+tloguser+"' ");
        }
        if(tlogname!= null&&!tlogname.equals("")){
            sql.append("and t.tlogname like '%"+tlogname+"%' ");
        }
        if(start!= null&&!start.equals("")){
            sql.append(" and t.tlogdate >= '"+start+" 00:00:00' ");
        }
        if(end!= null&&!end.equals("")){
            sql.append(" and t.tlogdate <= '"+end+" 23:59:59' ");
        }
        return Integer.parseInt(session.createSQLQuery(sql.toString()).uniqueResult()+"");
    }
    /**
     * 查询日志信息
     * @param p 分页对象
     * @return 日志信息
     */
    public List<Tlog> queryList(String tloguser,String tlogname,String start,String end,Page p){
        Session session = this.getSession();
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.tlogname,t.tloguser,t.tlogip,t.tlogdate,t.tlogtime ");
        sql.append("   from t_log t  ");
        sql.append("   left join t_login a on  a.loginname = t.tloguser  ");
        sql.append("  where 1 = 1  ");
        if(tloguser!= null&&!tloguser.equals("")){
            sql.append("and a.username = '"+tloguser+"' ");
        }
        if(tlogname!= null&&!tlogname.equals("")){
            sql.append("and t.tlogname like '%"+tlogname+"%' ");
        }
        if(start!= null&&!start.equals("")){
            sql.append(" and t.tlogdate >= '"+start+" 00:00:00' ");
        }
        if(end!= null&&!end.equals("")){
            sql.append(" and t.tlogdate <= '"+end+" 23:59:59' ");
        }
        sql.append(" order by t.tlogid desc ");
        Query q = session.createSQLQuery(sql.toString());
        q.setFirstResult(p.getStartInfo());
        q.setMaxResults(p.getPageInfoCount());
        List<Object[]> listobj = q.list();
        List<Tlog> list = new ArrayList();
        for(Object[] o : listobj){
            Tlog t = new Tlog();
            t.setTlogname(o[0].toString());
            t.setTloguser(o[1].toString());
            t.setTlogip(o[2].toString());
            t.setTlogdate((Date)o[3]);
            t.setTlogtime(o[4].toString());
            list.add(t);
        }
        return list;
    }
}