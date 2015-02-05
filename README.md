haloDao
=======

前台及其后台全自动动态多条件查询实现
--------
##$该通用dao层基于hql或sql语句实现动态条件查询,并且自动拼接!很容易扩展成基于其他orm或者存jdbc的Dao层,并容易在前台直接改变查询条件,
不用写一句代码,即前后台一致,并有良好的安全性及灵活性,基本可以除去Dao层的编写,将只专注业务层业务逻辑!
##设计原则
        最最最极致的懒 这懒不是复制黏贴 而是比她更高一层次的境界 连这两动作 都懒得做
        no think only use  尽可能少思考 少记忆 只是傻瓜般的使用 这也是懒得的表现
        简单不过度设计
        不为万分之一需求 而实现 而是要寻求曲线救国
##------------第一部分:基于hibernate的dao层组件----------
##动态条件查询预览  
     一般情况我们不会查询值为null值或者为空值的条件.
     比如:findListByMap(new HaloMap().set("userName_eq","change").set("password_eq","123").set("email_eq",null)).
     查询用户名为vonchange和password为123的结果集.
     若要查询email值为null的列 则为set("email_eq_rx",null).或set("email_eq_in",null)

     比如:findListByMap(new HaloMap().set("userName_like","vonchange").set("createDate_le",new Date())
     .set("email_in",new String[]{"123@vonchange.com","345@vonchange.com"}))
     查询用户名左模糊于vonchange,创建时间小于当前,邮箱在"123@vonchange.com","345@vonchange.com"中的结果集
     
     findListByMap(new HaloMap().set("userName_like","vonchange").set("(createDate_le",new Date())
      .set("|email_in)",new String[]{"123@change.com","345@vonchange.com"}))
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
##分页:findPageByMap
##---------------------------查询部分-----------------------------------
##基本查询
        1.等于查询:eq     findListByMap(new HaloMap().set("userName_eq","vonchange").
        2.不等于查询:neq    .set("userName_neq","vonchange")
        3.大于查询:gt     .set("createTime_gt","2015-02-01")
        4.小于查询:lt      .set("createTime_lt","2015-02-01")
        5.大于等于:ge      .set("createTime_ge","2015-02-01")
        6.大于等于:le      .set("createTime_le","2015-02-01")
        7.in                    .set("userName_in",new String[]{"a","e"}) 
        8.notin               .set("userName_notin",new String[]{"a","e"}) 
        9.不是:not           .set("userName_in","e")  用户名不为e且或者为空的结果集
        10.模糊查询:like  .set("userName_like","e") 默认右模糊查询
        11.全模糊及其左模糊查询:  .set("userName_5like5","e") 全模糊查询 .set("userName_5like","e") 左模糊查询
        12.日期模糊查询   .set(createDate_monthlike,'2014-12') 查询2014年12月份数据
##扩展和日期相关条件查询
     查询从2012年11月到2014年12月 new HaloMap().set(createDate_monthge,'2012-11')
     .set(createDate_monthle,'2014-14').  
     其中前缀为:day(dd) 天 mounth(MM)月 year(yyyy)年 hour(HH) 小时  minute(mm) 分 second(ss) 秒
##查询第一条数据或某几条数据
        findFirstByMap(new HaloMap().set("userName_eq","change")).
        findListByMap(Map<String,?> parameter, int begin, int end):查询从begin到end条数据.
       或者 findListByMap(new HaloMap().set("userName_eq","change").set("addEnd",3).set("addBegin",1))
       (对应addEnd()和addBegin()方法)
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
        支持的格式可以统一在halo.config包下的hao.xml中配置
        对于不支持格式可以set(createDate_ge?yy年11月,'12年11月')
##可以完全无特殊字符,用数字替换
          5等于% 比如全模糊5like5等于%like% (键盘对应)
          9等于(     0==) (键盘对应)
          3等于# #后面可明确字段类型:set("money#bigdecimal","999") (键盘对应)
           1等于|(形象一) 8等于.
          如果key的变量起始是数字,可以在前加入_防止命名规范问题(前台json)
          注:生成的条件包含QWRTYUIOP对应键盘上方数字
          因而数据库字段不可以含数字
##实现原理基于hql或sql语句字符串,且简单的键值对,前台很容易传键值对象构建查询语句并有良好的安全性
         addColumn("userName")==set("addColumn","userName") 
         addOrder("createdate")==set("addOrder","createdate")
         addGroup("role")==set("addGroup","role") 
         用haloMap可在set("addGroup","userName"):前台可避免重复可以addGroup1,addGroup2 
##完全防止sql注入
         完全防止sql注入,所以在拼接hql和sql中追加sql片断时需要写入xml,虽然麻烦一步,
         但在程序上已完全杜绝用户sql注入的可能.
##HaloMap
          支持链式和一些方便设置参数的方法,为迫使其使用,Dao层参数未使用Map接口实现,而是用子类HaloMap
##------------第二部分:基于Halo视图方式的sql动态拼接实现
          部分功能未完善,暂不提供说明,主要特性可以很好的解决复杂查询,复杂报表等需求
##--------------第三部分:基于HaloDao的泛型dao及Service层及其代码生成
          这部分需要分离,使用在测试包下gen,配置在junit组件包jdbc属性文件下,未来进一步需要做成可视化的

         
