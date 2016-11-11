<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/mytaglib" prefix="cc" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!doctype html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <cc:basic path="<%=path %>"/>
    <cc:gridformatter name="tarea" code="TAREA"/>

    <script type="text/javascript">
        $(function(){
            $('#toolbar-nav').navfix(1, 999);
        });
        //查询
        function submitForm(){
            $('#dategrid').datagrid('reload',{
                areatree : $("#areatree").combotree("getValue"),
                name     : $('#name').val()
            });
        }
        var url;
        //打开添加页面
        function openAdd(){
            $('#dlgAddOrEdit').dialog('open').dialog('setTitle','添加');
            $('#fm').form('clear');
            url = 'departmentController/add.do';
        }
        //打开修改页面
        function openEdit(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                $('#dlgAddOrEdit').dialog('open').dialog('setTitle','修改');
                $('#areacode_dlg').combotree('setValue', row.areacode);
                $('#fm').form('load',{
                    departmentid_dlg  : row.departmentid,
                    name_dlg  : row.name,
                    address_dlg  : row.address
                });
                url = 'departmentController/update.do';
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
                            $('#dlgAddOrEdit').dialog('close');
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
        function deleteDepartment(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('提示','你确定要删除这条信息吗？',function(r){
                    if (r){
                        $.post('departmentController/delete.do',{departmentid:row.departmentid},function(result){
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
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" style="width:80px" onclick="submitForm()">查询</a>
    </div>
</div>

<div style="margin:10px 0;"></div>

<div class="easyui-panel" title="查询条件"  >
    <table cellpadding="5">
        <tr>
            <td class="normal" >所属行政区划</td>
            <td><input id = "areatree" name="areatree" class="easyui-combotree" data-options="url:'areaController/getAreaTree.do'" style="height:25px"></td>
            <td  class="normal" >单位名称</td>
            <td><input id = "name" name="name" class="easyui-textbox" style="height:25px" ></td>
        </tr>
    </table>
</div>

<div style="margin:10px 0;"></div>

<table id="dategrid" title="单位信息" class="easyui-datagrid" style="height:370px;" url="departmentController/gridform1.do"
       toolbar="#toolbar" pagination="true" rownumbers="true"  fitColumns="false" singleSelect="true" resizable="true">
    <thead>
        <tr>
            <th field="departmentid" width="200" hidden="true" >ID</th>
            <th field="areacode"     width="200" formatter="tareaformatter" >所属区划</th>
            <th field="name"         width="200">单位名称</th>
            <th field="address"      width="300">单位地址</th>
        </tr>
    </thead>
</table>
<div id="toolbar">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAdd()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openEdit()">修改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteDepartment()">删除</a>
</div>

<div id="dlgAddOrEdit" class="easyui-dialog" style=" width:400px; height:300px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post" >
        <table>
            <tr>
                <td class="normal" width="200px">所属区划</td>
                <td><input id = "areacode_dlg" name="areacode_dlg" class="easyui-combotree" data-options="url:'areaController/getAreaTree.do',required:true" style="width:200px;height:25px">
                    <input id="departmentid_dlg" name="departmentid_dlg" type="hidden">
                </td>
            </tr>
            <tr>
                <td class="normal" width="200px">单位名称</td>
                <td><input id="name_dlg"  name="name_dlg" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['specialCharacter','checkLength[36]']"></td>
            </tr>
            <tr>
                <td class="normal" width="200px">单位地址</td>
                <td><input id="address_dlg"  name="address_dlg" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['specialCharacter','checkLength[200]']"></td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a id="dlg-save"  href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
    <a id="dlg-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgAddOrEdit').dialog('close')">关闭</a>
</div>

</body>
</html>