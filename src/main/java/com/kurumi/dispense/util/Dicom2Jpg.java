package com.kurumi.dispense.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jpg2000鐨勫彧鏈塪cm4che鑳借В
 * 杩戞棤鎹熺殑鍙湁dcmtk鑳借В
 * 楂樼骇jpg鏈夋崯鍙兘鐢╠cmtk
 * 楂樼骇jpg鏃犳崯dcm4che鍜宒cmtk閮借兘瑙�
 * 
 * @author yeyongli
 *
 */
public class Dicom2Jpg {
	private static Logger log = LoggerFactory.getLogger(Dicom2Jpg.class);
	
	public static void test1() {
//		//C:\Users\h2oco2\Desktop\dcm4che-5.11.0-bin\dcm4che-5.11.0\bin
////		runbat("C:/Users/h2oco2/Desktop/dcm4che-5.11.0-bin/dcm4che-5.11.0/bin/dcm2jpg.bat", 
////				"C:/Users/h2oco2/Desktop/椤圭洰/dicom/CompressDicom/ct1_lossy.dcm",
////				"C:/Users/h2oco2/Desktop/11.jpg");
//
////		runbat("C:\\Users\\h2oco2\\Desktop\\dcmtk-3.6\\dcmtk-3.6.2-win64-dynamic\\bin\\dcmj2pnm.exe", 
////				"C:/Users/h2oco2/Desktop/椤圭洰/dicom/CompressDicom/ct1_jp2k_lossless.dcm", "+on",
////				"C:/Users/h2oco2/Desktop/11.jpg");
		//runbat("Jp2kLossLess", "D:\\dicom\\archdir\\v0000000\\A7\\8Z\\OA\\OA\\1.2.840.113619.2.55.3.2831208458.415.1327797214.572\\A78ZOAOA\\A15QHB56\\00000000\\0000MNY4\\F2Z9M6_W\\K_L91404\\R3N4W4F_", "C:/Users/h2oco2/Desktop/11.jpg");
		runbat("Jp2kLossLess", "D:/dicom/archdir/v0000000/A7/8Z/OA/OA/1.2.840.113619.2.55.3.2831208458.415.1327797214.572/A78ZOAOA/A15QHB56/00000000/0000MNY4/F2Z9M6_W/K_L91404/R3N4W4G4", "C:/Users/h2oco2/Desktop/11.jpg");
	}
	
	/**
	 * 鍘嬬缉绫诲瀷 1.楂樼骇jpg鍘嬬缉, 2.jp2000鍘嬬缉, 3.杩戞棤鎹熷帇缂� , 4.鍏朵粬鍘嬬缉鏂瑰紡
	 * islossy:1.鏈夋崯, 2.鏃犳崯
	 * @param batPath
	 * @param compressType 
	 * @param argStrings
	 */
	public static boolean runbat(String transferSyntaxUID, String sourceDir, String destDir) {
		Properties properties = PropsUtil.loadProps("config.properties");
		//String cmd = "cmd /c start " + batPath + " ";
		String dcmtkPath = properties.getProperty("dcmtk");
		String dcm4chePath = properties.getProperty("dcm4che");
		String cmd = "";
		
		if (transferSyntaxUID.equalsIgnoreCase("LSLess") || transferSyntaxUID.equalsIgnoreCase("LSLossy")) {
			//浣跨敤 3.6 l2m2pnm
			String bathPath = dcmtkPath + "dcml2pnm.exe ";
		    cmd = getCmd(bathPath, sourceDir, "+on", destDir);
		} else if (transferSyntaxUID.equalsIgnoreCase("Jp2kLossLess") || transferSyntaxUID.equalsIgnoreCase("Jp2kLossy")
				|| transferSyntaxUID.equalsIgnoreCase("Jp2kP2Lossy") || transferSyntaxUID.equals("Jp2kP2Less")) {
			//浣跨敤 dcm4che
			String bathPath = dcm4chePath + " ";
			cmd = getCmd(bathPath, sourceDir, destDir);
		} else {
			//浣跨敤 dcmj2pm 3.6
			String bathPath = dcmtkPath + "dcmj2pnm.exe ";
			cmd = getCmd(bathPath, sourceDir, "+on", destDir);
		}
		
		return getJpg(cmd, destDir);
	}

	public static boolean runbat(String transferSyntaxUID, String sourceDir, String destDir, int frameIndex) {
		Properties properties = PropsUtil.loadProps("config.properties");
		//String cmd = "cmd /c start " + batPath + " ";
		String dcmtkPath = properties.getProperty("dcmtk");
		String dcm4chePath = properties.getProperty("dcm4che");
		String cmd = "";
		
		if (transferSyntaxUID.equalsIgnoreCase("LSLess") || transferSyntaxUID.equalsIgnoreCase("LSLossy")) {
			//浣跨敤 3.6 l2m2pnm
			String bathPath = dcmtkPath + "dcml2pnm.exe ";
		    cmd = getCmd(bathPath, sourceDir, "+on", destDir);
		} else if (transferSyntaxUID.equalsIgnoreCase("Jp2kLossLess") || transferSyntaxUID.equalsIgnoreCase("Jp2kLossy")
				|| transferSyntaxUID.equalsIgnoreCase("Jp2kP2Lossy") || transferSyntaxUID.equals("Jp2kP2Less")) {
			//浣跨敤 dcm4che
			String bathPath = dcm4chePath + " --frame " + frameIndex + " ";
			cmd = getCmd(bathPath, sourceDir, destDir);
		} else {
			//浣跨敤 dcmj2pm 3.6
			String bathPath = dcmtkPath + "dcmj2pnm.exe ";
			cmd = getCmd(bathPath, sourceDir, "+on", destDir);
		}
		
		return getJpg(cmd, destDir);
	}	
	
	/**
	 * 拼接命令行参数
	 * 
	 * @param cmd
	 * @param argStrings
	 * @return
	 */
	public static String getCmd(String cmd, String...argStrings) {
		if (argStrings != null && argStrings.length > 0) {
			for (String string : argStrings) {
				cmd += string + " ";
			}
		}
		return cmd;
	}
	
	public static boolean getJpg(String cmd, String destDir) {
		try {
			 Process p = Runtime.getRuntime().exec(cmd);
			 final InputStream is = p.getInputStream();   
			
            new Thread() {  
                public void run() { 
                   InputStreamReader isr = new InputStreamReader(is);
                   BufferedReader br = new BufferedReader(isr); 
                   
                    try {  
                        String line = null;  
                        
                        while ((line = br.readLine()) != null) {  
                              if (line != null){
                           	   System.out.println(line);
                              }  
                          }  
                    } catch (IOException e) {  
                         e.printStackTrace();  
                    } finally{  
                        try {
                       	 if (is != null) {
                       		 is.close();
                       	 }
                       	 
                       	 if (isr != null) {
                       		 isr.close();
                       	 }
                       	 
                       	 if (br != null) {
                       		 br.close();
                       	 }
                        } catch (IOException e) {  
                           e.printStackTrace();  
                       }  
                     } 
                  
                    }  
                 }.start();  
                 
                 p.waitFor();  
                 p.destroy();   
                 
                 File file = new File(destDir);
                 //如果文件的大小大于8k, 说明转换成功
                 if (StringUtils.isNotBlank(destDir) && file.exists() && file.length() > (8*1024)) {
                	 return true;
                 } 
                 
                 return false;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("开一个进程dicom文件转换成图片失败------dicom----" + cmd +"---异常信息为:----" +ExceptionUtil.getStackTrace(e));
			return false;
		}
	}
	
	/**
	 * dcmdump.exe读取窗宽(0028, 1051), 窗位(0028, 1050)
	 * 
	 * @return
	 */
	public static Map<String, String> getWindowWidthAndWindowCenter(String dicomPath) {
		final Map<String, String> resultMap = new HashMap<>();
		String cmd = null;
		
		try {
			Properties properties = PropsUtil.loadProps("config.properties");
			String dcmtkPath = properties.getProperty("dcmtk");
			String bathPath = dcmtkPath + "dcmdump.exe ";
			cmd = getCmd(bathPath, dicomPath);
			Process p = Runtime.getRuntime().exec(cmd);
			final InputStream is = p.getInputStream();
			
			final String newCmd = cmd;
			new Thread() {  
               public void run() {  
            	   InputStreamReader isr = new InputStreamReader(is);
                   BufferedReader br = new BufferedReader(isr);  
                   
                   try {  
                       String line = null;  

                       while ((line = br.readLine()) != null) {
                             if (StringUtils.isNotBlank(line)){
                            	 if (line.contains(Constants.DICOM_WINDOW_WIDTH)) {
                            		 //窗宽
                            		 String ww = line.substring(line.indexOf("[")+1, line.indexOf("]"));
                            		 
                            		 if (StringUtils.isNotBlank(ww)) {
                            			 resultMap.put("ww", ww.trim());
                            		 }
                            	 } else if (line.contains(Constants.DICOM_WINDOW_CENTER)) {
                            		 //窗位
                            		 String wl = line.substring(line.indexOf("[")+1, line.indexOf("]"));
                            		 
                            		 if (StringUtils.isNotBlank(wl)) {
                            			 resultMap.put("wl", wl.trim());
                            		 }
                            	 } else if (line.contains(Constants.DICOM_BIT_STORED)) {
                            		 //位数
                            		 String bitStored = line.substring(line.indexOf("US")+3, line.indexOf("US")+6);
                            		 
                            		 if (StringUtils.isNotBlank(bitStored)) {
                            			 resultMap.put("bitStored", bitStored.trim());
                            		 }
                            	 }
                             }  
                      }  
                   } catch (IOException e) {  
                        e.printStackTrace();  
                        log.error("从进程外读取dicom的窗宽和窗位失败---" + newCmd + "----异常信息为:----" + ExceptionUtil.getStackTrace(e));
                   } finally{  
                       try {  
                           if (is != null) {
                         	  is.close();
                           }
                           
                           if (isr != null) {
                         	  isr.close();
                           }
                           
                           if (br != null) {
                         	  br.close();
                           }
                         } catch (IOException e) {  
                            e.printStackTrace();
                        }  
                      }   
                   }  
                }.start();  
                
                //进程阻塞在这里
                p.waitFor();  
                //销毁进程
                p.destroy();   
		} catch (Exception e) {
			e.printStackTrace();
			log.error("----从进程外读取dicom的窗宽和窗位失败---" + cmd + "----异常信息为:----" + ExceptionUtil.getStackTrace(e));
		}
		
		return resultMap;
	}
	
	
	
}





