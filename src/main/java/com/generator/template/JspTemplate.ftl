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
        $(document).ready(function(e) {
            $('#toolbar-nav').navfix(1, 999);
        });
        //查询
        function submitForm(){
            $('#dategrid').datagrid('reload');
        }
        var url;
        //打开新增页面
        function openAdd(){
            $('#dlg').dialog('open').dialog('setTitle','添加');
            $('#fm').form('clear');
            url = '${class}Controller/save.do';
        }
        //打开修改页面
        function openEdit(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                $('#dlg').dialog('open').dialog('setTitle','修改');
                $('#fm').form('load',{
                <#list properties as prop>
                    <#if prop_has_next>
                    ${prop.name}_dlg  : row.${prop.name},
                    <#else>
                    ${prop.name}_dlg  : row.${prop.name}
                    </#if>
                </#list>
                });
                url = '${class}Controller/update.do';
            }else{
                $.messager.show({
                    title: '系统消息',
                    msg:'请选择需要修改的数据！'
                });
            }
        }

        var checkSubmitFlg = false;
        //保存
        function save(){
            //判断，是否是重复提交
            if (!checkSubmitFlg) {
                checkSubmitFlg = true;
                $('#fm').form('submit',{
                    url: url,
                    onSubmit: function(){
                        var val = $(this).form('validate');
                        if(!val){
                            checkSubmitFlg = false;
                        }
                        return val;
                    },
                    success: function(result){
                        var result = eval('('+result+')');
                        if (result.success){
                            $('#dlg').dialog('close');
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
                        checkSubmitFlg = false;
                    }
                });
            }else{
                $.messager.show({
                    title: '系统消息',
                    msg: '不能重复提交！'
                });
            }
        }
        //删除
        function deleteObject(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('提示','你确定要删除这条信息吗？',function(r){
                    if (r){
                        $.post('${class}Controller/delete.do',{
                        <#list properties as prop>
                            <#if prop.isPk == "true">
                            ${prop.name} : row.${prop.name}
                            //获取对象
                            </#if>
                        </#list>
                            },function(result){
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
                $.messager.show({
                    title: '系统消息',
                    msg:'请选择需要删除的数据！'
                });
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

<div style="margin:20px 0;"></div>

<div class="easyui-panel" title="查询条件"  >
    <table cellpadding="5">
        <tr>
            <td class="normal" ></td>
            <td></td>
        </tr>
    </table>
</div>

<div style="margin:20px 0;"></div>

<table id="dategrid" title="角色信息" class="easyui-datagrid" style="height:370px;" url="${class}Controller/gridform.do"
       toolbar="#toolbar" pagination="true" rownumbers="true"  fitColumns=false singleSelect="true" resizable="true">
    <thead>
        <tr>
    <#list properties as prop>
            <th field="${prop.name}" width="200" >${prop.remarks}</th>
    </#list>
        </tr>
    </thead>
</table>
<div id="toolbar">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAdd()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openEdit()">修改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteObject()">删除</a>
</div>

<div id="dlg" class="easyui-dialog" style=" width:400px; height:280px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post" >
        <table>
        <#list properties as prop>
            <tr>
                <td class="normal" width="200px">${prop.remarks}</td>
                <td><input id="${prop.name}_dlg"  name="${prop.name}_dlg" class="easyui-textbox" required="true"  style="height:25px;" /></td>
            </tr>
        </#list>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a id="dlg-save"  href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
    <a id="dlg-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
</div>

</body>
</html>

