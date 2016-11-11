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

            $.post('personalController/get.do', function(result) {
                $('#fm').form('load', {
                    username : result.username,
                    phone : result.phone
                });
            }, 'json');
        });

        //保存
        function save(){
            $('#fm').form('submit',{
                url: "personalController/update.do",
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
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
                }
            });
        }
    </script>
</head>

<body>

<div id="toolbar-nav">
    <div class="easyui-panel" style="padding:5px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-save'" style="width:80px"  onclick="save()">保存</a>
    </div>
</div>

<div style="margin:10px 0;"></div>

<div id="dlg" class="easyui-panel" title="用户信息" style="height:500px;"  >
    <form id="fm" method="post" >
        <table cellpadding="5">
            <tr>
                <td class="normal" style="width:200px;">用户名</td>
                <td><input id="username" name="username" style="width:200px;height:25px;" class="easyui-textbox" required="true" data-options="validType:['chinese','checkLength[50]']" ></td>
            </tr>
            <tr>
                <td class="normal" style="width:200px;">联系电话</td>
                <td><input id="phone" name="phone" style="width:200px;height:25px;" class="easyui-textbox" required="true" data-options="validType:['number','length[11,11]']"  ></td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>

