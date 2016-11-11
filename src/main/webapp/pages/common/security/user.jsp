<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
	<cc:gridformatter name="status" code="USABLE"/>
	<cc:gridformatter name="troleid" code="TROLE"/>
    <cc:gridformatter name="sector" code="SECTOR"/>

    <script type="text/javascript">
	    $(document).ready(function(e) {
			$('#toolbar-nav').navfix(1, 999);
		});

        var url;

		$(function() {
			//datagrid初始化  
			$('#userlist').datagrid({
			    height : 380,
				nowrap : false,//单元格信息自动换行
				striped : true,
				border : true,
				url:'userController/gridform1.do',
				remoteSort : false,
				idField : 'fldId',
				singleSelect : true,//是否单选  
				pagination : true,//分页控件  
				rownumbers : true,//行号  
				columns:[[  
                    {field:'tloginid',title:'用户ID',width:250},  
                    {field:'loginname',title:'登录名',width:120},  
                    {field:'username',title:'用户名',width:120}, 
                    {field:'phone',title:'联系电话',width:120},
                    {field:'registrationtime',title:'注册时间',width:250, formatter:Common.DateTimeFormJons},
                    {field:'troleid',title:'角色名',width:120,formatter: troleidformatter, editor: { type: 'combobox', options: { data: status, valueField: "value", textField: "text" }}},
                    {field:'status', title:'用户状态', width: 120, formatter: statusformatter, editor: { type: 'combobox', options: { data: status, valueField: "value", textField: "text" }}},
                    {field:'sector', title:'所属部门', width: 120, formatter: sectorformatter, editor: { type: 'combobox', options: { data: status, valueField: "value", textField: "text" }}}
                ]],
				toolbar : [ {
					text : '添加',
					iconCls : 'icon-add',
					handler : function() {
						$('#dlg').dialog('open').dialog('setTitle','添加用户');
						$('#fm').form('clear');
                        url = 'userController/usersave.do';
                        $('#loginname_dlg').removeAttr('readonly');
                    }
				}, '-', {
					text : '修改',
					iconCls : 'icon-edit',
					handler : function() {
					    var row = $('#userlist').datagrid('getSelected');
                        if (row){
                            $('#dlg').dialog('open').dialog('setTitle','修改');
                            $('#sector_dlg').combotree('setValue', row.sector);
                            $('#fm').form('load',{
                                id_dlg        : row.tloginid,
                                loginname_dlg : row.loginname,
                                username_dlg  : row.username,
                                phone_dlg     : row.phone,
                                trole_dlg     : row.troleid,
                                status_dlg    : row.status
                            });
                            url = 'userController/userupdate.do';
                            $('#loginname_dlg').attr('readonly', 'readonly');
                        }else{
                           $.messager.show({
                               title: '系统消息',
                                msg: '请选择需要修改的数据！'
                           });
                        };
					}
				}, '-', {
					text : '删除',
					iconCls : 'icon-remove',
					handler : function() {
                        var row = $('#userlist').datagrid('getSelected');
                        if (row) {
                            if(row.loginname_dlg =='superman'){
                                $.messager.show({
                                    title: '系统消息',
                                    msg: '超级管理员不允许删除！'
                                });
                                return ;
                            }
                            $.messager.confirm('提示', '你确定要删除这条信息吗？', function (r) {
                                if (r){
                                    $.post('userController/userdelete.do',{id:row.tloginid},function(result){
                                        if (result.success){
                                            $('#userlist').datagrid('reload');
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
                                msg: '请选择需要删除的数据！'
                            });
                        };
				    }
				},'-',{
                    text : '密码初始化',
                    iconCls : 'icon-edit',
                    handler : function() {
                        var row = $('#userlist').datagrid('getSelected');
                        if (row) {
                            $.messager.confirm('提示', '你确定要初始化此用户的密码吗？', function (r) {
                                if (r){
                                    $.post('userController/initializePassword.do',{id:row.tloginid},function(result){
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
                            });
                        }else{
                            $.messager.show({
                                title: '系统消息',
                                msg: '请选择需要密码初始化的数据！'
                            });
                        };
                    }
                }]
			});
		});
		//查询
		function submitForm(){
            $("#userlist").datagrid('reload',{
                loginname: $('#loginname').val(),
                username : $('#username').val(),
                status   : $('#status').combobox('getValue'),
                trole    : $('#trole').combobox('getValue'),
                sector   : $("#sector").combotree("getValue")
            });
        }

        var checkSubmitFlg = false;
        //保存用户
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
                            $('#userlist').datagrid('reload');
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
        	<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" style="width:80px"  onclick="submitForm()">查询</a>
    	</div>
    </div>
        
  	<div style="margin:10px 0;"></div>

    <div class="easyui-panel" title="查询条件"  >
        <table cellpadding="5">
            <tr>
                <td  class="normal" >登录名</td>
                <td><input id = "loginname" name="loginname" class="easyui-textbox" style="height:25px"></td>
                <td  class="normal">用户名</td>
                <td><input id = "username"  name="username" class="easyui-textbox" style="height:25px"></td>
                <td  class="normal">是否可用</td>
                <td><input id="status" name="status" class="easyui-combobox" style="height:25px" editable="false" data-options=" url:'baseController/getComboBox.do?type=USABLE&isnull=true',valueField:'id',textField:'text'"></td>
            </tr>
            <tr>
            	<td  class="normal">角色</td>
            	<td><input id="trole" name="trole" class="easyui-combobox" style="height:25px" editable="false" data-options=" url:'baseController/getComboBox.do?type=TROLE&isnull=true',valueField:'id',textField:'text'"></td>
                <td class="normal" >所属部门</td>
                <td><input id = "sector" name="sector" class="easyui-combotree" data-options="url:'userController/getSectorTree.do'" style="height:25px"></td>
            </tr>
        </table>
    </div>
    
	<div style="margin:10px 0;"></div>
	
	<table id="userlist" title="用户信息" ></table>

    <div id="dlg" class="easyui-dialog" style=" width:400px; height:300px; padding:10px 20px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons">
        <form id="fm" method="post" >
            <table>
                <tr>
                    <td class="normal" width="200px">登录名</td>
                    <td><input id="loginname_dlg"  name="loginname_dlg" class="easyui-textbox" required="true"  style="height:25px;" data-options="validType:['englishLowerCase','checkLength[50]']" >
                        <input  id="id_dlg"  name="id_dlg"  type="hidden">
                    </td>
                </tr>
                <tr>
                    <td class="normal" width="200px">用户名</td>
                    <td><input id="username_dlg"  name="username_dlg" class="easyui-textbox" required="true"  style="height:25px;" data-options="validType:['chinese','checkLength[50]']"></td>
                </tr>
                <tr>
                    <td class="normal" width="200px">联系电话</td>
                    <td><input id="phone_dlg"  name="phone_dlg" class="easyui-textbox"  style="height:25px;" data-options="validType:['number','length[11,11]']" ></td>
                </tr>
                <tr>
                    <td class="normal"width="200px" >角色</td>
                    <td><input class="easyui-combobox" id="trole_dlg" name="trole_dlg" editable="false" style="height:25px;"  required="true" data-options=" url:'baseController/getComboBox.do?type=TROLE',valueField:'id',textField:'text'"></td>
                </tr>
                <tr>
                    <td class="normal"width="100px" >是否可用</td>
                    <td><input class="easyui-combobox" id="status_dlg" name="status_dlg" editable="false" style="height:25px;"  required="true" data-options=" url:'baseController/getComboBox.do?type=USABLE',valueField:'id',textField:'text'"></td>
                </tr>
                <tr>
                    <td class="normal" >所属部门</td>
                    <td><input id = "sector_dlg" name="sector_dlg" class="easyui-combotree" data-options="url:'userController/getSectorTree.do'" required="true"  style="height:25px"></td>
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
