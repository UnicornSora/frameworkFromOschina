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
    <cc:gridformatter name="tdepartment" code="TDEPARTMENT"/>

    <script type="text/javascript">
        $(document).ready(function(e) {
            $('#toolbar-nav').navfix(1, 999);
        });
        //查询
        function submitForm(){
            $('#dategrid').datagrid('reload',{
                departmenttree : $("#departmenttree").combotree("getValue"),
                name           : $('#name').val()
            });
        }
        var url;
        //打开新增页面
        function openAdd(){
            $('#dlg').dialog('open').dialog('setTitle','添加');
            $('#fm').form('clear');
            url = 'sectorController/save.do';
        }
        //打开修改页面
        function openEdit(){
            var row = $('#dategrid').datagrid('getSelected');
            if (row){
                $('#dlg').dialog('open').dialog('setTitle','修改');
                $('#departmenttree_dlg').combotree('setValue', row.tdepartmentid);
                $('#fm').form('load',{
                    sectorid_dlg   : row.tsectorid,
                    sectorname_dlg : row.tsectorname,
                    linkman_dlg    : row.linkman,
                    phone_dlg      : row.phone
                });
                url = 'sectorController/update.do';
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
                        $.post('sectorController/delete.do',{
                            tsectorid : row.tsectorid
                            //获取对象
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

<div style="margin:10px 0;"></div>

<div class="easyui-panel" title="查询条件"  >
    <table cellpadding="5">
        <tr>
            <td class="normal" >所属单位</td>
            <td><input id = "departmenttree" name="departmenttree" class="easyui-combotree" data-options="url:'sectorController/getTree.do'" style="height:25px"></td>
            <td  class="normal" >部门名称:</td>
            <td><input id = "name" name="name" class="easyui-textbox" style="height:25px" ></td>
        </tr>
    </table>
</div>

<div style="margin:10px 0;"></div>

<table id="dategrid" title="部门信息" class="easyui-datagrid" style="height:370px;" url="sectorController/gridform.do"
       toolbar="#toolbar" pagination="true" rownumbers="true"  fitColumns="false" singleSelect="true" resizable="true">
    <thead>
        <tr>
            <th field="tsectorid"     width="200"  hidden="true" >部门id</th>
            <th field="tdepartmentid" width="200"  formatter="tdepartmentformatter" >所属单位</th>
            <th field="tsectorname"   width="200" >部门名称</th>
            <th field="linkman"       width="200" >部门联系人</th>
            <th field="phone"         width="200" >联系人电话</th>
        </tr>
    </thead>
</table>
<div id="toolbar">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAdd()">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openEdit()">修改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteObject()">删除</a>
</div>

<div id="dlg" class="easyui-dialog" style=" width:400px; height:300px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post" >
        <table>
            <tr>
                <td class="normal" width="200px">所属单位</td>
                <td><input id = "departmenttree_dlg" name="departmenttree_dlg" class="easyui-combotree" required="true"  data-options="url:'sectorController/getTree.do'" style="height:25px" />
                    <input id="sectorid_dlg"  name="sectorid_dlg" type="hidden" />
                </td>
            </tr>
            <tr>
                <td class="normal" width="200px">部门名称</td>
                <td><input id="sectorname_dlg"  name="sectorname_dlg" class="easyui-textbox" required="true"  style="height:25px;" data-options="validType:['chinese','checkLength[50]']"  /></td>
            </tr>
            <tr>
                <td class="normal" width="200px">联系人</td>
                <td><input id="linkman_dlg"  name="linkman_dlg" class="easyui-textbox"   style="height:25px;" data-options="validType:['chinese','checkLength[50]']" /></td>
            </tr>
            <tr>
                <td class="normal" width="200px">联系电话</td>
                <td><input id="phone_dlg"  name="phone_dlg" class="easyui-textbox"  style="height:25px;"  data-options="validType:['number','length[11,11]']" /></td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a id="dlg-save"  href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
    <a id="dlg-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
</div>

</body>
</html>

