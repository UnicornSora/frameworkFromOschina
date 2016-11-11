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
            $('#troledategrid').datagrid('reload',{
                rolename  : $('#rolename').val()
            });
        }
        var url;
        //打开新增页面
        function openAdd(){
            $('#dlg').dialog('open').dialog('setTitle','添加');
            $('#fm').form('clear');
            url = 'roleController/save.do';
        }
        //打开授权页面
        function openSave(){
            var row = $('#troledategrid').datagrid('getSelected');
            if (row){
           		 $('#fm_role').form('load',{
           		 	role_id : row.troleid,
                    role_name : row.rolename
                });
                $('#roleTree').tree({
                    url:'roleController/getUserTree.do?roleid='+row.troleid,
                    onLoadSuccess:function(node,data){
				        $('#roleTree').tree('expandAll');
                    }
                });
	            $('#dlg_role').dialog('open').dialog('setTitle','授权');
            }else{
                $.messager.show({
                    title: '系统消息',
                    msg:'请选择需要授权的角色！'
                });
            }
        }
        //打开修改页面
        function openEdit(){
            var row = $('#troledategrid').datagrid('getSelected');
            if (row){
                $('#dlg').dialog('open').dialog('setTitle','修改');
                $('#fm').form('load',{
                    troleid_dlg  : row.troleid,
                    rolename_dlg : row.rolename
                });
                 url = 'roleController/update.do';
            }else{
                $.messager.show({
                    title: '系统消息',
                    msg:'请选择需要修改的数据！'
                });
            }
        }
        //授权保存
        function role_save(){
        var role_text_1 = new Array();
        var role_text_2 = new Array();
       	role_text_1 = $('#roleTree').tree('getChecked');
       	role_text_2 = $('#roleTree').tree('getChecked', 'indeterminate');
       	var tree_text="";
       	for(var i=0;i<role_text_1.length;i++){
       		tree_text += role_text_1[i].id;
       		tree_text+=",";	
       	}
       	for(var i=0;i<role_text_2.length;i++){
       		tree_text += role_text_2[i].id;
       		tree_text+=",";
       	}
        $('#role_tree_text').val(tree_text);
	         $('#fm_role').form('submit',{
                url : 'roleController/role_save.do',
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#dlg_role').dialog('close');
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
                }
            });
	          
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
                            $('#troledategrid').datagrid('reload');
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
            var row = $('#troledategrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('提示','你确定要删除这条信息吗？',function(r){
                    if (r){
                        $.post('roleController/delete.do',{troleid:row.troleid},function(result){
                            if (result.success){
                                $('#troledategrid').datagrid('reload');
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
                <td  class="normal" >角色名</td>
                <td><input id = "rolename" name="rolename" class="easyui-textbox" style="height:25px" ></td>
            </tr>
        </table>
    </div>
    
    <div style="margin:10px 0;"></div>
    
    <table id="troledategrid" title="角色信息" class="easyui-datagrid" style="height:370px;" url="roleController/gridform1.do"
           toolbar="#toolbar" pagination="true" rownumbers="true"  fitColumns="false" singleSelect="true" resizable="true">
	        <thead>
	            <tr>
	                <th field="troleid" width="200" hidden="true" >ID</th>
	                <th field="rolename" width="200" >角色名</th>
	            </tr>
	        </thead>
    </table>
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAdd()">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openEdit()">修改</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="openSave()">授权</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteObject()">删除</a>
    </div>
    
    <div id="dlg" class="easyui-dialog" style=" width:400px; height:300px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons">
        <form id="fm" method="post" >
			<table>
				<tr>
					<td class="normal" width="200px">角色名</td>
					<td><input id="rolename_dlg"  name="rolename_dlg" class="easyui-textbox" required="true"  style="height:25px;" data-options="validType:['chinese','checkLength[50]']">
						<input id="troleid_dlg" name="troleid_dlg" type="hidden">
					</td>
				</tr>
			</table>
        </form>
    </div>
    <div id="dlg-buttons">
        <a id="dlg-save"  href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
        <a id="dlg-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
    </div>
    
    <div id="dlg_role" class="easyui-dialog" style=" width:280px; height:450px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true"  buttons="#dlg-role-buttons">
       <form id="fm_role" method="post" >
      	  <input id="role_tree_text" name="role_tree_text" type="hidden" >
      	  <input id="role_name" name="role_name" type="hidden" >
      	  <input id="role_id" name="role_id" type="hidden" >
      	  <ul id="roleTree" class="easyui-tree" checkbox="true" ></ul>
 	   </form>
    </div>
    <div id="dlg-role-buttons">
        <a id="dlg-role-save" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="role_save()">保存</a>
        <a id="dlg-role-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_role').dialog('close')">关闭</a>
    </div>
    
  </body>
</html>

