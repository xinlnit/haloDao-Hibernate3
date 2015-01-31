haloDao
=======

基于hibernate的通用Dao组件实现
--------
#该通用dao层基于hql或sql语句实现动态条件查询,很容易扩展成基于其他orm或者存jdbc的Dao层,并容易在前台直接改变查询条件,
不用写一句代码,即前后台一致,并有良好的安全性及灵活性,基本可以除去Dao层的编写,将只专注业务层业务逻辑!
-------
#简述
         1.前台容易修改查询条件.该通用dao主要是像(Criteria Query)一样实现了动态条件查询,不过不同处在于,查询字段条件等全部在
         一个HaloMap的Map对象的key值中,value值为字段值,于是在前台传json对象封装到Map对象类便可查询.
         并且相比较MyBatis的动态SQL方式更容易编写和使用.
         修改前台json便可更改查询条件.
         2.免掉了判断字段是否为空的大量代码.因为为空不会查询,要查询也可直接修改key值实现
         3.批量更新,删除实现.直接生成执行的hql语句,灵活度增加,并可以根据查询条件删除修改
         4.查询第一条记录.
         5.扩展了查询条件,以适应一些常用需求.比如从某月到某月查询的实现.修改key值就可以了!
         6.基于sql的haloView可变参数视图及其在视图基础上的结果集中的动态查询实现(很拗口^_^).
         这也是基于Mybatis思想,但实现及其使用比其要好许多.也是编写jdbc版本的基础.
         虽然体系较新不太完善,但在hibernate中足以应付一些需求.
         7.结合代码生成技术,将极大减少代码编写量,并使其专注于业务逻
         8.主要代码不到700行
#不足
        1.还未编写基于jdbc版本的haloDao,不过这个好重构.但和成熟orm比较,暂时会缺少缓存.懒加载等其他特性.
        如果做成框架,那么要考虑的还有很多.
        2.最初版本是公司内部实用,不过这版本是重构版本,代码大多不同,不能兼容,可能有部分bug未发现,需要实践.
#主要功能:
##动态条件查询的实现
     
     一般情况我们不会查询值为null值或者为空值的条件.
     比如:findListByMap(new HaloMap().set("userName_eq","vonchange").set("password_eq","123").set("email_eq",null)).
     查询用户名为vonchange和password为123的结果集.
     若要查询email值为null的列 则为set("email_eq_rx",null).或set("email_eq_in",null)
 
     比如:findListByMap(new HaloMap().set("userName_like","vonchange").set("createDate_le",new Date())
     .set("email_in",new String[]{"123@vonchange.com","345@vonchange.com"}))
     查询用户名左模糊于vonchange,创建时间小于当前,邮箱在"123@vonchange.com","345@vonchange.com"中的结果集
     
     findListByMap(new HaloMap().set("userName_like","vonchange").set("(createDate_le",new Date())
      .set("|email_in)",new String[]{"123@vonchange.com","345@vonchange.com"}))
      查询用户名左模糊于vonchange,创建时间小于当前或者邮箱在"123@vonchange.com","345@vonchange.com"中的结果集
    
###(可以理解空格为_符号) 
      比如(userName_like==(userName like    |email_in)== or email in)
##按haloMap删除
     deleteByMap(new haloMap().set("userName_like","change");
     删除用户名左模糊为von的结果
##根据haloMap修改
     updateWithNotNullByHql(new BaseUser().setRole(1),new haloMap().set("userName_like","change"));
     默认不更新为null的字段
##updateWithNotNull:不更新为空值的字段
      取自网络:更新不 为null 的字段
##扩展like条件查询
     比如:findListByMap(new HaloMap().set("userName_like","change") 默认为左模糊查询.
     而userName_5like为右模糊查询 userName_5like5 为全模糊查询
##扩展和日期相关条件查询
     查询2014年12月份数据:new HaloMap().set(createDate_monthlike,'2014-12').
     查询从2012年11月到2014年12月 new HaloMap().set(createDate_monthge,'2012-11')
     .set(createDate_monthle,'2014-14').  
     其中前缀为:day(dd) 天 mounth(MM)月 year(yyyy)年 hour(HH) 小时  minute(mm) 分 second(ss) 秒
##查询第一条数据或某几条数据
        findFirstByMap(new HaloMap().set("userName_eq","change")).
        findListByMap(Map<String,?> parameter, int begin, int end):查询从begin到end条数据.
       或者 findListByMap(new HaloMap().set("userName_eq","change").set("addEnd",3).set("addBegin",1))
       (对应addEnd()和addBegin()方法)
##使用ge,gt等作为条件
        eq:=  neq:<>(!=)   ge:>=   gt:>  le:<=  lt:<  notin:not in  notIn:not in  
        not:  不等于或者值为null
        比如:new HaloMap().set(createDate_ge,'2012-11').set(createDate_le,'2014-14').
##只查询某些字段并封装到实体:addColumn(hibernate中不建议使用,haloView中可以使用)
          findListByMap(new HaloMap().set("userName_like","change").addColumn("userName_eq","passWord")
          .addColumn("email");
          查询是实体中用户,密码,邮箱字段并封装到实体中(不支持懒加载及其他特性)
##分页支持groupBy
         findPageByMap(new HaloMap().set("userName_like","change").addGroup("role"));
###可追加的addOrder(addGroup addColumn也支持)
          findPageByMap(new HaloMap().set("userName_like","change").addGroup("role").addOrder("createDate_desc")
          .addOrder("userName").addOrder("updatetime","id"));
###灵活安全实现hql拼接
           findPageByMap(new HaloMap().sets("aa","von"+"%","123@ww.com").set("userName_prm":"von"+"%");
           在 halo包下同名实体的xml中的hql定义id为aa的  写入 userName like :userName and email =:email
            (注:起个名很费脑啊!直接id为aa到zz,简单暴力)
            ---或者在实体加入halo注释标明位置比如halo(postion="user") 则xml需要放入halo.user包下
###日期可传字符
        new HaloMap().set(createDate_ge,'2012-11').set(createDate_ge,'2012年11月12日'))
        对于不支持格式可以set(createDate_ge?yy年11月,'12年11月')
##可以完全无特殊字符,用数字替换
          5==% 比如全模糊5like5==%like% (键盘对应)
          9==( 0==) (键盘对应)
          3==# #后面可明确字段类型:set("money#bigdecimal","999") (键盘对应)
           1==|(形象一) 8==.
          如果key的变量起始是数字,可以在前加入_防止命名规范问题(前台json)
          注:生成的条件包含QWRTYUIOP对应键盘上方数字
          因而数据库字段不可以含数字
########################
##基于sql的haloView可变参数视图实现(暂不完善,但足以应付一些特殊需求)
        实体命名必须Halo开头!
        首先在halo包中对应实体xml中views中view编写好sql语句:select * from base_user where role=:role ${email} ${groupBy}
        调用findListByHaloView(new HaloMap().set("role_prm",1).set("groupBy_data","aa")
        .set("email_data","ab").set("email_prm","123@ww.com").set("userName_like","von")
        .addColumn("userName_eq","password").addOrder("createDate_eq","role");
        //datas中data数据 id 为aa:group by role 
        id 为ab  and email=:email
        为:查询出角色为1,邮箱为123@ww.com,并按角色分组的结果集中查询用户名左模糊von,并按照createDate和role正序,
        并只查询出用户名及密码字段并封装到实体中
        其中拼接文件中拼接字符串使用了freemarker
##基于sql实现的haloView 中sql拼接为山寨版hql  基于骆驼命名法 遇到大写转为_小写
          sets("aa","von"+"%","123@ww.com").set("userName_prm":"von"+"%");
          在 配置中hqls中hql id 为aa:userName like :userName and email =:email
##实现原理基于hql或sql语句字符串,可直接前台传json对象构建查询语句并有良好的安全性
         addColumn("userName")==set("addColumn","userName") 
         addOrder("createdate")==set("addOrder","createdate")
         addGroup("role")==set("addGroup","role") 
         用haloMap可在set("addGroup","userName"):前台可避免重复可以addGroup1,addGroup2 

  
##完全防止sql注入
         完全防止sql注入,所以在拼接hql和sql中追加sql片断时需要写入属性文件,虽然麻烦一步,
         但在程序上已完全杜绝用户sql注入的可能.
##HaloMap
          支持链式和一些方便设置参数的方法,为迫使使用,Dao层参数未使用父类Map,而是用子类HaloMap
