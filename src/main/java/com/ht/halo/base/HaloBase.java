package com.ht.halo.base;


public class HaloBase extends Base{
	public static final String ADDCOLUMN = "addColumn";// 添加查询字段,默认查询出主键
	public static final String ADDORDER = "addOrder";// 添加排序
	public static final String ADDGROUP = "addGroup";// 添加排序
	public static final String ADDHQL = "addHql";// 添加查询hql片段的key值,比如fcy.user.baseUser.a(最前是文件名,放到halo.hql中)
	public static final String ADDBEGIN = "addBegin";// 开始条数
	public static final String ADDEND = "addEnd";// 结束条数
	public static final String HQL = "hql";
	public static final String PRM = "prm";// 添加查询hql中的参数标识
	public static final String SPACE = "\u0020";
	public static final char SPACECHAR = '\u0020';
	public static final String TAPESPT = "#";
	public static final String FORMATESPT = "?";
	public static final String MYSPACE = "_";//原先:改为_ 空格意义明确 并不和占位符冲突 sql使用hql方式也名正言顺
	public static final char MYSPACECHAR = '_';
	public static final String EX = "ex";
	public static final String IN = "in";// 老版就是查询 与条件in冲突
	public static final String RX = "rx";// 任性 就是查询
	public static final String CK = "ck";// check 检查值为空或者""
	public static final String TOBEAN = "toBean";
	public static final String HALOPACH = "halo";
	public static final String[] NUMS = new String[] { "1", "3", "5",  "7", "8", "9", "0" };
	public static final String[] NUMREPLACE = new String[] { "|", "#", "%",  "?", ".", "(", ")" };
	public static final String[] NUMREPLACELETTER= new String[] { "Q", "E", "T",  "U", "I", "O", "P" };
	public static final String[] PATTERN = new String[] { "yyyy", "yyyy-MM", "yyyy-MM-dd", "MM-dd", "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日", "yyyy年MM月dd日 HH:mm:ss", "yyyyMM", "yyyyMMdd", "yyyy/MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss" };


}
