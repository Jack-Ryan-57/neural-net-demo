package com.nndemo;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.image.ImageCreator;
import com.neuralnetwork.NeuralNetwork;

import java.awt.*;
import java.awt.image.BufferedImage;


public class HomeScreenGraphics extends JPanel implements ActionListener{

    private static int buttonW = 300, buttonH = 50;
    private int w, h;
    public JButton ccButton, drButton;
    public DemoGUI gui;
    private NetworkVisualizer nv;
    private NeuralNetwork net;

    //Initialize JPanel 
    public HomeScreenGraphics(int w, int h){
        this.setSize(w, h);
        this.w = w;
        this.h = h;
        net = new NeuralNetwork(new float[]{1f, 1f, 1f}, new int[]{12, 12}, 11);
        nv = new NetworkVisualizer(net, this);
    }

    //Paint method for Home Screen JPanel
    public void paintComponent(Graphics g){
        //Initialize graphics 
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        //Title font 
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.setColor(Color.WHITE);
        //Create background image 
        BufferedImage img = new ImageCreator("nndemo/assets/img/greengradient.jpg").createImage();
        g.drawImage(img, 0, 0, w + 1, h + 1, null);
        //Draw title 
        int titleWidth = g2.getFontMetrics().stringWidth("Jack Ryan's Neural Network");
        g2.drawString("Jack Ryan's Neural Network", w/2 - titleWidth/2, (int)(h*0.2));

        //Draw decorative Network Visualizer
        nv.setBounds(150, 160, 800, 250);
        nv.setBorder(false);
        nv.drawNetwork(g);
        

        //Create buttons on screen 
        ccButton = createHomeButton("Color Classifier", w/2, (int)(h*0.6));
        ccButton.addActionListener(this);
        //drButton = createHomeButton("Digit Reader", w/2, (int)(h*0.6 + 70));
        this.add(ccButton);
        //this.add(drButton);
    }

    //Generic method for generating home screen buttons 
    public JButton createHomeButton(String title, int locX, int locY){
        JButton b = new JButton(title);
        b.setSize(buttonW, buttonH);
        b.setLocation(locX - buttonW/2, locY);
        b.setFocusPainted(false);
        return b;
    }

    //Button event listeners 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ccButton){
            net.clearNetwork();
            gui.getContentPane().removeAll();
            gui.setToColorDemo();
            gui.setVisible(true);
        }
        
    }

}