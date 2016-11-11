package com.base.taglib;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.base.service.BaseService;
import com.base.vo.ComboBox;
import net.sf.json.JSONArray;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: 查询字典，实现dategrid格式化
 */
public class GridformatterTag  extends TagSupport {
    private String name;
    private String code;
    private String isnull;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsnull() {
        return isnull;
    }

    public void setIsnull(String isnull) {
        this.isnull = isnull;
    }

    @Override
    public int doEndTag() throws JspException {
        //查询字典
        ServletContext servletContext = pageContext.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        BaseService bs = (BaseService) wac.getBean("baseService");
        List<ComboBox> list = bs.getComboBox(code,isnull);
        JSONArray json = new JSONArray();
        json.addAll(list);
        //标签内容
        StringBuffer sb = new StringBuffer();
        sb.append("\n <script>");
        sb.append("\n function "+name+"formatter(value, rowData, rowIndex) { ");
        sb.append("\n     var "+name+" = "+json+" ; ");
        sb.append("\n     for (var i = 0; i < "+name+".length; i++) { ");
        sb.append("\n         if ("+name+"[i].id == value) { ");
        sb.append("\n             return "+name+"[i].text; ");
        sb.append("\n         } ");
        sb.append("\n     } ");
        sb.append("\n } ");
        sb.append("\n </script>");

        try {
            pageContext.getOut().println(sb.toString());
        } catch (IOException ignored){

        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doStartTag() throws JspException {
        // TODO Auto-generated method stub
        return super.doStartTag();
    }

    @Override
    public void release() {
        // TODO Auto-generated method stub
        super.release();
    }

}