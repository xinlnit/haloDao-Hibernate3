/**@View
 * 
 */
function reset(){
	var data = view.get("#formCondition");
	var entity=$map({});
	var items =data.get("elements").items;
	   $.each(items,function(n,value) {
	   	    entity.put(value.get("property"),"");
	   	});
	data.set("entity",entity._map);
}
/**@View
 * 
 */
function search(){
var data = view.get("#formCondition.entity");
ht.dir(data);
with (view.get("#dataSet${entity!}")){
	set("parameter", $map(data));
	flushAsync();
  }
}

// @Bind #DataPilot.onSubControlAction
!function(arg) {
var datas = view.get("#dataSet${entity!}.data");
var actionUpdate${entity!} = view.get("#actionUpdate${entity!}");
switch(arg.code){
	case "+":{
		datas.insert();
		view.get("#dialog${entity!}").show();
		arg.processDefault=false;
		break;
	}
	case "-":{
		dorado.MessageBox.confirm("您真的想删除当前数据吗?",function(){
			datas.remove();
			actionUpdate${entity!}.execute();
		});
		arg.processDefault=false;
		break;
	}
}
};
// @Bind #ok.onClick
!function(){
	var dialog=view.get("#dialog${entity!}");
	var action=view.get("#actionUpdate${entity!}");
	dorado.MessageBox.confirm("您真的要保存吗?",function(){
		action.execute(function(){
			dialog.hide();
		});
	});
};
//@Bind #cancel.onClick
!function(){
	var data=view.get("#dataSet${entity!}").getData();
	var dialog=view.get("#dialog${entity!}");
	dorado.MessageBox.confirm("您真的要取消当前操作吗？",function(){
		data.cancel();
		dialog.hide();
	});
};
//@Bind #change.onClick
!function(){
	var data=view.get("#dataSet${entity!}.data");
	if(data.current){
		view.get("#dialog${entity!}").show();
	}else{
		dorado.MessageBox.alert("当前表没有信息可供编辑!");
	}
};
//@Bind #reset.onClick
!function(){
    view.reset();
    view.search();
};
//@Bind #find.onClick
!function(){
    view.search();
};
//@Bind #formCondition.onKeyDown
!function(arg){
   if(arg.keyCode==13){
    view.search();
    }
};
