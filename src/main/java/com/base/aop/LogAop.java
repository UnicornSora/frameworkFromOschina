package com.base.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.base.annotation.Log;
import com.base.po.Tlog;
import com.base.service.BaseService;
import com.common.security.vo.LoginUserVO;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: 日志aop类。
 */
@Aspect
@Component
public class LogAop {

    @Autowired
    private BaseService baseService;

    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    private String loginName;

    @Before("@annotation(com.base.annotation.Log)")
    public void beforeExec(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取操作用户
        LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
        startTime.set(System.currentTimeMillis());
        if(vo != null&&!vo.getLoginname().equals("")){
            loginName = vo.getLoginname();
        }
    }

    @After("@annotation(com.base.annotation.Log)")
    public void afterExec(JoinPoint joinPoint){
        long endtime = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取操作方法
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        //保存日志对象
        Tlog log = new Tlog();
        log.setTlogmethod(method.getName());
        log.setTlogname(method.getAnnotation(Log.class).name());
        if(loginName!=null&&!loginName.equals("")){
            log.setTloguser(loginName);
            log.setTlogip(request.getRemoteAddr());
            log.setTlogdate(new Date(endtime));
            log.setTlogtime((endtime - startTime.get())+"ms");
            baseService.save(log);
        }else{
            LoginUserVO vo = (LoginUserVO)request.getSession().getAttribute("LOGINUSER");
            if(vo != null&&!vo.getLoginname().equals("")){
                log.setTloguser(vo.getLoginname());
                log.setTlogip(request.getRemoteAddr());
                log.setTlogdate(new Date(endtime));
                log.setTlogtime((endtime - startTime.get())+"ms");
                baseService.save(log);
            }
        }
    }
}

