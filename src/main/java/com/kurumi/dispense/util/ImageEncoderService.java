package com.kurumi.dispense.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片基本操作
 * @author yeyongli
 *
 */
public class ImageEncoderService {
	
		private static ImageEncoderService instance;

	    private ImageEncoderService() {
	        instance = this;
	    }

	    public synchronized static ImageEncoderService getInstance() {
	        if (instance == null) {
	            instance = new ImageEncoderService();
	        }
	        return instance;
	    }

	    /**
	     * 缩小并转换格式
	     * 
	     * @param srcPath 源路径
	     * @param destPath 目标路径
	     * @param height 目标高
	     * @param width 目标宽
	     * @param formate 文件格式
	     * @return
	     * 
	     */
	    public boolean narrowAndFormateTransfer(String srcPath, String destPath, int height, int width, String formate) {
	        boolean flag = false;
	        try {
	            File file = new File(srcPath);
	            File destFile = new File(destPath);
	            
	            if (!destFile.getParentFile().exists()) {
	                destFile.getParentFile().mkdir();
	            }
	            
	            BufferedImage src = ImageIO.read(file); // 读入文件
	            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            Graphics g = tag.getGraphics();
	            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
	            g.dispose();
	            flag = ImageIO.write(tag, formate, new FileOutputStream(destFile));// 输出到文件流
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return flag;
	    }

//	    public static void main(String[] args) {
//	        try {
//	            ImageEncoderService service = new ImageEncoderService();
//	            boolean flag = service.narrowAndFormateTransfer("C:\\Users\\h2oco2\\Desktop\\11.jpg",
//	            		"C:\\Users\\h2oco2\\Desktop\\111.png", 400, 400, "png");
//	            System.out.println(flag);
//	        } catch (Exception e) {
//	            System.out.println(e);
//	        }
//	    }

	    
}






