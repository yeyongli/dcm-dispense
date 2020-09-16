package com.kurumi.dispense;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.junit.Test;

public class ImageTest {
	
	 public static void main(String args[]) throws IOException{
	        BufferedImage grayImage = null;
	        grayImage = imageToGray("C:\\Users\\h2oco2\\Desktop\\test.png");
	        //会保存在当前工程的目录下
	        ImageIO.write(grayImage, "png", new File("C:\\Users\\h2oco2\\Desktop\\grayImage.png"));
	    }

	    public static BufferedImage imageToGray(String imagePath){
	        BufferedImage image = null;
	        BufferedImage grayImage = null;
	        int gray = 0;
	        try {
	            File imagef = new File(imagePath);
	            image = ImageIO.read(imagef);

	            if(!imagef.exists()){
	                 System.out.println("image not found!");
	                 return grayImage;
	            }
	            int height = image.getHeight();
	            int width = image.getWidth();
	            int minX = image.getMinX();
	            int minY = image.getMinY();

	            grayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	            for(int i=minX; i<width; i++) {
	                for(int j=minY; j<height; j++) {
	                    int[] RGB = {0, 0, 0};
	                    //将24bit中存储的RGB值单个提取出来，可以理解为byte转int
	                    RGB[0] = (image.getRGB(i, j) & 0xff0000) >> 16;
	                    RGB[1] = (image.getRGB(i, j) & 0xff00) >> 8;
	                    RGB[2] = (image.getRGB(i, j) & 0xff);

	                    //这里是将RGB分别乘上一个权重，得出相应的灰度值gray
	                    gray = (30*RGB[0] + 59*RGB[1] + 11*RGB[2]) / 100;
	                    //将得出的灰度值转换成计算机中存储的模式
	                    int rgb_togray = ((gray & 0xff)<<16 ) | ((gray & 0xff)<<8) | (gray & 0xff);
	                    grayImage.setRGB(i, j, rgb_togray);
	                }
	            }
	        }catch(IOException e) {
	            e.printStackTrace();
	        }
	            return grayImage;
	    }
}
