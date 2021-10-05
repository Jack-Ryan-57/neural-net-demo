package com.image;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class ImagePixelator {
    
    BufferedImage input, output;

    public ImagePixelator(BufferedImage b, int scaleX, int scaleY){
        input = b;
        output = pixelateImage(scaleX, scaleY);
        saveImage(output, "painting-pixelated.png");
    }

    public BufferedImage pixelateImage(int scaleX, int scaleY){
        BufferedImage ret = new BufferedImage(input.getWidth()/scaleX, input.getHeight()/scaleY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < input.getWidth()/scaleX; i++){
            for (int j = 0; j < input.getHeight()/scaleY; j++){
                int startX = i*scaleX, startY = j*scaleY;
                int endX = startX + scaleX, endY = startY + scaleY;
                Color c = averagePixelVal(startX, startY, endX, endY);
                ret.setRGB(i, j, c.getRGB());
            }
        }
        return ret;
    }

    public Color averagePixelVal(int startX, int startY, int endX, int endY){
        int red = 0, green = 0, blue = 0;
        for (int i = startX; i < endX; i++){
            for (int j = startY; j < endY; j++){
                Color c = new Color(input.getRGB(i, j));
                int r = c.getRed(), g = c.getGreen(), b = c.getBlue();
                red += r;
                green += g;
                blue += b;
            }
        }
        int total = (endX - startX)*(endY - startY);
        return new Color(red/total, green/total, blue/total);
    }

    public void saveImage(BufferedImage b, String imgName){
        ImageDoodler.saveImage(b, imgName);
    }

    public BufferedImage getImg(){
        return output;
    }

}
