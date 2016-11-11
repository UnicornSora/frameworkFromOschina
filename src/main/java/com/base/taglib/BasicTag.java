package com.base.taglib;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: 页面基础js和css标签，通过该标签可以省去繁琐、臃肿的引入js,css过程
 */
public class BasicTag extends TagSupport {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int doEndTag() throws JspException {

        StringBuffer sb = new StringBuffer();
        //eaysUI js
        sb.append("\n<link rel=\"stylesheet\" type=\"text/css\" href=\"js/jquery-easyui-1.4.1/themes/default/easyui.css\">  ");
        sb.append("\n<link rel=\"stylesheet\" type=\"text/css\" href=\"js/jquery-easyui-1.4.1/themes/icon.css\">  ");
        sb.append("\n<link rel=\"stylesheet\" type=\"text/css\" href=\"css/basiccss.css\">  ");
        sb.append("\n<script type=\"text/javascript\" src=\"js/jquery-easyui-1.4.1/jquery.min.js\"></script>  ");
        sb.append("\n<script type=\"text/javascript\" src=\"js/jquery-easyui-1.4.1/jquery.easyui.min.js\"></script>  ");
        sb.append("\n<script type=\"text/javascript\" src=\"js/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js\"></script>   ");
        sb.append("\n<script type=\"text/javascript\" src=\"js/navfix.js\"></script>   ");
        sb.append("\n<script type=\"text/javascript\" src=\"js/common.js\"></script>   ");

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