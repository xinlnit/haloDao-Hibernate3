##前后台全自动动态多条件查询组件的实现
###基本条件查询

 - 等于查询:eq
```
findListByMap(new HaloMap().set("userName_eq","vonchange"));
```
 - 不等于查询:neq
```
findListByMap(new HaloMap().set("userName_neq","vonchange"));
```
 - 大于查询:gt
```
findListByMap(new HaloMap() .set("createTime_gt","2015-02-01"));
```
 - 小于查询:lt
```
findListByMap(new HaloMap().set("createTime_lt","2015-02-01"));
```
 - 大于等于:ge
```
findListByMap(new HaloMap().set("createTime_ge","2015-02-01"));
```
 - 小于等于:le    
```
findListByMap(new HaloMap().set("createTime_le","2015-02-01"));
```
 - 包含:in
```
  findListByMap(new HaloMap().set("userName_in",new String[]{"a","e"}) );
```
 - 不包含:notin
```
  findListByMap(new HaloMap().set("userName_notin",new String[]{"a","e"}) );
```
 - 不是:not
```
findListByMap(new HaloMap().set("userName_in","e")) );
//查询用户名不为e并且为NULL的结果集
```
 - 模糊查询
```
 findListByMap(new HaloMap().set("userName_like","e")) );
 //右模糊为e的用户名结果集,即默认右模糊查询--可以使用索引
 findListByMap(new HaloMap().set("userName_5like5","e")) );
 //全模糊查询(键盘5对应符号%)
 findListByMap(new HaloMap().set("userName_5like","e")) );
 //右模糊查询
```
 - 日期模糊查询
```
findListByMap(new HaloMap().set(createDate_monthlike,'2014-12') ) );
//查询2014年12月份的结果集
/**其中前缀为:day(dd) 天 month(MM)月 year(yyyy)年 hour(HH) 小时  minute(mm) 分 second(ss) 秒*/
findListByMap(new HaloMap().set(createDate_ddlike,'2014-12-18') ) );
//查询2014年12月18日的结果集
```
 - 从某个日期到某个日期
```
findListByMap( new HaloMap().set(createDate_monthge,'2014-11').set(createDate_monthle,'2015-02'));  
//查询从2014月11月到2015年02月份的结果集
/**
符合国人思维:包含2014年11月,12月及其2015年的1月和2月
如果从2015年02月到2015年02月,那么相当与2015年02月的数据集,前缀和日期模糊查询相同.
*/
```
 - 排序(可追加的addOrder)
```
  findPageByMap(new HaloMap().set("userName_like","change").addOrder("createDate_desc").addOrder("userName").addOrder("updateTime","id"));
  //查询名字右模糊change的数据集并分页,并按照createDate倒序,userName正序,updateTime正序和id正序
  /**
  如果前台或者代码使用set,要这么写:
  */
  findPageByMap(new HaloMap().set("userName_like","change").set("addOrder","createDate_desc").set("addOrder","updateTime"));
  //前台要不传addOrder对应的数组,要不就得按顺序写addOrder参数
```
 - 分组
```
 findPageByMap(new HaloMap().set("userName_like","change").addGroup("role"));
 //查询按role分组的用户名右模糊change的结构集
  /**
  这个因为这个版本依托hibernate,实现了hql可以分组,但未经考验,但hibernate中sql动态条件拼接实现可以放心使用!
  */
```
 - 查询某些字段
```
    findListByMap(new    HaloMap().set("userName_like","change").addColumn("userName_eq","passWord").addColumn("email");
 // 查询是实体中用户,密码,邮箱字段并封装到实体中
 /**
 同样不建议在hibernate下使用,hibernate下的sql分组实现可以使用.
 */
```
 - 查询第一条记录
```
findFirstByMap(new HaloMap().set("userName_eq","change"));
findListByMap(Map<String,?> parameter, int begin, int end));
//查询从begin到end条数据.
/**
不想独立个方法可以使用:
*/
findListByMap(new HaloMap().set("userName_eq","change").set("addEnd",3)
.set("addBegin",1))
//(对应HaloMap中的addEnd()和addBegin()方法)

```
 - where条件后面复杂查询需求
```
findPageByMap(new HaloMap().sets("aa_hql","von"+"%","123@ww.com").set("userName_prm":"von"+"%");
/**
在 halo包下同名实体的xml中的hql标签定义id为aa的值:userName like :userName and email =:email
 (注:起个名很费脑啊!直接id为aa到zz,简单暴力(>-<))
*/
//同名xml文件可以放到halo包下任意子包下,但不允许重名!   
/**
该片段hql放到xml中,便于管理,且完全防止了sql注入的可能.当然牺牲了部分便利,麻烦些.
*/   
       
```
 - 站位符传参:prm
```
.set("userName_prm":"von"+"%");
//18中的给hql片段站位符复制
```
 - 检查必须存在值:ck
```
 findListByMap(new HaloMap().set(userName_eq_ck,null));
 //比如前台传了空值,但逻辑上是不允许空的,这是程序会报错,意思你如果必须,则需要进行判空效验
 
```

 - 日期可以传字符
```
 new HaloMap().set(createDate_ge,'2012-11').set(createDate_ge,'2012年11月12日'))
  // 支持的格式可以统一在halo.config包下的halo.xml中配置
  //对于不支持格式也可以set(createDate_ge?yy年11月,'12年11月')
```
 - 自动根据实体类型类型转换 
```
findListByMap(new HaloMap().set("money","17.8")
.set("flag",1);
//money为BigDeciaml而flag为Boolean型
```
 
 - 可以将下划线_理解成空格
###以上基本满足了where条件后面的查询需求
&emsp;&emsp;同时你也发现了,map并没有采用接口类,而是自己写的实现类 意思是你必须的,确定的以及肯定的,搭配这个haloMap才行.因为只有俩者相结合才可以练就绝世武功啊!
       
###HaloMap的使用
-  链式调用 
```
new HaloMap().sets("aa_hql","von"+"%","123@ww.com").set("userName_prm":"von"+"%").set("addGroup","userName").set("id",1);
```
 - 可以点出方法
```
HaloMap().addOrder("createTime_desc").addColumn("role","userName").addColumn("id").addGroup("role");
//里面提供浅显易懂的方法,让你不会哪里点哪里,so easy!
```
###删除更新
&emsp;&emsp;同时因为删除更新操作除了where语句前不同 其他可以重用 很容易的编写出根据halomap更新和删除方法
 - 删除
```
 deleteByMap(new haloMap().set("userName_like","change");
 //删除用户名左模糊为von的结果
```
 - 修改
```
updateWithNotNullByHql(new BaseUser().setRole(1),new haloMap().set("userName_like","change"));
 //默认不更新为null的字段
```
###泛型Dao和Service接口

> 1.提供泛型Dao和Service接口,使Dao层继承,基本可以或者不用再写到Dao层代码,并且基本Service层的CRUD也可以免掉!
> 2.同时配合代码生成,基本只要关心逻辑就可以了!

##面向sql动态视图的构思及其实现
 &emsp;&emsp;简单的说他的理念是:
 - **你可以再该sql视图基础上,再继续进行,全自动多条件动态查询.**
&emsp;&emsp;并且:

> 支持传入占位符参数.
> 支持视图内动态拼接.使用freemarker轻松实现该功能.
> 视图依赖和重用.支持视图了内直接引用某视图,也就是要支持子视图.(还未实现);

```
	List<HaloTest> haloTests=	haloTestDao.findListByHaloView(new HaloMap().set("state_prm", 1).set("houseId_data", "ab").set("houseName_like", "6").addView("a").addOrder("houseName_desc")); System.out.println(GsonUtils.getGsonIn().toJson(haloTests));
	 /**
	 对应实体HaloTest,sql视图放在xml中views/view/id='a'中,
	 freemarker数据是在datas/data/id="ab"中,参数状态为1,在改结果集基础上,查询房间名有模糊6的并且按房间名倒序排序的结果集
	 */
	  //其中sql为:
	 	  select crd.receivable_detail_id,crd.house_name  from charge_receivable_detail crd
        where crd.house_id='${houseId}' and state=:state
 
```
 &emsp;&emsp;同时提供了封装到Map对象的方法,不过这个Map叫HaloViewMap,提供了getInteger等方法,方便将结果类型转换成你想要的.
 

```
	List<HaloViewMap> haloTests=	haloViewMapDao.findListByHaloView(new HaloMap()
		 .set("houseId_data", "ab")
		 .ADDXML("HaloTest")
		.set("houseName_like", "6")
		.set("aa_hql", "6"+"%")
		.addOrder("houseName_desc")
		);		 System.out.println(GsonUtils.format(GsonUtils.getGsonIn().toJson(haloTests)));
		//使用HaloTest.xml下的默认view中sql进行查询
		/**aa_hql使用hqls/hql/id="aa"的数据进行拼接,不够这个不能使用直接使用sql,要使用山寨hql---->只是基于命名规范,将大写字母比如A转为_a,而你要记住的就是要将sql变成山寨hql
		*/
```