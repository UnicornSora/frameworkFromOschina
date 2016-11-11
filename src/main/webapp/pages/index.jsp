<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.common.security.vo.LoginUserVO"%>
<%@ taglib uri="/mytaglib" prefix="cc" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

LoginUserVO vo = (LoginUserVO)session.getAttribute("LOGINUSER");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    

    <cc:basic path="<%=path %>"/>

    <link rel="stylesheet" type="text/css" href="css/index.css" />

	<script type="text/javascript">
	$(function(){
	    $.ajaxSetup({
	        complete:function(XMLHttpRequest,textStatus){   
        	    var sessionstatus=XMLHttpRequest.getResponseHeader('sessionstatus'); //通过XMLHttpRequest取得响应头,sessionstatus， 
                if(sessionstatus=='timeout'){  
                    alert('登陆已经超时，请重新登陆！'); 
                    //跳转到登陆页面
        		    location.href='<%=path %>/pages/login.jsp';
        	   }   
            }   
	    })
	})
	</script>	
	<script type="text/javascript">
	 	$(function(){	
	 	    $('#asyncTree').tree({
	            onClick: function(node){
		            var isleaf =  $('#asyncTree').tree('isLeaf', node.target);
		            if(isleaf){//判断是否是叶子
                        //判断页面是否存在
                        if($('#tabs').tabs('exists', node.text)){
                            $('#tabs').tabs('select', node.text);//选中并刷新
		                    var currTab = $('#tabs').tabs('getSelected');
		                    var url = $(currTab.panel('options').content).attr('src');
		                    //url存在，并且不是首页
		                    if(url != undefined && currTab.panel('options').title != '首页'){
			                    $('#tabs').tabs('update',{
				                    tab:currTab,
				                    options:{content:createFrame(url)}
			                    });
		                    }
	                    }else{
		                    var content = createFrame(node.attributes.url);
		                    $('#tabs').tabs('add',{
			                    title:node.text,
			                    content:content,
			                    closable:true
		                    });
	                    }
	                    tabClose();
		            }  
	            }
            });
            //根据url生成iframe
            function createFrame(url) {
	            var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	            return s;
            }
	 	    function tabClose() {
	        /*双击关闭TAB选项卡*/
	            $(".tabs-inner").dblclick(function(){
		            var subtitle = $(this).children(".tabs-closable").text();
		            $('#tabs').tabs('close',subtitle);
	            });
	        /*为选项卡绑定右键*/
	            $(".tabs-inner").bind('contextmenu',function(e){
		            $('#mm').menu('show', { left: e.pageX,top: e.pageY });
		            var subtitle =$(this).children(".tabs-closable").text();
		            $('#mm').data("currtab",subtitle);
		            $('#tabs').tabs('select',subtitle);
		            return false;
	            });
            }
            //绑定右键菜单事件
            function tabCloseEven() {
	            //刷新
	            $('#mm-tabupdate').click(function(){
		            var currTab = $('#tabs').tabs('getSelected');
		            var url = $(currTab.panel('options').content).attr('src');
		            if(url != undefined && currTab.panel('options').title != '首页') {
		            	$('#tabs').tabs('update',{
		            		tab:currTab,
		            		options:{ content:createFrame(url) }
			            });
		            }
	            });
		        //关闭当前
		        $('#mm-tabclose').click(function(){
		            var currtab_title = $('#mm').data("currtab");
		            $('#tabs').tabs('close',currtab_title);
		        });
	            //全部关闭
		        $('#mm-tabcloseall').click(function(){
		        	$('.tabs-inner span').each(function(i,n){
		        		var t = $(n).text();
		        		if(t != '首页') {
		        			$('#tabs').tabs('close',t);
		        		}
		        	});
		        });
		        //关闭除当前之外的TAB
		        $('#mm-tabcloseother').click(function(){
		        	var prevall = $('.tabs-selected').prevAll();
		        	var nextall = $('.tabs-selected').nextAll();		
		        	if(prevall.length>0){
		        		prevall.each(function(i,n){
		        			var t=$('a:eq(0) span',$(n)).text();
			        		if(t != '首页') {
		        				$('#tabs').tabs('close',t);
			        		}
			        	});
			        }
			        if(nextall.length>0) {
			        	nextall.each(function(i,n){
				        	var t=$('a:eq(0) span',$(n)).text();
				        	if(t != '首页') {
				        		$('#tabs').tabs('close',t);
			        		}
				        });
			        }
			        return false;
		        });
	        }
            tabCloseEven();
	 	});
	 	//打开修改密码页面
        function openupdate(){
            $('#dlg').dialog('open').dialog('setTitle','修改密码');
            $('#fm').form('clear');
        }

        //修改密码
        function updatepassword(){
            $('#fm').form('submit',{
                url: 'loginUserController/updatePassWord.do',
                onSubmit: function(){
                    //判断新密码是否一致
                    if($('#password_new1').val()!=$('#password_new2').val()){
                        $.messager.show({
                            title: '系统消息',
                            msg: '新密码与确认新密码不一致！'
                        });
                        return false ;
                    }
                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#dlg').dialog('close');
                        $.messager.show({
                            title: '系统消息',
                            msg: result.msg
                        });
                    }else{
                        $.messager.show({
                            title: '系统消息',
                            msg: result.msg
                        });
                    }
                }
            });
        }
        
        //退出系统
        function logout(){
		    $.messager.confirm('提示', '你确定要退出吗？', function(r) {
				if (r) {
					window.location.href='<%=path %>/loginUserController/logout.do';
				}
			});
		}
	</script>
    
    
  </head>
  
  <body class="easyui-layout">  
	<div data-options="region:'north',split:true,border:true" style="height:100px;"class="cs-north">
		<div class="head_a" >
			<div class="head_aa"> </div>
			<div class="head_ab" >
                <div class="head_aba">
                    用户名：<%=vo.getUsername()%>
                    角色：<%=vo.getRolename()%>
                    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-edit'" style="width:80px" onclick="openupdate()">修改密码</a>
                    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-back'" style="width:80px" onclick="logout()">退出</a>
                </div>
                <div class="head_abb"></div>
			</div>
		</div>
	</div>
	
    <div data-options="region:'west',title:'导航',split:true" style="width:200px;">
    
    <ul id="asyncTree" class="easyui-tree" data-options="url:'loginUserController/getUserTree.do'"></ul> 
    
    </div>  
    
    <div id="mainPanle" data-options="region:'center',border:true,border:false " >
		<div id="tabs" class="easyui-tabs"  fit="true" border="false" >
            <div title="首页">
				<div >
					制作：football98<br>
				</div>
			</div>
        </div>
    </div> 
    
    <div data-options="region:'south',split:true,border:false" style="height:25px;background:#EAEEF5;"><center>技术支持:football98(QQ:109844680)</center></div>
    
    <div id="mm" class="easyui-menu cs-tab-menu">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseother">关闭其他</div>
		<div id="mm-tabcloseall">关闭全部</div>
	</div>

    <div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px" data-options="resizable:true,modal:true" closed="true" buttons="#dlg-buttons">
        <form id="fm" method="post" >
            <table>
                <tr>
                    <td class="normal" width="200px">原密码</td>
                    <td><input id="password_old"  name="password_old" type="password" class="easyui-textbox"  required="true"  style="height:25px;" data-options="validType:['password','length[6,20]']" ></td>
                </tr>
                <tr>
                    <td class="normal" width="200px">新密码</td>
                    <td><input id="password_new1"  name="password_new1" type="password"  class="easyui-textbox"  required="true"  style="height:25px;"  data-options="validType:['password','length[6,20]']" ></td>
                </tr>
                <tr>
                    <td class="normal" width="200px">确认新密码</td>
                    <td><input id="password_new2"  name="password_new2" type="password"  class="easyui-textbox"  required="true"  style="height:25px;"  data-options="validType:['password','length[6,20]']" ></td>
                </tr>
            </table>
        </form>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="updatepassword()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
    </div>
    
  </body>  
</html>
