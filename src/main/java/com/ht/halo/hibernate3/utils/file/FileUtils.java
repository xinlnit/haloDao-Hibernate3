package com.ht.halo.hibernate3.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: FileUtils
 * @Description: TODO 文件操作类
 * @author lindapeng
 * @date 2014-4-26 上午11:08:42
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
	private static final String ENCODING = "UTF-8";

	private static String getFilePath(String packageName, String name) {
		String path = packageName.replaceAll("\\.", "/");
		return  path + "/" + name;//"/" +
	}

	public static File getClassPath(String packageName, String name) {
		
		String path =Thread.currentThread().getContextClassLoader().getResource("").getPath();
		try {
			path=URLDecoder.decode(path,ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File file = new File(path+getFilePath(packageName, name));
		return file;
	}
   public static void main(String[] args) {
	 String path=  Thread.currentThread().getContextClassLoader().getResource("").getPath();
	   System.out.println(path);
   }
	public static String writeToString(File file) {
		String str = null;
		try {
			str = readFileToString(file, ENCODING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * @Title: filterOutFile
	 * @Description: TODO 过滤输出文件:如果没有路径文件夹 创建之
	 * @param outFile
	 * @return
	 */
	public static File newOutFile(File outFile) {
		return createFolder(outFile);
	}

	public static File newOutFile(String outfilePath) {
		return createFolder(new File(outfilePath));
	}

	public static File createFolder(File targetFile) {
		String path = targetFile.getAbsolutePath();
		String folder = StringUtils.substringBefore(path, targetFile.getName());
		File folderFile = new File(folder);
		if (!folderFile.exists()) {
			folderFile.mkdirs();
		}
		return targetFile;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param folderPath
	 */
	public static void newFolder(String folderPath) {
		try {
			File folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdir();
			}
		} catch (Exception e) {
			System.out.println("创建文件夹操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 创建并获得路径
	 * 
	 * @param folderPath
	 * @return
	 */
	public static String getFolderPath(String folderPath) {
		File folder = null;
		try {
			folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
		} catch (Exception e) {
			System.out.println("创建文件夹操作出错");
			e.printStackTrace();
		}
		return folder.getAbsolutePath();
	}

	/**
	 * @description 读取文本文件的内容
	 * @param curfile
	 *            文本文件路径
	 * @return 返回文件内容
	 */
	public static String readFile(String curfile) {
		File f = new File(curfile);
		try {
			if (!f.exists())
				throw new Exception();
			FileReader cf = new FileReader(curfile);
			BufferedReader is = new BufferedReader(cf);
			StringBuffer filecontent = new StringBuffer("");
			String str = is.readLine();
			while (str != null) {
				filecontent.append(str);
				str = is.readLine();
				if (str != null)
					filecontent.append("\n");
			}
			is.close();
			cf.close();
			return filecontent.toString();
		} catch (Exception e) {
			System.err.println("不能读属性文件: " + curfile + " \n" + e.getMessage());
			return "";
		}
	}

	/**
	 * @description 取指定文件的扩展名
	 * @param filePathName
	 *            文件路径
	 * @return 扩展名
	 */
	public static String getFileExt(String filePathName) {
		int pos = 0;
		pos = filePathName.lastIndexOf('.');
		if (pos != -1)
			return filePathName.substring(pos + 1, filePathName.length());
		else
			return "";
	}

	/**
	 * @description 读取文件大小
	 * @param filename
	 *            指定文件路径
	 * @return 文件大小
	 */
	public static int getFileSize(String filename) {
		try {
			File fl = new File(filename);
			int length = (int) fl.length();
			return length;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @Description:生成新的文件名
	 * @param @param fileName
	 * @param @return
	 * @return String
	 * @throws BussinessException
	 * @see see_to_target
	 */
	public static String newFileName(String fileName) {
		String extName = null;
		// 获取扩展名
		if (fileName.lastIndexOf(".") >= 0) {
			extName = fileName.substring(fileName.lastIndexOf("."));
		}

		return StringUtils.remove(UUID.randomUUID().toString(), '-') + extName;// e
	}

	/**
	 * @Description:判断文件是否存在
	 * @param @param fileName 文件名
	 * @param @return
	 * @return boolean
	 * @throws BussinessException
	 * @see see_to_target
	 */
	public static boolean existFile(String fileName) {
		File file = new File(fileName);
		if (file.exists())
			return true;
		else
			return false;
	}

}
