package com.colortraining;

import java.io.*;

public class WriteToFile {
    
    String name;

    public WriteToFile(String name){
        this.name = name;
        try{
            File f = new File(name);
            if (f.createNewFile()){
                System.out.println("File created: " + f.getName());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void write(String text, boolean append){
        try{
            FileWriter writer = new FileWriter(name, append);
            writer.write(text);
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
