从开源中国码云上找到的一套开发框架，值得学习和分享。
原作者：football98

 **framework快速开发平台**
=================================== 
 **前言** 
-----------------------------------  
从事JAVA开发有4年多了，随着使用的技术越来越多，渐渐的形成了个人的代码风格，以及对WEB系统的独特认识。为了将所学融会贯通，

将所思所想整理、总结、归纳，所以独立开发一套WEB系统快速开发平台。

 **介绍** 
-----------------------------------  

framework是一款基于SSH(springmvc+spring+hibernate)框架、使用Mysql数据库、前台使用EasyUI的开发平台。平台内置generator

代码代码生成器，可以帮助解决Java项目代码编写的重复工作，让开发更多关注业务逻辑。


 **功能点** 
----------------------------------- 
### 1、SSH(springmvc+spring+hibernate)


   没有采用传统的三层架构，将Dao层简化，Dao层仅仅提供session，将业务代码统一写在Service层中，所有Service层都对应唯一一个Dao。

   之所以这样做的是因为，在工作中发现，现有的程序大多是针对业务的，而不是针对表的。所有简化Dao的作用，就避免了一个Service层调用多个Dao

   的情况，使代码更加符合业务逻辑。

   当然，这就导致了架构不符合标准的三层架构，Service层写了全部数据库语句。

### 2、generator代码生成器


   generator代码生成器，使用了mybatis的读取数据库的方法类，然后操作读取到的数据库集合，配合Freemarker技术，完成代码生成。

   当然，要提前写好模板。

### 3、activiti工作流


   activiti对springmvc支持的比较好，而且提供了流程设计器，使用也比较方便，优点挺多，请自行百度。这里说说缺点：开始节点没有

   用户名，需要直接操作数据库进行添加。待办业务查询不是太友好，还是需要自己写。最重要的是，生成流程图路径文字丢失问题，目前尚未解决。

### 4、shiro权限控制


   简单方便，配合annotation注解，可实现方法级的权限控制。

### 5、ireport报表打印

   
   ireport报表是目前我用到过的最好用的报表控件，优点很多。说说缺点，必须安装PDF阅读器，不轻量。以后可能会寻找一种更轻量的打印控件。

### 6、poi操作excel


### 7、记录操作日志log

   
   使用annotation注解、aop切面，完成操作日志记录。

 **数据库初始化** 
----------------------------------- 
   数据库名：framework

   字符集:utf8

   排序规则：utf8_bin

   数据源文件：webapp/resources/framework.sql  