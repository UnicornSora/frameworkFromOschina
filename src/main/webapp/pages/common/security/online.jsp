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
            $('#dategrid').datagrid('reload');
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
            <td><input id = "type" name="type" class="easyui-textbox" style="height:25px;" ></td>
            <td  class="normal">操作描述</td>
            <td><input id = "value"  name="value" class="easyui-textbox" style="height:25px;" ></td>
        </tr>
    </table>
</div>

<div style="margin:10px 0;"></div>

<table id="dategrid" title="在线用户" class="easyui-datagrid" style="height:370px;" url="onlineController/gridform.do"
       pagination="true" rownumbers="true"  fitColumns="false" singleSelect="true" resizable="true">
    <thead>
        <tr>
            <th field="loginname"  width="200" formatter="userformatter">用户名</th>
        </tr>
    </thead>
</table>

</body>
</html>

