package com.colortraining;

import javax.swing.*;

public class ColorTrainingGUI extends JFrame {
    
    GUIGraphics graphics;

    private boolean grayScaleTraining = false;

    private int width = 700, height = 500;

    public ColorTrainingGUI(String title, String dataPath, boolean grayScale){
        this.grayScaleTraining = grayScale;
        graphics = new GUIGraphics(dataPath);
        if (grayScaleTraining){
            graphics.setGrayScaleTraining(true);
        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.add(graphics);
        graphics.setFrame(this);
        ClickListener listener = new ClickListener();
        listener.setFrame(this);
        listener.setGraphics(graphics);
        this.getContentPane().addMouseListener(listener);
        this.setVisible(true);
        this.setTitle(title);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void setGrayScaleTraining(boolean grayscale){
        this.grayScaleTraining = grayscale;
    }


}
