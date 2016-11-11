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
    <cc:gridformatter name="areatype" code="AREATYPE"/>

    <script type="text/javascript">
        $(function(){
            $('#toolbar-nav').navfix(1, 999);
        });
        var url;
        //打开添加页面
        function openAdd(){
            $('#dlgAddOrEdit').dialog('open').dialog('setTitle','添加');
            $('#fm').form('clear');
            url = 'areaController/add.do';
            $('#parentcode_dlg').combotree('reload');
        }
        //打开修改页面
        function openEdit(){
            var row = $('#ptareadategrid').datagrid('getSelected');
            if (row){
                $('#dlgAddOrEdit').dialog('open').dialog('setTitle','修改');
                $('#parentcode_dlg').combotree('setValue', row.parentcode);
                $('#fm').form('load',{
                    areaid_dlg  : row.areaid,
                    code_dlg  : row.code,
                    name_dlg  : row.name,
                    areatype_dlg : row.areatype
                });
                url = 'areaController/update.do';
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
                            $('#ptareadategrid').datagrid('reload');
                            $('#areatree').combotree('reload');
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
        function deleteArea(){
            var row = $('#ptareadategrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('提示','你确定要删除这条信息吗？',function(r){
                    if (r){
                        $.post('areaController/delete.do',{areaid:row.areaid},function(result){
                            if (result.success){
                                $('#ptareadategrid').datagrid('reload');
                                $.messager.show({
                                    title: '系统消息',
                                    msg: result.msg
                                });
                                $('#areatree').combotree('reload');
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
        //查询
        function submitForm(){
            $('#ptareadategrid').datagrid('reload',{
                areatree : $("#areatree").combotree("getValue")
            });
        }
        //打开顶级区划管理页面
        function opentoparea(){
            $('#dlg_toparea').dialog('open').dialog('setTitle','顶级区划管理');
            $.post("areaController/getTopArea.do", function(result) {
                if(result.msg == 'update' ){
                    $('#fm_toparea').form('load', {
                        areaid_toparea : result.p.areaid,
                        parentcode_toparea : result.p.parentcode,
                        name_toparea : result.p.name,
                        code_toparea : result.p.code,
                        areatype_toparea : result.p.areatype
                    });
                    url = 'areaController/updateTopArea.do';
                }else{
                    url = 'areaController/saveTopArea.do';
                };
            },'json');
        }
        //保存顶级区划
        function save_toparea(){
            //判断，是否是重复提交
            if (!checkSubmitFlg) {
                checkSubmitFlg = true;
                $('#fm_toparea').form('submit',{
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
                            $('#dlg_toparea').dialog('close');
                            $.messager.show({
                                title: '系统消息',
                                msg: result.msg
                            });
                            $('#areatree').combotree('reload');
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
    </script>
</head>
<body>
<div id="toolbar-nav">
    <div class="easyui-panel" style="padding:5px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" style="width:80px" onclick="submitForm()">查询</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-add'" style="width:120px" onclick="opentoparea()">顶级区划管理</a>
    </div>
</div>

<div style="margin:10px 0;"></div>

<div class="easyui-panel" title="查询条件"  >
    <table cellpadding="5">
        <tr>
            <td class="normal" >区划</td>
            <td><input id = "areatree" name="areatree" class="easyui-combotree" data-options="url:'areaController/getAreaTree.do'" style="height:25px"></td>
        </tr>
    </table>
</div>

<div style="margin:10px 0;"></div>

<table id="ptareadategrid" title="区划信息" class="easyui-datagrid" style="height:370px;" url="areaController/gridform1.do"
       toolbar="#toolbar" pagination="true" rownumbers="true"  fitColumns="false" singleSelect="true" resizable="true">
    <thead>
    <tr>
        <th field="areaid" width="200" hidden="true" >ID</th>
        <th field="parentcode" width="200">父区划代码</th>
        <th field="code"     width="200" >区划代码</th>
        <th field="name"     width="200" >区划名称</th>
        <th field="areatype" width="200"  formatter="areatypeformatter" >区划类型</th>
    </tr>
    </thead>
</table>
<div id="toolbar">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAdd()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openEdit()">修改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteArea()">删除</a>
</div>

<div id="dlgAddOrEdit" class="easyui-dialog" style=" width:400px; height:300px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post" >
        <table>
            <tr>
                <td class="normal" width="200px">父级区划名称</td>
                <td><input id = "parentcode_dlg" name="parentcode_dlg" class="easyui-combotree" data-options="url:'areaController/getAreaTree.do',required:true" style="width:200px;height:25px">
                    <input id="areaid_dlg" name="areaid_dlg" type="hidden">
                </td>
            </tr>
            <tr>
                <td class="normal" width="200px">区划名称</td>
                <td><input id="name_dlg"  name="name_dlg" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['chinese','checkLength[36]']"></td>
            </tr>
            <tr>
                <td class="normal" width="200px">区划代码</td>
                <td><input id="code_dlg"  name="code_dlg" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['number','length[1,12]']"></td>
            </tr>
            <tr>
                <td class="normal" width="200px">区划类型</td>
                <td><input class="easyui-combobox" id="areatype_dlg" name="areatype_dlg" style="width:200px;height:25px;" required="true" editable="false" data-options=" url:'baseController/getComboBox.do?type=AREATYPE&isnull=false',valueField:'id',textField:'text'"></td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a id="dlg-save"  href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
    <a id="dlg-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgAddOrEdit').dialog('close')">关闭</a>
</div>

<div id="dlg_toparea" class="easyui-dialog" style=" width:400px; height:300px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true" buttons="#buttons_toparea">
    <form id="fm_toparea" method="post" >
        <table>
            <tr>
                <td class="normal" width="200px">父级区划代码</td>
                <td><input id="parentcode_toparea"  name="parentcode_toparea" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['number','length[1,12]']" >
                    <input id="areaid_toparea" name="areaid_toparea" type="hidden">
                </td>
            </tr>
            <tr>
                <td class="normal" width="200px">区划名称</td>
                <td><input id="name_toparea"  name="name_toparea" class="easyui-textbox" required="true"  style="width:200px;height:25px;" data-options="validType:['chinese','checkLength[36]']" ></td>
            </tr>
            <tr>
                <td class="normal" width="200px">区划代码</td>
                <td><input id="code_toparea"  name="code_toparea" class="easyui-textbox" required="true"  style="width:200px;height:25px;"  data-options="validType:['number','length[1,12]']" ></td>
            </tr>
            <tr>
                <td class="normal" width="200px">区划类型</td>
                <td><input class="easyui-combobox" id="areatype_toparea" name="areatype_toparea" style="width:200px;height:25px;" required="true" editable="false" data-options=" url:'baseController/getComboBox.do?type=AREATYPE&isnull=false',valueField:'id',textField:'text'"></td>
            </tr>
        </table>
    </form>
</div>
<div id="buttons_toparea">
    <a id="save_toparea"  href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="save_toparea()">保存</a>
    <a id="close_toparea" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_toparea').dialog('close')">关闭</a>
</div>

</body>
</html>