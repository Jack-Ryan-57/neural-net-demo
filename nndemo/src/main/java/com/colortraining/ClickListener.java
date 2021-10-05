package com.colortraining;

import java.awt.event.MouseEvent;
//import java.awt.Color;
import javax.swing.event.MouseInputAdapter;


public class ClickListener extends MouseInputAdapter{

    ColorTrainingGUI gui;
    GUIGraphics graphics;

    public void mouseClicked(MouseEvent e){
       //changeBackgroundColor();
    }

    public void setFrame(ColorTrainingGUI gui){
        this.gui = gui;
    }

    public void setGraphics(GUIGraphics g){
        this.graphics = g;
    }



}
