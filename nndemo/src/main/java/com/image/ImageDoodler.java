package com.image;

import java.util.HashMap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

import javax.imageio.ImageIO;

import com.colortraining.ColorTrainer;

public class ImageDoodler{

    private HashMap<String, Color> colors = new HashMap<String, Color>();

    public ImageDoodler(String url, ColorTrainer ct){
        populateHashMap();
        BufferedImage b = new ImageCreator(url).createImage();
        BufferedImage pixelated = new ImagePixelator(b, 5, 5).getImg();
        BufferedImage condensed = createCondensedImage(pixelated, ct);
        ImageDoodler.saveImage(condensed, "painting-condensed.png");
    }


    //Condense Image down into new image with only the 11 specific colors
    public BufferedImage createCondensedImage(BufferedImage b, ColorTrainer ct){
        //Empty image with same dimensions as input
        BufferedImage ret = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < b.getWidth(); i++){
            for (int j = 0; j < b.getHeight(); j++){
                Color c = new Color(b.getRGB(i, j));
                Color px = searchMapFor(ct.simplifyColor(c.getRed(), c.getGreen(), c.getBlue()));
                ret.setRGB(i, j, px.getRGB());
            }
        }
        return ret;
    }

    //Match String representation of color to its corresponding Color 
    public void populateHashMap(){
        colors.put("red", Color.RED);
        colors.put("green", Color.GREEN);
        colors.put("blue", Color.BLUE);
        colors.put("white", Color.WHITE);
        colors.put("brown", new Color(165, 42, 42));
        colors.put("black", Color.BLACK);
        colors.put("orange", Color.ORANGE);
        colors.put("yellow", Color.YELLOW);
        colors.put("purple", Color.MAGENTA);
        colors.put("pink", Color.PINK);
        colors.put("gray", Color.GRAY);
    }

    //Finds respective Color class given String representation  
    private Color searchMapFor(String str){
        if (colors.containsKey(str)){
            return colors.get(str);
        }
        System.out.println("Color not found: " + str);
        return null;
    }

    //Change single stray pixels
    public static BufferedImage simplifyImage(BufferedImage b, String imgName){
        for (int i = 0; i < b.getWidth(); i++){
            for (int j = 0; j < b.getHeight(); j++){
                if ((i - 1 > -1) && (j - 1 > -1) && (i + 1 < b.getWidth()) && (j + 1 < b.getHeight())){
                    if (shouldBeChanged(b, i, j)){
                        b.setRGB(i, j, b.getRGB(i + 1, j));
                    }

                }
            }
        }
        saveImage(b, imgName);
        return b;
    }

    //Decide if pixel is stray pixel for image simplification
    public static boolean shouldBeChanged(BufferedImage b, int i, int j){
        int c = b.getRGB(i, j);
        if (c == b.getRGB(i + 1, j) || c == b.getRGB(i - 1, j) || c == b.getRGB(i, j + 1) || c == b.getRGB(i, j - 1)){
            return false;
        }
        return true;
    }


    //Save image to its designated location
    public static void saveImage(BufferedImage b, String imgName){
        try{
            File f = new File(imgName);
            ImageIO.write(b, "png", f);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}