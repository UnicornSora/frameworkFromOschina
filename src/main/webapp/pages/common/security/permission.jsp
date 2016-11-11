<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/mytaglib" prefix="cc"%>
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
<cc:basic path="<%=path %>" />

<script type="text/javascript">
    $(document).ready(function(e) { $('#toolbar-nav').navfix(1, 999); });

    $(function() {
		//初始胡ajax树
		$('#asyncTree').tree({
			onClick : function(node) {
				$.post('permissionController/getPermission.do', {
					id : node.id
				}, function(result) {
					$('#form1').form('load', {
						permissionname : result.permissionname,
						tpermissionid : result.tpermissionid,
						action : result.action,
						url : result.url,
						parentid : result.parentid,
						orders : result.orders
					});
				}, 'json');
			}
		});
	});
	
	//添加按钮
	function clearForm() {
		//添加必须存在父节点id
		var node = $('#asyncTree').tree('getSelected');
		var action = $('#action').combobox('getValue');
		if (action == 2) {
			$.messager.show({
				title : '系统消息',
				msg : '无法添加该权限！'
			});
		} else if (node == null) {
			$.messager.show({
				title : '系统消息',
				msg : '请选择节点!'
			});
		} else {
			$('#form1').form('clear');
			$('#tpermissionid').val(node.id);
		}
	}
	var url;
    var checkSubmitFlg = false;
	//保存
	function addForm() {
		if ($('#parentid').val() == '') {
			url = 'permissionController/addPermission.do';
		} else {
			url = 'permissionController/updatePermission.do';
		};
        //判断，是否是重复提交
        if (!checkSubmitFlg) {
            checkSubmitFlg = true;
            $('#form1').form('submit', {
                url : url,
                onSubmit : function() {
                    var val = $(this).form('validate');
                    if(!val){
                        checkSubmitFlg = false;
                    }
                    return val;
                },
                success : function(result) {
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#form1').form('clear');
                        $('#asyncTree').tree('reload');
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
	
	function deleteForm() {
		$.messager.confirm('提示', '你确定要删除这条信息吗？', function(r) {
			if (r) {
				$.post('permissionController/deletePermission.do', {
					tpermissionid : $('#tpermissionid').val()
				}, function(result) {
                    if (result.success){
                        $('#form1').form('clear');
                        $('#asyncTree').tree('reload');
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
				}, 'json');
			}else{
                $.messager.show({
                    title: '系统消息',
                    msg:'请选择需要删除的数据！'
                });
            }
		});
	}
</script>
</head>

<body>

<div id="toolbar-nav">
    <div class="easyui-panel" style="padding:5px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"	data-options="iconCls:'icon-add'" style="width:80px" onclick="clearForm()">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-save'" style="width:80px" onclick="addForm()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-remove'" style="width:80px" onclick="deleteForm()">删除</a>
    </div>
</div>

<div style="margin:10px 0;"></div>

<div class="easyui-layout" style="width:100%;height:520px;">

    <div data-options="region:'west',title:'权限列表',split:true" style="width:200px;">
        <ul id="asyncTree" class="easyui-tree" data-options="url:'permissionController/getUserTree.do'"></ul>
    </div>

    <div id="mainPanle" data-options="region:'center',title:'属性' " >

            <form id="form1" method="post" >
                <table cellpadding="5">
                    <tr>
                        <td class="normal" style="width:200px;">节点ID:</td>
                        <td><input id="tpermissionid" name="tpermissionid" style="width:200px;height:25px;" readonly="readonly" class="easyui-validatebox textbox" required="true" >
                            <input type="hidden" id="parentid" name="parentid"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="normal" style="width:200px;">权限名:</td>
                        <td><input id="permissionname" name="permissionname" style="width:200px;height:25px;" class="easyui-textbox" required="true" data-options="validType:['chinese','checkLength[30]']" >
                        </td>
                    </tr>
                    <tr>
                        <td class="normal" style="width:200px;">权限类型:</td>
                        <td><input id="action" name="action" style="width:200px;height:25px;" class="easyui-combobox" data-options=" url:'baseController/getComboBox.do?type=ACTION',required:true,valueField:'id',textField:'text',editable:false">
                    </tr>
                    <tr>
                        <td class="normal" style="width:200px;">URL:</td>
                        <td><input id="url" name="url" style="width:200px;height:25px;"  class="easyui-textbox" required="true" data-options="validType:['specialCharacter','checkLength[256]']">
                        </td>
                    <tr>
                    <tr>
                        <td class="normal" style="width:200px;">序号:</td>
                        <td><input id="orders" name="orders" style="width:200px;height:25px;"  class="easyui-textbox" required="true" data-options="validType:['number','checkLength[2]']" >
                        </td>
                    <tr>
                </table>
            </form>

    </div>
</div>





</body>
</html>
