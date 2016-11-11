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
    <cc:gridformatter name="agree" code="AGREE"/>

    <script type="text/javascript">
        $(function() {
            $('#toolbar-nav').navfix(1, 999);
        })

        //查看流程图图片
        function showPic(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                parent.$('#tabs').tabs("add", {
                    title: '流程图',
                    content: '<iframe scrolling="auto" frameborder="0"  src="doneController/traceProcess.do?processInstanceId='+row.procinstid+'" style="width:100%;height:100%;"></iframe>',
                    closable: true
                });
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要查看的数据！"});
            }
        }

        //打开明细页面
        function openWindow(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                //加载审批明细
                $.post('doneController/gridformnode.do',{executionid:row.executionid},function(result){
                    $("#dategrid_node").datagrid({
                        data: result.rows
                    });
                },'json');
                $('#dlg').dialog('open').dialog('setTitle','业务单明细');
            }else{
                $.messager.show({title : '系统消息',msg : "请选择要查看的数据！"});
            }
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

<table id="dategrid" title="已办审批" class="easyui-datagrid" style="height:390px;" url="doneController/gridform.do"
       toolbar="#toolbar" pagination="true" rownumbers="true"  fitColumns="false" singleSelect="true" resizable="true">
    <thead>
    <tr>
        <th field="executionid" hidden="true" >executionid</th>
        <th field="procinstid"  hidden="true" >procinstid</th>
        <th field="actname"     width="200" >流程节点</th>
        <th field="endtime"     width="200" formatter="Common.DateTimeFormJons" >办结时间</th>
    </tr>
    </thead>
</table>
<div id="toolbar">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="showPic()">审批流程</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="openWindow()">申请明细</a>
</div>

<div id="dlg" class="easyui-dialog" style=" width:800px; height:400px; padding:10px 10px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons" >
    <table id="dategrid_node" title="审批信息" class="easyui-datagrid" style="height:300px;" rownumbers="true"  fitColumns="false" singleSelect="true" resizable="true">
        <thead>
        <tr>
            <th field="actname"   width="150" >节点明细</th>
            <th field="assignee"  width="150" formatter="userformatter" >审批人</th>
            <th field="agree"     width="100" formatter="agreeformatter" >是否同意</th>
            <th field="opinion"   width="150" >意见</th>
            <th field="starttime" width="200" >开始时间</th>
            <th field="endtime"   width="200" >完成时间</th>
        </tr>
        </thead>
    </table>
</div>
<div id="dlg-buttons">
    <a id="dlg-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
</div>

</body>
</html>