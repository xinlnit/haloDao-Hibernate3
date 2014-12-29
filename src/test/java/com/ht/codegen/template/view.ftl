<?xml version="1.0" encoding="UTF-8"?>
<ViewConfig>
  <Arguments/>
  <Context/>
  <Model>
    <DataType name="${entity!}">
      <Property name="creationType">${entity_path!}.${entity!}</Property> 
      [#list bean.declaredFields as field]
      [#if field.modifiers==2]
      <PropertyDef name="${field.name!}">
			 				<Property name="dataType">[@typeShort str=field.type.name][/@typeShort]</Property>
			 			 		<Property name="label">[@fieldInfo field=field.name entity=entity_full ][/@fieldInfo]</Property>
			 			 		</PropertyDef>
	  [/#if]
      [/#list]
    	    </DataType>
  </Model>
  <View>
    <Property name="title">${module!}管理</Property>
    <DataSet id="dataSet${entity!}">
      <Property name="dataProvider">${entity_low}Action#find${entity!}ByPage</Property>
      <Property name="pageSize">20</Property>
      <Property name="dataType">[${entity!}]</Property>
    </DataSet>
    <UpdateAction id="actionUpdate${entity!}">
      <Property name="successMessage">保存成功!</Property>
      <Property name="executingMessage">正在保存...</Property>
      <Property name="dataResolver">${entity_low!}Action#cud${entity!}s</Property>
      <UpdateItem>
        <Property name="dataSet">dataSet${entity!}</Property>
      </UpdateItem>
    </UpdateAction>
    <ToolBar>
      <DataPilot id="DataPilot">
        <Property name="dataSet">dataSet${entity!}</Property>
        <Property name="itemCodes">pages,+,-</Property>
      </DataPilot>
      <ToolBarButton id="change">
        <Property name="caption">修改</Property>
        <Property name="icon">url(>skin>common/icons.gif) -200px -0px</Property>
      </ToolBarButton>
       <ToolBarButton id="reset">
        <Property name="caption">重置查询</Property>
        <Property name="icon">url(>skin>common/icons.gif) -80px -100px</Property>
      </ToolBarButton>
      <ToolBarButton id="find">
        <Property name="caption">查询</Property>
        <Property name="icon">url(>skin>common/icons.gif) -20px -40px</Property>
      </ToolBarButton>
    </ToolBar>
    <AutoForm id="formCondition">
      <Property name="cols">*,*,*</Property>
          </AutoForm>
    <DataGrid>
      <Property name="dataSet">dataSet${entity!}</Property>
      <Property name="dataType">${entity!}</Property>
      <Property name="readOnly">true</Property>
       [#list bean.declaredFields as field]
      [#if field.modifiers==2]
             <DataColumn name="${field.name!}">
        <Property name="property">${field.name!}</Property>
         <Property name="align">right</Property>
      </DataColumn>
        [/#if]
      [/#list]
           </DataGrid>
    <Dialog id="dialog${entity!}">
      <Property name="center">true</Property>
      <Property name="width">600</Property>
      <Property name="height">400</Property>
      <Property name="modal">true</Property>
      <Property name="closeable">false</Property>
      <Property name="caption">${module!}信息</Property>
      <Buttons>
        <Button id="ok">
          <Property name="caption">确定</Property>
          <Property name="icon">url(>skin>common/icons.gif) -140px -20px</Property>
        </Button>
        <Button  id="cancel">
          <Property name="caption">取消</Property>
          <Property name="icon">url(>skin>common/icons.gif) -40px -0px</Property>
        </Button>
      </Buttons>
      <Children>
        <AutoForm>
          <Property name="dataSet">dataSet${entity!}</Property>
          <Property name="cols">*,*</Property>
            <Property name="labelAlign">right</Property>
           [#list bean.declaredFields as field]
      [#if field.modifiers==2]
	       	      <AutoFormElement name="${field.name!}">
            <Property name="name">${field.name!}</Property>
            <Property name="property">${field.name!}</Property>
            <Editor/>
	      </AutoFormElement>
	        [/#if]
      [/#list]
	               </AutoForm>
      </Children>
      <Tools/>
    </Dialog>
  </View>
</ViewConfig>