<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/../easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/easyui/css/demo.css">
    <script type="text/javascript" src="${pageContext.request.contextPath }/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/easyui/js/validateExtends.js"></script>
    <script src="${pageContext.request.contextPath }/js/md5.js"></script>
    <script src="${pageContext.request.contextPath }/js/datagrid-export.js"></script>
    <script type="text/javascript">
        $(function () {
            var table;

            $("#dataList").datagrid({
                title:'用户列表',
                iconCls:'icon-more',//图标
                border: true,
                collapsible:false,//是否可折叠的
                fit: true,//设置大小以适应它的父容器
                method: "post",
                url:"get_list?t="+new Date().getTime(),
                idField:'id',
                singleSelect:false,//是否单选
                loadMsg:'数据加载中，请稍后...',
                pagination:true,//分页控件
                rownumbers:true,//行号
                sortName:'id',//定义那列可以排序
                sortOrder:'DESC',//排列顺序
                remoteSort: false,
                columns: [[
                    {field:'chk',checkbox: true,width:50},
                    {field:'id',title:'ID',width:50, sortable: true, hidden: true},
                    {field:'username',title:'用户名',width:150, sortable: true},
                    {field:'password',title:'密码',width:300},
                ]],
                toolbar: "#toolbar"
            });

            //设置分页控件
            var p = $('#dataList').datagrid('getPager');

            $(p).pagination({
                pageSize: 10,//每页显示的记录条数，默认为10
                pageList: [10,20,30,50,100],//可以设置每页记录条数的列表
                beforePageText: '第',//页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
            });

            $('#search-btn').click(function () {
                $('#dataList').datagrid('reload', {
                    username: $("#search-username").val()
                });
            });
            
            $('#add').click(function () {
                table = $("#addTable");
                $("#addDialog").dialog("open");
            });

            $('#update').click(function () {
                table = $("#updateTable");
                var selectRows = $("#dataList").datagrid("getSelections");
                if(selectRows.length != 1){
                    $.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
                } else{
                    $("#updateDialog").dialog("open");
                }
            });

            //删除
            $("#delete").click(function(){
                var selectRows = $("#dataList").datagrid("getSelections");
                var selectLength = selectRows.length;
                if(selectLength == 0){
                    $.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
                } else{
                    var ids = [];
                    $(selectRows).each(function(i, row){
                        ids[i] = row.id;
                    });
                    $.messager.confirm("消息提醒", "将删除与用户相关的所有数据，确认继续？", function(r){
                        if(r){
                            $.ajax({
                                type: "post",
                                url: "delete",
                                data: {ids: ids},
                                dataType:'json',
                                success: function(data){
                                    if(data.type == "success"){
                                        $.messager.alert("消息提醒","删除成功!","info");
                                        //刷新表格
                                        $("#dataList").datagrid("reload");
                                        $("#dataList").datagrid("uncheckAll");
                                    } else{
                                        $.messager.alert("消息提醒",data.msg,"warning");
                                        return;
                                    }
                                }
                            });
                        }
                    });
                }
            });

            $("#download").click(function () {
                exportExcel();
            });


            //设置添加窗口
            $("#addDialog").dialog({
                title: "添加用户",
                width: 350,
                height: 200,
                iconCls: "icon-add",
                resizable: true,
                modal: true,
                draggable: true,
                closed: true,
                buttons: [
                    {
                        text:'添加',
                        plain: true,
                        iconCls:'icon-user_add',
                        handler:function () {
                            var validate = $("#addForm").form("validate");
                            if(!validate){
                                $.messager.alert("消息提醒","请检查你输入的数据!","warning");
                                return;
                            }else {
                                var data = $("#addForm").serialize();
                                $.ajax({
                                    type: "post",
                                    url: "add",
                                    data: {
                                        username : $("#add_username").val(),
                                        password : hex_md5($("#add_password").val()),
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if(data.type=="success"){
                                            $.messager.alert("消息提醒","添加成功!","info");
                                            //关闭窗口
                                            $("#addDialog").dialog("close");
                                            //清空原表格数据
                                            $("#add_username").textbox('setValue', "");
                                            $("#add_password").textbox('setValue', "");
                                            //重新刷新页面数据
                                            $('#dataList').datagrid("reload");
                                        }else {
                                            $.messager.alert("消息提醒",data.msg,"warning");
                                            return;
                                        }
                                    }
                                });

                            }
                        }
                    }
                ],
                onClose: function(){
                    $("#add_username").textbox('setValue', "");
                    $("#add_password").textbox('setValue', "");
                }
            });

            //设置修改窗口
            $("#updateDialog").dialog({
                title: "修改用户信息",
                width: 350,
                height: 200,
                iconCls: "icon-edit",
                modal: true,
                draggable: true,
                closed: true,
                buttons: [
                    {
                        text:'提交',
                        plain: true,
                        iconCls:'icon-edit',
                        handler:function(){
                            var validate = $("#updateForm").form("validate");
                            if(!validate){
                                $.messager.alert("消息提醒","请检查你输入的数据!","warning");
                                return;
                            }else {
//                                var data = $("#updateForm").serialize();
                                $.ajax({
                                    type: "post",
                                    url: "update",
                                    data: {
                                        id : $("#update-id").val(),
                                        username : $("#update_username").val(),
                                        password : hex_md5($("#update_password").val()),
                                    },
                                    dataType:'json',
                                    success: function(data){
                                        if(data.type == "success"){
                                            $.messager.alert("消息提醒","修改成功!","info");
                                            //关闭窗口
                                            $("#updateDialog").dialog("close");

                                            //重新刷新页面数据
                                            $('#dataList').datagrid("reload");
                                            $('#dataList').datagrid("uncheckAll");

                                        } else{
                                            $.messager.alert("消息提醒",data.msg,"warning");
                                            return;
                                        }
                                    }
                                });
                            }
                        }
                    }
                ],
                onBeforeOpen: function(){
                    var selectRow = $("#dataList").datagrid("getSelected");
                    //设置值
                    $("#update-id").val(selectRow.id);
                    $("#update_username").textbox('setValue', selectRow.username);
                    $("#update_password").textbox('setValue', selectRow.password);
                }
            });

        });

        function exportExcel() {
//            $('#dataList').datagrid('toExcel','dg.xls');
            location.href="/user/getexcle";
        }
        
    </script>
</head>

<body>
    <!-- 数据列表 -->
    <table id="dataList" cellspacing="0" cellpadding="0">

    </table>

    <!-- 工具栏 -->
    <div id="toolbar">
        <div style="float: left;">
            <a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
        </div>
        <div style="float: left;" class="datagrid-btn-separator"></div>
        <div style="float: left;">
            <a id="update" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
        </div>
        <div style="float: left;" class="datagrid-btn-separator"></div>
        <div style="float: left;">
            <a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a>
        </div>
        <div style="float: left;" class="datagrid-btn-separator"></div>
        <div style="float: left;">
            用户名：<input id="search-username" class="easyui-textbox" />
            <a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
        </div>
        <div style="float: left;" class="datagrid-btn-separator"></div>
        <div >
            <a id="download" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true">导出</a>
        </div>
    </div>

    <!-- 添加窗口 -->
    <div id="addDialog" style="padding: 10px;">
        <form id="addForm" method="post">
            <table id="addTable" cellpadding="8">
                <tr >
                    <td>用户名:</td>
                    <td>
                        <input id="add_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请填写用户名'"  />
                    </td>
                </tr>
                <tr>
                    <td>密码:</td>
                    <td><input id="add_password" style="width: 200px; height: 30px;" class="easyui-textbox" type="password" name="password" data-options="required:true, missingMessage:'请填写密码'"  /></td>
                </tr>
            </table>
        </form>
    </div>

    <!-- 修改窗口 -->
    <div id="updateDialog" style="padding: 10px">
        <form id="updateForm" method="post">
            <input type="hidden" name="id" id="update-id">
            <table id="updateTable" border=0 cellpadding="8" >
                <tr >
                    <td>用户名:</td>
                    <td>
                        <input id="update_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请填写用户名'"  />
                    </td>
                </tr>
                <tr>
                    <td>密码:</td>
                    <td><input id="update_password" style="width: 200px; height: 30px;" class="easyui-textbox" type="password" name="password" data-options="required:true, missingMessage:'请填写密码'"  /></td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
