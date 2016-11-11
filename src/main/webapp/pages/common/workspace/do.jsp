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

    <script type="text/javascript">
        $(function() {
            $('#toolbar-nav').navfix(1, 999);
            //datagrid初始化
            $('#dategrid').datagrid({
                onLoadSuccess:function(data){
                    $("a[name='opera']").linkbutton({text:'',plain:true,iconCls:'icon-edit'});
                }
            })
        })

        function formatterOperation(val,row,index){
            return '<a href="javascript:void(0)" name="opera" class="easyui-linkbutton"  onclick="openComplete('+index+')" />';
        }

        //打开审批页面
        function openComplete(index){
            $('#dategrid').datagrid('selectRow',index);// 关键在这里
            var row = $('#dategrid').datagrid('getSelected');
            //保存赋值
            $('#fm_dlg').form('load',{
                agree_dlg   : 'yes',
                opinion_dlg : '同意',
                id_dlg      : row.id,
                task_def_key_dlg : row.task_def_key,
                key_dlg     : row.key
            });
            //打开页面
            $('#dlg').dialog('open').dialog('setTitle','审批');
        }

        //保存审批结果
        var checkSubmitFlg = false;
        function complete(){
            //判断，是否是重复提交
            if (!checkSubmitFlg) {
                checkSubmitFlg = true;
                $('#fm_dlg').form('submit',{
                    url: "doController/complete.do",
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

    </script>
</head>
<body>

<div id="toolbar-nav">
    <div class="easyui-panel" style="padding:5px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" style="width:80px"  onclick="">查询</a>
    </div>
</div>

<div style="margin:10px 0;"></div>

<table id="dategrid" title="待办业务" class="easyui-datagrid" style="height:370px;" url="doController/gridform.do"
       rownumbers="false"  fitColumns="true" singleSelect="true" resizable="true">
    <thead>
        <tr>
            <th data-options="field:'operation',width:200,align:'center',formatter:formatterOperation">审批</th>
            <th field="id"           hidden="true" >ID</th>
            <th field="execution_id" hidden="true" >部署ID</th>
            <th field="proc_inst_id" hidden="true" >业务ID</th>
            <th field="task_def_key" hidden="true" >节点key</th>
            <th field="name"         width="200"   >审批节点</th>
            <th field="user_id"      width="200" formatter="userformatter" >申请用户</th>
            <th field="create_time"  width="200" formatter="Common.DateTimeFormJons">开始时间</th>
            <th field="key"          hidden="true" >key</th>
        </tr>
    </thead>
</table>

<div id="dlg" class="easyui-dialog" style=" width:800px; height:400px; padding:10px 10px;" data-options="resizable:true,modal:true"  closed="true" buttons="#dlg-buttons" >
    <div class="easyui-panel" style="padding:5px;">
        <form id="fm_dlg" method="post" >
            <table cellpadding="5">
                <tr>
                    <td class="normal"  >是否同意</td>
                    <td><input class="easyui-combobox" id="agree_dlg" name="agree_dlg" required="true" style="height:25px;" editable="false" data-options=" url:'baseController/getComboBox.do?type=AGREE&isnull=false',valueField:'id',textField:'text'">
                        <input id = "id_dlg"     name="id_dlg" type="hidden" >
                        <input id = "task_def_key_dlg" name="task_def_key_dlg" type="hidden" >
                        <input id = "key_dlg" name="key_dlg" type="hidden" >
                    </td>
                    <td  class="normal" >审批意见</td>
                    <td><input id = "opinion_dlg" name="opinion_dlg" class="easyui-textbox" required="true"  style="height:25px;width:400px;" data-options="validType:['specialCharacter','checkLength[200]']" /></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div id="dlg-buttons">
    <a id="dlg-save"  href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="complete()">保存</a>
    <a id="dlg-close" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
</div>




</body>
</html>