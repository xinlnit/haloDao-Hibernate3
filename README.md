haloDao
=======

基于hibernate的通用Dao实现
--------
该通用dao层基于hql和sql实现,很容易扩展基于其他orm或者jdb
-------
##主要实现了:
##1.动态hql的实现
     一般我们不会查询值为null值或者为空值的条件.<br/>
     比如:findListByMap(new HaloMap().set("userName","vonchange").set("password","123").set("email",null)).
     查询用户名为vonchange和password为123的结果集.若要查询email为null 则为set("email:=:in",null).
     比如:findListByMap(new HaloMap().set("userName:like","vonchange").set("createDate:<=",new Date())
     .set("email:in",new String[]{"123@vonchange.com","345@vonchange.com"}))
     查询用户名左模糊于vonchange,创建时间小于当前,邮箱在"123@vonchange.com","345@vonchange.com"中的结果集
##2.扩展like条件查询
     比如:findListByMap(new HaloMap().set("userName:like","vonchange") 默认为左模糊查询.
     而userName:5like为右模糊查询 userName:5like5 为全模糊查询
##3.扩展和日期相关条件查询
     查询2014年12月份:new HaloMap().set(createDate:monthlike,'2014-12').
     查询从2012年11月到2014年12月 new HaloMap().set(createDate:monthge,'2012-11').set(createDate:monthle,'2014-14').
##4.查询第一条数据
        findFirstByMap(new HaloMap().set("userName","vonchange")).
        findListByMap(Map<String,?> parameter, int begin, int end):查询从begin到end条数据.
##5.可使用ge,gt等作为条件
        可以: new HaloMap().set(createDate:month>=,'2012-11').set(createDate:month<=,'2014-14').
        也可以:new HaloMap().set(createDate:monthge,'2012-11').set(createDate:monthle,'2014-14').
##6.只查询某些字段并封装到实体
          findListByMap(new HaloMap().set("userName:like","vonchange").addColumn("userName","passWord")
          .addColumn("email");
          查询是实体中用户,密码,邮箱字段并封装到实体中(不支持懒加载)
##7.分页也支持groupBy
         findPageByMap(new HaloMap().set("userName:like","vonchange").addGroup("role"));
##8.hql其他
###order by
          findPageByMap(new HaloMap().set("userName:like","vonchange").addGroup("role").addOrderDesc("createDate")
          .addOrder("userName"));
###灵活单不安全接口 addHql
           findPageByMap(new HaloMap().addHql(" and userName like :userName").set("userName:prm":"von"+"%");
###日期可传字符
        new HaloMap().set(createDate:ge,'2012-11').set(createDate:ge,'2012年11月12日'))
        对于不支持格式可以set(createDate:ge?yy年11月,'12年11月')
##9.基于sql的haloView可变参数视图实现
        首先在halo.view包中在ViewTest编写好sql语句:select * from base_user where role=:role ${email} ${groupBy}
        调用findListByHaloView("ViewTest",new HaloMap().set("role:prm",1).set("groupBy:data"," group by role")
        .set("email:data"," and email=:eamil ").set("email:prm","123@ww.com").set("userName:like","von")
        .addColumn("userName","password").addOrder("createDate","role");
        为:查询出角色为1,邮箱为123@ww.com,并按角色分组的结果集中查询用户名左模糊von,并按照createDate和role正序,
        并只查询出用户名及密码字段并封装到实体中
#其他说明
##1.无特殊字符化
   5==% 比如全模糊5like5==%like% (键盘对应)
   9==( 0==) (键盘对应)
   3==# #后面可明确字段类型:set("money#bigdecimal","999") (键盘对应)
   6==:    1==|(形象一) 8==.
   注:生成的条件包含QWRTYUIOP对应键盘上方数字
##2.基于sql实现的haloView 中addHql及haloView传的sql片段无数据库字段
   比如:addHql(" and userName =:userName") set("aa:data","and userName =:userName")
     
