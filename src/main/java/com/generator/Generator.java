package com.generator;

import com.generator.database.*;
import com.generator.utils.DBMetadataUtils;
import com.generator.utils.FreemarkerUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.*;

/**
 * @author: football98
 * @createTime: 16-3-28
 * @classDescription: 代码生成器类，通过参数，用于生产代码。
 */
public class Generator {

    //数据源配置
    public static String url = "jdbc:mysql://127.0.0.1:3306/framework";
    public static String username = "root";
    public static String password = "root";
    //生成文件目录配置
    public static String templatepath = "D:\\workspaces\\IdeaProjects\\framework\\src\\main\\java\\zteict\\qinhuangdao\\framework\\generator\\template";
    public static String filepath = "D:\\generator";
    //如果tablename为""，将遍历数据库中的表
    public static String tablename = "t_role";

    public static void generatorTemplate(String templateName, String fileEnd,String fileDirName, List<IntrospectedTable> list) throws IOException, TemplateException {
        FreemarkerUtil fm = new FreemarkerUtil(templatepath);
        Template template = fm.getTemplate(templateName);
        List<Map<String, Object>> introspectedTablelist =  fm.getMap(list);

        // 生成输出到文件
        File fileDir = new File(filepath+"/"+fileDirName);
        // 创建文件夹，不存在则创建
        FileUtils.forceMkdir(fileDir);
        for(Map<String, Object> map : introspectedTablelist){
            String name = map.get("class")+"";
            name = name.substring(0, 1).toUpperCase() +name.substring(1);
            String filename = "/"+name+fileEnd;
            // 指定生成输出的文件
            File output = new File(fileDir + filename);
            Writer writer = new FileWriter(output);
            template.process(map, writer);
            writer.close();
        }

    }

    public static void main(String[] args) throws IOException, SQLException, TemplateException {
        //创建数据源，还可通过SimpleDataSource(Dialect dialect, DataSource dataSource)构造方法，直接使用其他的DataSource。
        SimpleDataSource dataSource = new SimpleDataSource( Dialect.MYSQL,url,username,password);
        //然后就是用创建好的dataSource去创建DBMetadataUtils:
        DBMetadataUtils dbUtils = new DBMetadataUtils(dataSource);
        //创建一个DatabaseConfig,调用introspectTables(config)方法获取数据库表的元数据：
        //这里需要注意DatabaseConfig，他有下面三个构造方法：
        //DatabaseConfig()
        //DatabaseConfig(String catalog, String schemaPattern)
        //DatabaseConfig(String catalog, String schemaPattern, String tableNamePattern)
        //一般情况下我们需要设置catalog和schemaPatter，还可以设置tableNamePattern来限定要获取的表。
        //其中schemaPatter和tableNamePattern都支持sql的%和_匹配。
        DatabaseConfig config ;
        if(tablename.equals("")){
            config = new DatabaseConfig();
        }else{
            config = new DatabaseConfig(null,null,tablename);
        }
        List<IntrospectedTable> list = dbUtils.introspectTables(config);

        generatorTemplate("PoTemplate.ftl", ".java","po", list);
        generatorTemplate("ServiceTemplate.ftl", "Service.java","service", list);
        generatorTemplate("ServiceImplTemplate.ftl", "ServiceImpl.java","serviceImpl", list);
        generatorTemplate("ControllerTemplate.ftl", "Controller.java","controller", list);
        generatorTemplate("JspTemplate.ftl", ".jsp","jsp", list);


    }
}
