package com.generator.utils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import com.generator.database.IntrospectedColumn;
import com.generator.database.IntrospectedTable;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author: football98
 * @createTime: 16-3-28
 * @classDescription: freemarker工具类
 */
public class FreemarkerUtil {
    private Configuration cfg ;

    public Configuration getCfg() {
        return cfg;
    }

    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }

    public FreemarkerUtil(String path) throws IOException {
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File(path));
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        this.setCfg(cfg);
    }

    /* 获取模板文件 */
    public Template getTemplate(String templateName) throws IOException {
        return cfg.getTemplate(templateName);
    }
    /* 获取模板数据*/
    public List<Map<String, Object>> getMap(List<IntrospectedTable> list){
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

        for (IntrospectedTable table : list) {
            Map<String, Object> root = new HashMap<String, Object>();
            //类名
            root.put("class", table.getName());
            //引入数据类型
            Set<String> classList = new HashSet<String>();

            Collection<Map<String, String>> properties = new HashSet<Map<String, String>>();
            root.put("properties", properties);
            root.put("classList",classList );

            for (IntrospectedColumn column : table.getAllColumns()) {
                //字段赋值
                Map<String, String> columnMap = new HashMap<String, String>();
                columnMap.put("name", column.getJavaProperty());
                columnMap.put("type", column.getFullyQualifiedJavaType().getShortName());
                columnMap.put("isPk",column.isPk()+"");
                if(column.getFullyQualifiedJavaType().getShortName().equals("Date")){
                    classList.add("java.util.Date;");
                }else if(column.getFullyQualifiedJavaType().getShortName().equals("BigDecimal")){
                    classList.add("java.math.BigDecimal;");
                }
                columnMap.put("remarks", column.getRemarks());
                properties.add(columnMap);
            }
            listMap.add(root);
        }
        return listMap;
    }

}
