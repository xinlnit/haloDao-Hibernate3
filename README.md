haloDao
=======

基于hibernate的通用Dao组件实现
--------
#该通用dao层基于hql和sql语句实现,很容易扩展成基于其他orm或者jdbc的Dao层,并容易在前台直接改变查询条件,
  并有良好的安全性及灵活性,基本可以除去Dao层的编写,将只专注业务层业务逻辑!
-------
#简述
         1.前台容易修改查询条件.该通用dao主要是像(Criteria Query)一样实现了动态条件查询,不过不同处在于,查询字段条件等全部在
         一个HaloMap的Map对象的key值中,value值为字段值,于是在前台传json对象封装到Map对象类便可查询.
         修改前台json便可更改查询条件.
         2.免掉了判断字段是否为空的大量代码.因为为空不会查询,要查询也可直接修改key值实现
         3.批量更新,删除实现.直接生成执行的hql语句,灵活度增加,并可以根据查询条件删除修改
         4.查询第一条记录.
         5.扩展了查询条件,以适应一些常用需求.比如从某月到某月查询的实现.修改key值就可以了!
         6.基于sql的haloView可变参数视图实现.虽然体系不太完善,但注意应付一些特殊需求.
         比如使用数据库视图想要部分sql片段可以根据需求存在与否(这种在hibernate中可以用存储过程,但太麻烦,直接拼接sql,
         维护性不好,尤其sql过多时)
         7.结合代码生成技术,将极大减少代码编写量,并使其专注于业务逻辑
#主要功能:
##动态条件查询的实现
     
     一般我们不会查询值为null值或者为空值的条件.
     比如:findListByMap(new HaloMap().set("userName","vonchange").set("password","123").set("email",null)).
     查询用户名为vonchange和password为123的结果集.若要查询email为null 则为set("email:eq:in",null).
     
     比如:findListByMap(new HaloMap().set("userName:like","vonchange").set("createDate:le",new Date())
     .set("email:in",new String[]{"123@vonchange.com","345@vonchange.com"}))
     查询用户名左模糊于vonchange,创建时间小于当前,邮箱在"123@vonchange.com","345@vonchange.com"中的结果集
     
     findListByMap(new HaloMap().set("userName:like","vonchange").set("(createDate:le",new Date())
      .set("|email:in)",new String[]{"123@vonchange.com","345@vonchange.com"}))
      查询用户名左模糊于vonchange,创建时间小于当前或者邮箱在"123@vonchange.com","345@vonchange.com"中的结果集
    
###(可以理解空格为:号) 
      比如(userName:like==(userName like    |email:in)== or email in)
##按haloMap删除
     deleteByMap(new haloMap().set("userName:like","von");
     删除用户名左模糊为von的结果
##根据haloMap修改
     updateWithNotNullByHql(new BaseUser().setRole(1),new haloMap().set("userName:like","von"));
     默认不更新为null的字段
##updateWithNotNull:不更新为空值的字段
      取自网络:更新不 为null 的字段
##扩展like条件查询
     比如:findListByMap(new HaloMap().set("userName:like","vonchange") 默认为左模糊查询.
     而userName:5like为右模糊查询 userName:5like5 为全模糊查询
##扩展和日期相关条件查询
     查询2014年12月份数据:new HaloMap().set(createDate:monthlike,'2014-12').
     查询从2012年11月到2014年12月 new HaloMap().set(createDate:monthge,'2012-11').set(createDate:monthle,'2014-14').  
     其中前缀为:day(dd) 天 mounth(MM)月 year(yyyy)年 hour(HH) 小时  minute(mm) 分 second(ss) 秒
##查询第一条数据
        findFirstByMap(new HaloMap().set("userName","vonchange")).
        findListByMap(Map<String,?> parameter, int begin, int end):查询从begin到end条数据.
##可使用ge,gt等作为条件
        可以: new HaloMap().set(createDate:month>=,'2012-11').set(createDate:month<=,'2014-14').
        也可以:new HaloMap().set(createDate:monthge,'2012-11').set(createDate:monthle,'2014-14').
##只查询某些字段并封装到实体:addColumn(hibernate中不建议使用,haloView中可以使用)
          findListByMap(new HaloMap().set("userName:like","vonchange").addColumn("userName","passWord")
          .addColumn("email");
          查询是实体中用户,密码,邮箱字段并封装到实体中(不支持懒加载)
##分页支持groupBy
         findPageByMap(new HaloMap().set("userName:like","vonchange").addGroup("role"));
###order by
          findPageByMap(new HaloMap().set("userName:like","vonchange").addGroup("role").addOrderDesc("createDate")
          .addOrder("userName"));
###灵活安全实现hql拼接
           findPageByMap(new HaloMap().sets("fcy.user.BaseUser.aa:hql","von"+"%","123@ww.com").set("userName:prm":"von"+"%");
           在 halo.hql中 fcy.properties中 编写user.BaseUser.aa=userName like :userName and email =:email
           默认拼接and (%s) 中%s的 hql,key为文件名+属性key名,推荐 模块名+实体名+aa到zz (注:起个名很费脑啊!所以简单暴力下)
###日期可传字符
        new HaloMap().set(createDate:ge,'2012-11').set(createDate:ge,'2012年11月12日'))
        对于不支持格式可以set(createDate:ge?yy年11月,'12年11月')
##基于sql的haloView可变参数视图实现(暂不完善,但足以应付一些特殊需求)
        在hibernate中优先是视图创建,特别需要动态sql拼接中才可使用
        首先在halo.view包中在ViewTest编写好sql语句:select * from base_user where role=:role ${email} ${groupBy}
        调用findListByHaloView("ViewTest",new HaloMap().set("role:prm",1).set("groupBy:data","fcy.user.BaseUser.aa")
        .set("email:data","fcy.user.BaseUser.bb").set("email:prm","123@ww.com").set("userName:like","von")
        .addColumn("userName","password").addOrder("createDate","role");
        //halo.data文件夹内fcy.properties 中 user.BaseUser.aa=group by role user.BaseUser.ab=and email=:email
        @@如果要传非sql数据要:前加data:比如data.user.BaseUser.aa 如果是要传非String类型,可直接设置
        为:查询出角色为1,邮箱为123@ww.com,并按角色分组的结果集中查询用户名左模糊von,并按照createDate和role正序,
        并只查询出用户名及密码字段并封装到实体中
        其中拼接文件中拼接字符串使用了freemarker
##基于sql实现的haloView 中sql拼接为山寨版hql  基于骆驼命名法 遇到大写转为_小写
          sets("fcy.user.BaseUser.aa:hql","von"+"%","123@ww.com").set("userName:prm":"von"+"%");
          在 halo.hql中 fcy.properties中一样 为user.BaseUser.aa=userName like :userName and email =:email
##实现原理基于hql或sql语句字符串,可直接前台传json对象构建查询语句并有良好的安全性
         addColumn("userName")==set("addColumn","userName") 
         addOrder("createdate")==set("addOrder","createdate")
         addGroup("role")==set("addGroup","role") 
         用haloMap可在set("addGroup","userName"):前台可避免重复可以addGroup1,addGroup2 
##可以完全无特殊字符,用数字替换
          5==% 比如全模糊5like5==%like% (键盘对应)
          9==( 0==) (键盘对应)
          3==# #后面可明确字段类型:set("money#bigdecimal","999") (键盘对应)
          6==:    1==|(形象一) 8==.
          如果key的变量起始是数字,可以在前加入_防止命名规范问题(前台json)
          注:生成的条件包含QWRTYUIOP对应键盘上方数字
          因而数据库字段不可以含数字
##基于haloView的视图要写对应实体(未来可以结合代码生成技术生成)
          需要加入@Entity和@Id注解,其他不需,并且会根据实体字段类型强制类型转化
          ,避免了封装到map值转化问题
##完全防止sql注入
         完全防止sql注入,所以在拼接hql和sql中追加sql片断时需要写入属性文件,虽然麻烦一步,
         但在程序上已完全杜绝用户sql注入的可能.
