package com.colortraining;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUIGraphics extends JPanel implements ActionListener{
    
    ColorTrainingGUI frame;
    private boolean grayScale = false;
    Color backgroundColor = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
    ArrayList<JButton> colorButtons = new ArrayList<JButton>();
    String[] colors = {"Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Black", "White", "Brown", "Gray"};
    WriteToFile fileWriter;

    public GUIGraphics(){
        for (int i = 0; i < colors.length; i++){
            JButton button = new JButton(colors[i]);
            colorButtons.add(button);
            button.addActionListener(this);
            this.add(button, BorderLayout.SOUTH);
        }
        
        fileWriter = new WriteToFile("colortrainingdata.txt");
    }

    public GUIGraphics(String title){
        for (int i = 0; i < colors.length; i++){
            JButton button = new JButton(colors[i]);
            colorButtons.add(button);
            button.addActionListener(this);
            this.add(button, BorderLayout.SOUTH);
        }
        fileWriter = new WriteToFile(title);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //Graphics2D g2d = (Graphics2D)g;
        this.setBackground(backgroundColor);
    }

    public void setFrame(ColorTrainingGUI frame){
        this.frame = frame;
    }

    public void setBackgroundColor(Color c){
        this.backgroundColor = c;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        for (int i = 0; i < colorButtons.size(); i++){
            if (colorButtons.get(i) == obj){
                String r = Integer.toString(backgroundColor.getRed());
                String g = Integer.toString(backgroundColor.getGreen());
                String b = Integer.toString(backgroundColor.getBlue());
                System.out.println(r);
                System.out.println(g);
                System.out.println(b);
                fileWriter.write(r + "\n", true);
                fileWriter.write(g + "\n", true);
                fileWriter.write(b + "\n", true);
                fileWriter.write(colorButtons.get(i).getText() + "\n--------\n", true);
                changeBackgroundColor();
            }
        }
    }
    
    public void changeBackgroundColor(){
        int r = (int)(Math.random()*255);
        int g = (int)(Math.random()*255);
        int b = (int)(Math.random()*255);
        Color c = new Color(r, g, b);
        if (grayScale){
            c = new Color(r, r, r);
        }
        this.setBackgroundColor(c);
        this.setBackground(c);
    }

    public void setGrayScaleTraining(boolean grayscale){
        this.grayScale = grayscale;
    }

}
