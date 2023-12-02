package com.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

import javax.imageio.ImageIO;

public class ImageOutliner {
    
    private int scale = 10;
    private int w, h;
    private BufferedImage img, expanded;

    public ImageOutliner(BufferedImage b){
        this.img = b;
        this.w = b.getWidth(); 
        this.h = b.getHeight();
        BufferedImage expanded = expandImage(img, scale);
        this.expanded = expanded;
        traceImage(this.img, this.w, this.h);
        saveImage(expanded, "painting-traced.png");
    }

    public void traceImage(BufferedImage b, int w, int h){
        for (int i = 0; i < w; i++){
            for (int j = 0; j < h; j++){
                traceSinglePixel(b, i, j, w, h);
            }
        }
    }

    public void traceSinglePixel(BufferedImage b, int x, int y, int w, int h){
        //Get pixel color of input (compressed) image
        int pxColor = b.getRGB(x, y);
        //Convert "compressed" pixel to corresponding "expanded"
        int startX = x*scale, startY = y*scale;

        //"Empty" expanded pixel by making it completely white
        for (int i = 0; i < scale; i++){
            for (int j = 0; j < scale; j++){
                expanded.setRGB(startX + i, startY + j, Color.WHITE.getRGB());
            }
        }
        //Make sure we don't get index errors 
        if (x < w - 2){
            int rightCol = b.getRGB(x + 1, y);
            if (pxColor != rightCol){
                //Add black line to right side of expanded "pixel"
                int drawX =  startX + scale;
                for (int i = 0; i < scale; i++){
                    expanded.setRGB(drawX, startY + i, Color.BLACK.getRGB());
                }
            }
        }
        if (y < h - 2){
            int bottomCol = b.getRGB(x, y + 1);
            if (pxColor != bottomCol){
                //Add black line to bottom of expanded "pixel"
                int drawY = startY + scale;
                for (int i = 0; i < scale; i++){
                    expanded.setRGB(startX + i, drawY, Color.BLACK.getRGB());
                }
            }
        }
        if (x > 0){
            int leftCol = b.getRGB(x - 1, y);
            if (pxColor != leftCol){
                //Add black line to left side of expanded "pixel"
                for (int i = 0; i < scale; i++){
                    expanded.setRGB(startX, startY + i, Color.BLACK.getRGB());
                }
            }
        }
        if (y > 0){
            int topCol = b.getRGB(x, y - 1);
            if (pxColor != topCol){
                //Add black line to top of expanded "pixel"
                for (int i = 0; i < scale; i++){
                    expanded.setRGB(startX + i, startY, Color.BLACK.getRGB());
                }
            }
        }

    }


    //Make image larger to allow for tracing
    public BufferedImage expandImage(BufferedImage b, int scale){
        BufferedImage ret = new BufferedImage(b.getWidth()*scale, b.getHeight()*scale, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < b.getWidth(); i++){
            for (int j = 0; j < b.getHeight(); j++){
                int c = b.getRGB(i, j);
                for (int k = 0; k < scale; k++){
                    for (int l = 0; l < scale; l++){
                        ret.setRGB(i*scale + k, j*scale + l, c);
                    }
                }
            }
        }
        return ret;
    }

    //Save image to File
    public void saveImage(BufferedImage b, String imgName){
        try{
            File f = new File(imgName);
            ImageIO.write(b, "png", f);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
