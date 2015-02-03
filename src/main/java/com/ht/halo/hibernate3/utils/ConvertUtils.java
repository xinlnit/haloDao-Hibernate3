package com.ht.halo.hibernate3.utils;

import java.math.BigDecimal;




/**
 *  TODO 简单方式(效率高)实现类型转换,细节部分会不如ConvertUtils
 *  :ConvertUtils一次类型转换需要69ms 有点代价过高 不过多个也大概是这个时间?
 * @author fengchangyi@haitao-tech.com
 * @date 2015-1-26 下午3:27:13
 */
public class ConvertUtils extends org.apache.commons.beanutils.ConvertUtils{
        public static Integer toInteger(String value){
        	if(null==value){
        		return null;
        	}
        	value=value.trim();
        	if("null".equals(value)){
        		return null;
        	}
            Double d=	Double.parseDouble(value);
            return d.intValue();
        }
        public static Integer toInteger(Object value){
            return toInteger(String.valueOf(value));
        }
        public static String toString(Object value){
            return String.valueOf(value);
        }
        public static Long toLong(String value){
        	if(null==value){
        		return null;
        	}
        	value=value.trim();
        	if("null".equals(value)){
        		return null;
        	}
            Double d=	Double.parseDouble(value);
            return d.longValue();
        }
        public static Long toLong(Object value){
            return toLong(String.valueOf(value));
        }
        public static Boolean toBoolean(String value){
        	if(null==value){
        		return null;
        	}
        	String str=value.trim();
        	if("null".equals(str)){
        		return null;
        	}
        	if("1".equals(str)){
        		return true;
        	}
        	if("0".equals(str)){
        		return false;
        	}
            return Boolean.parseBoolean(str);
        }
        public static Boolean toBoolean(Object value){
        	return toBoolean(String.valueOf(value));
        }
        public static Double toDouble(String value){
        	if(null==value){
        		return null;
        	}
        	value=value.trim();
        	if("null".equals(value)){
        		return null;
        	}
        	return	Double.parseDouble(value);
        }
        public static Double toDouble(Object value){
            return toDouble(String.valueOf(value));
        }
        public static Float toFloat(String value){
        	if(null==value){
        		return null;
        	}
        	value=value.trim();
        	if("null".equals(value)){
        		return null;
        	}
        	return Float.parseFloat(value);
        }
        public static Float toFloat(Object value){
        	 return toFloat(String.valueOf(value));
        }
        public static Short toShort(String value){
        	if(null==value){
        		return null;
        	}
        	value=value.trim();
        	if("null".equals(value)){
        		return null;
        	}
            Double d=	Double.parseDouble(value);
            return d.shortValue();
        }
        public static Short toShort(Object value){
        	return toShort(String.valueOf(value));
        }
        public static Byte toByte(String value){
        	if(null==value){
        		return null;
        	}
        	value=value.trim();
        	if("null".equals(value)){
        		return null;
        	}
            return	Byte.parseByte(value);
        }
        public static Byte toByte(Object value){
        	return toByte(String.valueOf(value));
        }
        public static BigDecimal toBigDecimal(String value){
        	if(null==value){
        		return null;
        	}
        	value=value.trim();
        	if("null".equals(value)){
        		return null;
        	}    	
            return	new BigDecimal(value);
        }
        public static BigDecimal toBigDecimal(Object value){
        	return toBigDecimal(String.valueOf(value));
        }
        public static void main(String[] args) {
        	System.out.println(toInteger("null"));
        	System.out.println(System.currentTimeMillis());
        	System.out.println(toBoolean(null));
        	System.out.println(System.currentTimeMillis());
        	System.out.println(ConvertUtils.convert("null", Boolean.class));
        	System.out.println(System.currentTimeMillis());
        	System.out.println(toDouble("2.30E3"));       
		}
}
