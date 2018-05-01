/**
 * HangZhou department
 * AsiaInfo Corp. Ltd.
 * 2006-3-24
 * @author zengxr
 */
package com.asiainfo.cboss.tool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 */
public class StringUtil {

	// private String test = "test";
	// private String xxx = "xxx";
	// private String yyy = "yyy";

	/**
	 * 生成一指定长度的特殊字符串.
	 *
	 * @param chr
	 *            指定的字符
	 * @param length
	 *            字串长度
	 * @return
	 */
	public static String getSpecialStr(char chr, int length) {
		String tmpStr = "";
		for (int i = 0; i < length; ++i) {
			tmpStr += chr;
		}
		return tmpStr;
	}

	public static String ifNull(String nvlStr, String defStr) {
		if (nvlStr == null || nvlStr.trim().length() < 1)
			return defStr;
		else
			return nvlStr;
	}

	public static String buildString(Object obj) {

		Class clazz = obj.getClass();
		Field[] fs = clazz.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		sb.append(clazz.getName());
		sb.append("[");
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].getName().startsWith("$"))
				continue;
			if(fs[i].getType().equals(boolean.class)){
				continue;
			}
			try {
				// Method m = clazz.getMethod(getMethod(fs[i].getName()), null);
				fs[i].setAccessible(true);
				Object value = fs[i].get(obj);
				String v = "NULL";
				if (value != null)
					v = value.toString();
				sb.append(fs[i].getName());
				sb.append("=");
				sb.append(v);
				if (i < fs.length - 1) {
					sb.append(";");
				} else {
					sb.append("]");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return sb.toString();
	}

	public static String getMethod(String prop) {

		return "get" + Character.toUpperCase(prop.charAt(0))
				+ prop.substring(1);
	}

	public static void main(String[] args) {
          //System.out.println("["+StringUtil.fill("", ' ',6, false)+"]");
          String tmp = "000吴心圣00100000";
          int total = 17;
          byte[] bytes = tmp.getBytes();
          String str  = new String(bytes,3,6);
          System.out.println(str);
//          System.out.println(tmp.getBytes().length );
//          System.out.println(tmp.substring(3,10));
          splitStr("asdfsdf,dsfasf,dasfssda",",");
        }

	public static String replaceHtml(String str) {
		String tmpstr = str;
		tmpstr = replace(tmpstr, "<br>", "\n");
		tmpstr = replace(tmpstr, "&lt;", "<");
		tmpstr = replace(tmpstr, "&gt;", ">");
		tmpstr = replace(tmpstr, "&amp;", "&");
		tmpstr = replace(tmpstr, "&apos;", "\'");
		tmpstr = replace(tmpstr, "&quot;", "\"");
		tmpstr = replace(tmpstr, "&nbsp;", " ");
		return tmpstr;

	}

	// 替换字符串函数
	// String strSource - 源字符串
	// String strFrom - 要替换的子串
	// String strTo - 替换为的字符串
	public static String replace(String strSource, String strFrom, String strTo) {
		// 如果要替换的子串为空，则直接返回源串
		if (strFrom == null || strFrom.equals("")) {
			return strSource;
		}
		String strDest = "";
		// 要替换的子串长度
		int intFromLen = strFrom.length();
		int intPos;
		// 循环替换字符串
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			// 获取匹配字符串的左边子串
			strDest = strDest + strSource.substring(0, intPos);
			// 加上替换后的子串
			strDest = strDest + strTo;
			// 修改源串为匹配子串后的子串
			strSource = strSource.substring(intPos + intFromLen);
		}
		// 加上没有匹配的子串
		strDest = strDest + strSource;
		// 返回
		return strDest;
	}

	public static String rfill(String src, char fillChar, int len) {
		return fill(src, fillChar, len, true);
	}

	public static String lfill(String src, char fillChar, int len) {
		return fill(src, fillChar, len, false);
	}

	public static String fill(String src, char fillChar, int len,
			boolean isRight) {
		if (src == null || fillChar == '\0')
			return null;
		StringBuffer sb = new StringBuffer();
		if (src.getBytes().length >= len)
			return src;

		for (int i = 0; i < len - src.getBytes().length; i++) {
			sb.append(fillChar);
		}

		if (isRight)
			sb.insert(0, src);
		else
			sb.append(src);

		return sb.toString();
	}

	/**
	 * 把字符串转换成html脚本中可以认到的格式，把阿ascii码值为10的改成空格，把“"”改成“'”。
	 *
	 * @param strSource
	 *            String 原始字符串
	 * @return String 转换后的字符串
	 */
	public static String convertToHtml(String strSource) {
		if (strSource == null) {
			return "";
		}

		strSource = strSource.replace((char) 10, ' ');
		strSource = strSource.replace('"', '\'');
		return strSource;
	}

	/**
	 * 字符串经过从字符集iso-8859-1到字符集gb2312的转换，所有从字符集为
	 * iso-8859-1的系统中取得的字符串都必须经过这样的转换才能正常显示在 jsp页面上，否则中文显示会有乱码出现。
	 *
	 * @param str
	 *            String待转换的字符串
	 * @return 转换过的字符串
	 */
	public static String getCNStr(String str) {
		if (str == null) {
			return "";
		}

		try {
			return new String(str.getBytes("ISO-8859-1"), "GB2312");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 字符串经过从字符集gb2312到字符集iso-8859-1的转换，所有要传输到字符集为
	 * iso-8859-1的系统中的字符串都必须经过这样的转换才能以正常的格式传到系统中 jsp页面上，否则传输过去的中文显示会有乱码。
	 *
	 * @param str
	 *            String待转换的字符串
	 * @return 转换过的字符串
	 */
	public static String getUNCNStr(String str) {

		if (str == null) {
			return "";
		}

		try {
			return new String(str.getBytes("GB2312"), "ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
        /**
         *
         * @param str String
         * @return String
         */
        public static String getISOStr(String str) {
         if (str == null) {
                        return "";
                }

                try {
                        return new String(str.getBytes(), "ISO-8859-1");
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return "";

       }
	/**
	 * 按不同的操作系统返回不同编码的字串
	 *
	 * @param str
	 * @return
	 */
	public static String getCNStrByOS(String str) {
		if (str == null || str.trim().equals("")) {
			return "";
		}
		try {
			return new String(str.getBytes("GB2312"));
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 数据处理线程中的扩展参数提示用
	 * @param extParameters
	 * @return
	 */
	public static String specialBuildString(Map extParameters) {

		if(extParameters == null) return "NULL";
		StringBuffer sb = new StringBuffer();
		Iterator it = extParameters.keySet().iterator();
		for(;it.hasNext();){
			Object key = it.next();
			Object value = extParameters.get(key);
			sb.append(key);
			sb.append('=');
			if(value instanceof String[]){
				String[] values = (String[])value;
				for(int i = 0; i < values.length; i ++){
					sb.append(values[i]);
					if(i < values.length - 1) sb.append(',');
				}
			}else{
				sb.append(value.toString());
			}
			sb.append(';');
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	public static String buildeString(Object obj) {

		if(obj == null) return "NULL";
		Class clazz = obj.getClass();
		//Field.
		Field[] fs = clazz.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		String clazzName = clazz.getName();
		clazzName = clazzName.substring(clazzName.lastIndexOf('.') + 1);
		sb.append(clazzName);
		sb.append("[");
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].getName().startsWith("$"))
				continue;
			if(fs[i].getType().equals(boolean.class)){
				continue;
			}
			//if(fs[i].)
			try {
				// Method m = clazz.getMethod(getMethod(fs[i].getName()), null);
				fs[i].setAccessible(true);
				Object value = fs[i].get(obj);
				String v = "NULL";
				if (value != null)
					v = value.toString();
				sb.append(fs[i].getName());
				sb.append("=");
				sb.append(v);
				if (i < fs.length - 1) {
					sb.append(";");
				} else {
					sb.append("]");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return sb.toString();
	}
	
	/**
	 * 程序出错堆栈信息转换成字符串
	 * @param ex
	 * @return
	 */
	public static String stackTraceString(Throwable ex){		
		StringWriter sw = new StringWriter();		
		PrintWriter pw = new PrintWriter(sw);	
		ex.printStackTrace(pw);
		return sw.toString();		
	}
	
	
/**
 * 字符串分割
 * @param sourceStr
 * @param splitStr
 * @return
 */
	public static String[] splitStr(String sourceStr,String splitStr){
	    String objectStr[] = new String[0];
        if (sourceStr == null || sourceStr.trim().equals("")) {
            return null;
        }
        Vector vctUnit = new Vector();
        StringTokenizer token = new StringTokenizer(sourceStr, splitStr);
        while (token.hasMoreElements()) {
           // objectStr[i++] = (String) (token.nextElement());
            vctUnit.add(token.nextElement());
        }
        if (vctUnit.size() > 0) {
            objectStr = new String[vctUnit.size()];
            for (int i = 0; i < vctUnit.size(); i++)
                objectStr[i] = (String) (vctUnit.get(i));
        }
        return objectStr;
	}
	
	/**
	   * 将String值设置到StringBuffer中
	   * @param strBuf  要设置的StringBuffer
	   * @param str     要传入的值　
	   */
	  public static void setStrBufValue(StringBuffer strBuf, String str) {
	    if (strBuf == null) {
	      strBuf = new StringBuffer("");
	      return;
	    }

	    if (str == null || str.equals("")) {
	      strBuf = strBuf.delete(0, strBuf.length());
	      return;
	    }

	    strBuf.delete(0, strBuf.length()); //先清空其中的值，
	    strBuf.append(str);
	  }
	  

}



