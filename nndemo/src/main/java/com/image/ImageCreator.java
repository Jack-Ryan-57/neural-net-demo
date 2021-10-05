package com.image;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ImageCreator{

    String URL;

    public ImageCreator(String imageURL){
        this.URL = imageURL;
    }

    public BufferedImage createImage(){
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(URL));
        }catch (IOException e){
            System.out.println("Wrong file path");
        }
        return img;
    }
}