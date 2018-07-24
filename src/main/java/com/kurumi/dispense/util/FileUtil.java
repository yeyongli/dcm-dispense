package com.kurumi.dispense.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	public static boolean createDir(String dir) {
		File file = new File(dir);
		if (file.exists()) {
			return false;
		}
		return file.mkdirs();
	}

	public static void writeFile(File file, String str) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(str);
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public static String readFile(File file) {
		FileReader fr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String str = "";
			
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	public static void copyFile(String source, String dest) {
		try {
			File sourceFile = new File(source);
			File destFile = new File(dest);
			Properties p = PropsUtil.loadProps("config.properties");
			//dicom文件保存的目录
			String dicomResource = p.getProperty("dicom.resource");
			
			if (sourceFile.exists()) {
				InputStream is = new FileInputStream(sourceFile);
				OutputStream os = new FileOutputStream(destFile);
				IOUtils.copy(is, os);
			} else {
				createDir("d:/dicom/log");
				String logDir = "备份的dicom文件找不到--------d:/dicom/log/not_find_dicom.txt";
				appendInfoToFile(logDir, source);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("备份dicom文件异常" + dest + "--------异常信息为:" + ExceptionUtil.getStackTrace(e));
		}
	}
	
	public static boolean copyFile1(String source, String dest) {
		InputStream is = null;
		OutputStream os = null;
		
		try {
			File sourceFile = new File(source);
			File destFile = new File(dest);
			//dicom文件保存的目录
			
			if (sourceFile.exists()) {
				 is = new FileInputStream(sourceFile);
				 os = new FileOutputStream(destFile);
				IOUtils.copy(is, os);
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}
	
	public static void appendInfoToFile(String fileName, String info) {
        File file =new File(fileName);
        FileWriter fileWriter = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fileWriter =new FileWriter(file, true);
            info =info +System.getProperty("line.separator") + System.getProperty("line.separator");
            fileWriter.write(info);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	if(null != fileWriter) {
        		try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
		}
    }

	/**
	 * 图片到byte数组
	 * 
	 * @return
	 */
	public static byte[] image2Byte(String path) {
		byte[] data = null;
		FileImageInputStream input = null;
		ByteArrayOutputStream output = null;
		
		try {
			input = new FileImageInputStream(new File(path));
		    output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			
			data = output.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	
	
}











