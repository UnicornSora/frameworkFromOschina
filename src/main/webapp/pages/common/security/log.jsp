<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/mytaglib" prefix="cc" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <cc:basic path="<%=path %>"/>
    <cc:gridformatter name="user" code="USER"/>

    <script type="text/javascript">
        $(document).ready(function(e) {
            $('#toolbar-nav').navfix(1, 999);
        });

        //查询
        function submitForm(){
            $('#dategrid').datagrid('reload',{
                tloguser : $('#tloguser').val(),
                tlogname : $('#tlogname').val(),
                start    : $('#start').datebox('getValue'),
                end      : $('#end').datebox('getValue')
            });
        }
    </script>
</head>

<body>
<div id="toolbar-nav">
    <div class="easyui-panel" style="padding:5px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" style="width:80px"  onclick="submitForm()">查询</a>
    </div>
</div>

<div style="margin:10px 0;"></div>

<div class="easyui-panel" title="查询条件"  >
    <table cellpadding="5">
        <tr>
            <td  class="normal">操作用户</td>
            <td><input id = "tloguser" name="tloguser" class="easyui-textbox" style="height:25px;" ></td>
            <td  class="normal">操作描述</td>
            <td><input id = "tlogname"  name="tlogname" class="easyui-textbox" style="height:25px;" ></td>
            <td  class="normal">开始时间</td>
            <td><input id = "start" name="start" class="easyui-datebox" editable="false" data-options="sharedCalendar:'#cc'" ></td>
            <td  class="normal">结束时间</td>
            <td><input id = "end"  name="end" class="easyui-datebox" editable="false" data-options="sharedCalendar:'#cc'" >
                <div id="cc" class="easyui-calendar"></div>
            </td>
        </tr>
    </table>
</div>

<div style="margin:10px 0;"></div>

<table id="dategrid" title="日志信息" class="easyui-datagrid" style="height:370px;" url="logController/gridform.do"
       pagination="true" rownumbers="true"  fitColumns="false" singleSelect="true" resizable="true">
    <thead>
        <tr>
            <th field="tloguser"   width="200" formatter="userformatter" >操作用户</th>
            <th field="tlogname"   width="200" >操作描述</th>
            <th field="tlogip"     width="200" >操作IP</th>
            <th field="tlogtime"   width="100" >操作耗时</th>
            <th field="tlogdate"   width="200"  formatter="Common.DateTimeFormJons" >操作时间</th>
        </tr>
    </thead>
</table>


</body>
</html>

