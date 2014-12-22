haloDao
=======

基于hibernate的通用Dao实现
--------
该通用dao层基于hql和sql实现,很容易扩展基于其他orm或者jdb
-------
#主要实现了:
##1.动态hql的实现
   一般我们不会查询值为null值或者为空值的条件.<br/>
   比如:findListByMap(new HaloMap().set("userName","vonchange").set("password","123").set("email",null)).<br/>
   查询用户名为vonchange和password为123的结果集.若要查询email为null 则为set("email:=:in",null).<br/>
##2.扩展like条件查询
   比如:findListByMap(new HaloMap().set("userName:like","vonchange") 默认为左模糊查询.<br/>
   而userName:5like为右模糊查询 userName:5like5 为全模糊查询<br/>
##3.扩展和日期相关条件查询
     查询2014年12月份:new HaloMap().set(createDate:monthlike,'2014-12').<br/>
     查询从2012年11月到2014年12月 new HaloMap().set(createDate:monthge,'2012-11').set(createDate:monthge,'2014-14').<br/>
##4.查询第一条数据
        findFirstByMap(new HaloMap().set("userName","vonchange")).<br/>
        findListByMap(Map<String,?> parameter, int begin, int end):查询从begin到end条数据.<br/>
##5.可使用ge,gt等条件
        可以: new   HaloMap().set(createDate:month>=,'2012-11').set(createDate:month<=,'2014-14').<br/>
        也可以new HaloMap().set(createDate:monthge,'2012-11').set(createDate:monthge,'2014-14').<br/>
   
