package com.nndemo;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class DemoGUI extends JFrame implements ComponentListener, ActionListener{
    
    ColorDemo g;
    static HomeScreenGraphics hsg;
    
    //Constructor creates JFrame with given title 
    public DemoGUI(String title){
        this.setTitle("Neural Network Demo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);

        setHomeScreen();
    }

    public DemoGUI(){}

    //Create Graphics and initialize neural network for demonstrating color classification
    public void setToColorDemo(){
        g = new ColorDemo();
        g.offsetX = this.getX();
        g.offsetY = this.getY() + 20;
        this.getContentPane().add(g);
        this.addComponentListener(this);
        this.setVisible(true);
    }

    //Initialize home screen 
    public void setHomeScreen(){
        hsg = new HomeScreenGraphics(this.getWidth(), this.getHeight());
        hsg.gui = this;
        this.add(hsg);
        this.setVisible(true);
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        g.offsetX = this.getX();
        g.offsetY = this.getY() + 20;
    }

    @Override
    public void componentShown(ComponentEvent e) {
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        
    }

    //Event handling for when home buttons are pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hsg.ccButton){
            setToColorDemo();
        }
        
    }

}
