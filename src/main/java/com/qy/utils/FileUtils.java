package com.qy.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Because of you on 2020/1/2.
 */
public class FileUtils {
    /**
     * 级联创建目录
     * @param path
     */
    public static void creatDir(String path) {
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
    }
    /**
     * 验证码图片处理
     * @param imgUrl
     * @param resUrl
     */
    public static void removeBackground(String imgUrl, String resUrl){
        //定义一个临界阈值
        int threshold = 300;
        try{
            BufferedImage img = ImageIO.read(new File(imgUrl));
            int width = img.getWidth();
            int height = img.getHeight();
            for(int i = 1;i < width;i++){
                for (int x = 0; x < width; x++){
                    for (int y = 0; y < height; y++){
                        Color color = new Color(img.getRGB(x, y));
                        //System.out.println("red:"+color.getRed()+" | green:"+color.getGreen()+" | blue:"+color.getBlue());
                        int num = color.getRed()+color.getGreen()+color.getBlue();
                        if(num >= threshold){
                            img.setRGB(x, y, Color.WHITE.getRGB());
                        }
                    }
                }
            }
            for(int i = 1;i<width;i++){
                Color color1 = new Color(img.getRGB(i, 1));
                int num1 = color1.getRed()+color1.getGreen()+color1.getBlue();
                for (int x = 0; x < width; x++)
                {
                    for (int y = 0; y < height; y++)
                    {
                        Color color = new Color(img.getRGB(x, y));

                        int num = color.getRed()+color.getGreen()+color.getBlue();
                        if(num==num1){
                            img.setRGB(x, y, Color.BLACK.getRGB());
                        }else{
                            img.setRGB(x, y, Color.WHITE.getRGB());
                        }
                    }
                }
            }
            File file = new File(resUrl);
            if (!file.exists())
            {
                File dir = file.getParentFile();
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                try
                {
                    file.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            ImageIO.write(img, "jpg", file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     * @param imgdata
     * @param filePath
     * @param filename
     */
    public static void saveImg(byte[] imgdata,String filePath,String filename) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        File dir = new File(filePath);
        try {
            if(!dir.exists()&&dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath+File.separator+filename);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(imgdata);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(bos!=null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
