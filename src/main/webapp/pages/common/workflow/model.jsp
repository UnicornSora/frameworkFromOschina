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
    <script type="text/javascript">
        $(document).ready(function(e) { $('#toolbar-nav').navfix(1, 999); });

        //查询
        function submitForm(){
            $('#dategrid').datagrid('reload',{
                key : $('#key').val(),
                name : $('#name').val(),
                metainfo : $('#metainfo').val()
            });
        }

        var url;
        //打开添加页面
        function openAdd(){
            url = 'modelController/createModel.do';
            $('#dlgAdd').dialog('open').dialog('setTitle','添加');
            $('#fm').form('clear');
        }
        //保存模型数据，打开流程编辑器
        function saveModel(){
            $('#fm').form('submit',{
                url: url,
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#dlgAdd').dialog('close');
                        $.messager.show({
                            title: '系统消息',
                            msg: result.msg
                        });
                        parent.$('#tabs').tabs("add", {
                            title: '模型设计器',
                            content: '<iframe scrolling="auto" frameborder="0"  src="modeler.html?modelId='+result.modelId+'" style="width:100%;height:100%;"></iframe>',
                            closable: true
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
        function deleteModel(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('系统消息','你确定要删除这条信息吗？',function(r){
                    if (r){
                        $.post('modelController/deleteModel.do',{id:row.id},function(result){
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
                    }
                });
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要删除的数据！"});
            }
        }
        //打开修改页面
        function openEdit(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                url = 'modelController/updateModel.do';
                $('#dlgAdd').dialog('open').dialog('setTitle','修改');
                $('#fm').form('load',{
                    id_dlg       : row.id,
                    name_dlg     : row.name,
                    key_dlg      : row.key ,
                    metaInfo_dlg :  eval('('+row.metainfo+')').description
                });
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要修改的数据！"});
            }
        }

        //查看流程图图片
        function showPic(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                parent.$('#tabs').tabs("add", {
                    title: '流程图',
                    content: '<iframe scrolling="auto" frameborder="0"  src="modelController/showPic.do?id='+row.id+'" style="width:100%;height:100%;"></iframe>',
                    closable: true
                });
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要查看的数据！"});
            }
        }

        //查看流程xml
        function showXML(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                window.open ('<%=request.getContextPath() %>/modelController/showXML.do?id='+row.id);
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要查看的数据！"});
            }
        }

        //部署
        function deployModel(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                $.post('modelController/deployModel.do',{id:row.id},function(result){
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
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要部署的数据！"});
            }
        }

    </script>
</head>
<body>

<div id="toolbar-nav">
    <div class="easyui-panel" style="padding:5px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" style="width:80px" onclick="submitForm()">查询</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-redo'" style="width:80px" onclick="deployModel()">部署</a>
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
            <td class="normal" >元数据</td>
            <td><input id = "metainfo" name="metainfo" class="easyui-textbox" style="height:25px" ></td>
        </tr>
    </table>
</div>

<div style="margin:10px 0;"></div>

<table id="dategrid" title="模型列表" class="easyui-datagrid" style="height:370px;" url="modelController/gridform.do"
       toolbar="#toolbar" pagination="true" rownumbers="true"  fitColumns="false" singleSelect="true" resizable="true">
    <thead>
    <tr>
        <th field="id"             width="200" hidden="hidden" >ID</th>
        <th field="key"            width="100">KEY</th>
        <th field="name"           width="100">名称</th>
        <th field="createtime"     width="200" formatter="Common.DateTimeFormJons">创建时间</th>
        <th field="lastupdatetime" width="200" formatter="Common.DateTimeFormJons">更新时间</th>
        <th field="metainfo"       width="400">元数据</th>
    </tr>
    </thead>
</table>
<div id="toolbar">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAdd()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openEdit()">修改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteModel()">删除</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-picture" plain="true" onclick="showPic()">查看流程图</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-xml" plain="true" onclick="showXML()">查看XML</a>
</div>

<div id="dlgAdd" class="easyui-dialog" style=" width:400px; height:300px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post" >
        <table>
            <tr>
                <td class="normal" width="200px">名称</td>
                <td><input id="name_dlg"  name="name_dlg" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['englishLowerCase','checkLength[50]']"></td>
                <input id="id_dlg" name="id_dlg" type="hidden">
            </tr>
            <tr>
                <td class="normal" width="200px">KEY</td>
                <td><input id="key_dlg"  name="key_dlg" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['englishUpperCase','checkLength[50]']" ></td>
            </tr>
            <tr>
                <td class="normal" width="200px">描述</td>
                <td><input id="metaInfo_dlg"  name="metaInfo_dlg" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['chinese','checkLength[50]']" ></td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a id="dlg-save"  href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveModel()">保存</a>
    <a id="dlg-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgAdd').dialog('close')">关闭</a>
</div>

</body>
</html>