<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory">
    </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
$(function(){
	$("#contentCategory").tree({
		url : '/rest/content/category',
		method : "GET",
		animate: true,
		onContextMenu: function(e,node){//在右键点击节点的时候触发
            e.preventDefault();//阻止显示默认的浏览器右击菜单
            //this表示当前这颗树；将当前点击的树节点选中
            $(this).tree('select',node.target);
            //选取id为contentCategoryMenu的元素（div菜单）将其显示在当前点击的坐标
            $('#contentCategoryMenu').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        },
        onAfterEdit : function(node){//在编辑节点之后触发
        	var _tree = $(this);
        	if(node.id == 0){
        		// 新增节点
        		$.post("/rest/content/category/add",{parentId:node.parentId,name:node.text},function(data){
        			_tree.tree("update",{//将新节点的id更新为数据库中对应的id
        				target : node.target,
        				id : data.id
        			});
        		});
        	}else{
        		$.ajax({
        			   type: "POST",
        			   url: "/rest/content/category/update",
        			   data: {id:node.id,name:node.text},
        			   success: function(msg){
        				   //$.messager.alert('提示','新增商品成功!');
        			   },
        			   error: function(){
        				   $.messager.alert('提示','重命名失败!');
        			   }
        			});
        	}
        }
	});
});
function menuHandler(item){
	var tree = $("#contentCategory");
	//当前选中的节点
	var node = tree.tree("getSelected");
	if(item.name === "add"){//点击 添加
		tree.tree('append', {//追加若干子节点到一个父节点，param参数有2个属
            parent: (node?node.target:null),//新增节点的父节点为当前选中的节点
            data: [{
                text: '新建分类',
                id : 0,
                parentId : node.id
            }]
        }); 
		//在当前树上查询节点id为0的那个节点（刚刚造的新节点）
		var _node = tree.tree('find',0);
		//选中最新造的节点，并对该新节点进行编辑
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){//如果点击了 确定 则r的值为true；否则false
				$.ajax({
     			   type: "POST",
     			   url: "/rest/content/category/delete",
     			   data : {parentId:node.parentId,id:node.id},
     			   success: function(msg){
     				  tree.tree("remove",node.target);
     			   },
     			   error: function(){
     				   $.messager.alert('提示','删除失败!');
     			   }
     			});
			}
		});
	}
}
</script>