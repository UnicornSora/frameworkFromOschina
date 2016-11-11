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
    <cc:gridformatter name="suspended" code="SUSPENDED"/>
    <script type="text/javascript">
        $(document).ready(function(e) { $('#toolbar-nav').navfix(1, 999); });

        //查询
        function submitForm(){
            $('#dategrid').datagrid('reload',{
                name : $('#name').val() ,
                key  : $('#key').val(),
                suspended  : $('#suspended').combobox('getValue')
            });
        }

        //打开修改页面
        function openEdit(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                $('#dlgEdit').dialog('open').dialog('setTitle','修改');
                $('#fm').form('load',{
                    id_dlg  : row.id,
                    name_dlg  : row.name,
                    suspended_dlg : row.suspended
                });
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要修改的数据！"});
            }
        }

        //保存模型数据，打开流程编辑器
        function updateProcess(){
            $('#fm').form('submit',{
                url: "processController/updateProcess.do",
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#dlgEdit').dialog('close');
                        $.messager.show({
                            title: '系统消息',
                            msg: result.msg
                        });
                        $('#dategrid').datagrid('reload');
                    } else {
                        $.messager.show({
                            title: '系统消息',
                            msg: result.msg
                        });
                    }
                }
            });
        }

        //删除
        function deleteProcess(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('系统消息','你确定要删除这条信息吗？',function(r){
                    if (r){
                        $.post('processController/deleteProcess.do',{deploymentId:row.deploymentId},function(result){
                            if (result.success){
                                $('#dategrid').datagrid('reload');
                                $.messager.show({
                                    title: '系统消息',
                                    msg: result.msg
                                });
                            } else {
                                $.messager.show({
                                    title: '系统消息',
                                    msg: result.msg
                                });
                            }
                        },'json');
                        $('#dategrid').datagrid('reload');
                    }
                });
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要删除的数据！"});
            }
        }

        //查看流程xml
        function showXML(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                window.open ('<%=request.getContextPath() %>/processController/resourceRead.do?processDefinitionId='+row.deploymentId+'&resourceName='+row.resourceName);
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要查看的数据！"});
            }
        }

        //查看流程xml
        function showPic(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                parent.$('#tabs').tabs("add", {
                    title: '流程图',
                    content: '<iframe scrolling="auto" frameborder="0"  src="processController/resourceRead.do?processDefinitionId='+row.deploymentId+'&resourceName='+row.diagramResourceName+'" style="width:100%;height:100%;"></iframe>',
                    closable: true
                });
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要查看的数据！"});
            }
        }

        //测试流程
        function test(){
            $.post('processController/testProcess.do',function(result){
                if (result.success){
                    $.messager.show({
                        title: '系统消息',
                        msg: result.msg
                    });
                } else {
                    $.messager.show({
                        title: '系统消息',
                        msg: result.msg
                    });
                }
            },'json');
        }

    </script>
</head>
<body>

<div id="toolbar-nav">
    <div class="easyui-panel" style="padding:5px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" style="width:80px" onclick="submitForm()">查询</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-redo'" style="width:80px" onclick="test()">测试流程</a>
    </div>
</div>

<div style="margin:10px 0;"></div>

<div class="easyui-panel" title="查询条件"  >
    <table cellpadding="5">
        <tr>
            <td class="normal" >KEY</td>
            <td><input id = "key" name="key" class="easyui-textbox" style="height:25px" ></td>
            <td class="normal" >名称</td>
            <td><input id = "name" name="name" class="easyui-textbox" style="height:25px" ></td>
            <td  class="normal">状态</td>
            <td><input class="easyui-combobox" id="suspended" name="suspended" style="height:25px" editable="false" data-options=" url:'baseController/getComboBox.do?type=SUSPENDED&isnull=true',valueField:'id',textField:'text'"></td>
        </tr>
    </table>
</div>

<div style="margin:10px 0;"></div>

<table id="dategrid" title="流程列表" class="easyui-datagrid" style="height:370px;" url="processController/gridform.do"
       toolbar="#toolbar" pagination="true" rownumbers="true"  fitColumns ="false" singleSelect="true" resizable="true">
    <thead>
    <tr>
        <th field="id"                  width="300">ID</th>
        <th field="deploymentId"        width="300">部署ID</th>
        <th field="diagramResourceName" width="150">流程图名称</th>
        <th field="resourceName"        width="100">源文件名称</th>
        <th field="key"                 width="100">KEY</th>
        <th field="name"                width="100">名称</th>
        <th field="version"             width="50">版本</th>
        <th field="deploymentTime"      width="150" formatter="Common.DateTimeFormJons">部署时间</th>
        <th field="suspended"           width="50"  formatter="suspendedformatter" >状态</th>
    </tr>
    </thead>
</table>
<div id="toolbar">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openEdit()">修改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteProcess()">删除</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-picture" plain="true" onclick="showPic()">查看流程图</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-xml" plain="true" onclick="showXML()">查看XML</a>
</div>

<div id="dlgEdit" class="easyui-dialog" style=" width:400px; height:280px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons" >
    <form id="fm" method="post" >
        <table>
            <tr>
                <td class="normal" width="200px">名称</td>
                <td><input id="name_dlg"  name="name_dlg" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['chinese','checkLength[50]']" ></td>
                <input id="id_dlg" name="id_dlg" type="hidden">
            </tr>
            <tr>
                <td class="normal" width="200px">状态</td>
                <td><input class="easyui-combobox" id="suspended_dlg" name="suspended_dlg" style="height:25px;width:200px;" editable="false" data-options=" url:'baseController/getComboBox.do?type=SUSPENDED&isnull=false',valueField:'id',textField:'text'"></td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a id="dlg-save"  href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="updateProcess()">保存</a>
    <a id="dlg-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgEdit').dialog('close')">关闭</a>
</div>

</body>
</html>