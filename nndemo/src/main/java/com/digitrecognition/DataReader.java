package com.digitrecognition;

import java.io.*;
import java.math.BigInteger;
//import java.lang.Math;


public class DataReader {

    private byte[] data;
    private String set;
    public int[] dataVals;
    public int[][][] imgVals;

    
    public DataReader(String filePath){
        try{
            retrieveFile(filePath);
        }catch (IOException e){
            e.printStackTrace();
        }
        parseData(data);
    }

    //Retrieve data File specified in constructor 
    public void retrieveFile(String filePath) throws IOException{

        File f = new File(filePath);
        long size = f.length();
        byte[] contents = new byte[(int)size];
        FileInputStream fis = new FileInputStream(f);
        fis.read(contents);
        fis.close();
        data = contents;
   
    }

    //Interpret binary data from files and change to integers 
    public void parseData(byte[] data){
        //Find type of data (image or label) from first 4 bytes
        int type = parseInt(0, 4);

        //Find length of dataset
        int length = parseInt(4, 8);

        if (type == 2051){
            int numRows = parseInt(8, 12);
            int numCols = parseInt(12, 16);

            //Parse all of the pixel values from image data set
            int[] parsed = new int[data.length - 16];
            for (int i = 16; i < data.length; i++){
                parsed[i - 16] = parseInt(i, i + 1);
            }
            imgVals = reshapeArray(parsed, numRows, numCols);
        }
        else if (type == 2049){
            int[] parsed = new int[data.length - 8];
            for (int i = 8; i < data.length; i++){
                parsed[i - 8] = parseInt(i, i + 1);
            }
            dataVals = parsed;
        }
        if (length == 10000){
            set = "train";
        }else if (length == 60000){
            set = "test";
        }
    }


    public int parseInt(int start, int end){
        return fromByteArr(subArray(start, end));
    }

    public byte[] subArray(int start, int end){
        byte[] ret = new byte[end - start];
        int count = 0;
        for (int i = start; i < end; i++){
            ret[count] = data[i];
            count++;
        }
        return ret;
    }

    public int fromByteArr(byte[] arr){
        
        return toUnsigned(new BigInteger(arr).intValueExact());
    }

    public int toUnsigned(int val){
        if (val >= 0 && val <= 177){
            return val;
        }else if (val < 0){
            val = 256 - Math.abs(val);
        }
        return val;
    }

    //Get 3d array of all pictures and values (row-major)
    public int[][][] reshapeArray(int[] arr, int rows, int cols){
        //Get number of data points
        int numDataPoints = arr.length/(cols*rows);
        int[][][] ret = new int[numDataPoints][rows][cols];

        for (int i = 0; i < numDataPoints; i++){

            int offset = i*rows*cols;
            int[][] picture = new int[rows][cols];
            int row = 0, col = 0;
            for (int j = offset; j < (offset + rows*cols); j++){
                picture[row][col] = arr[j];
                col++;
                if (col == cols){
                    row++;
                    col = 0;
                }
            }
            ret[i] = picture;
        }
        return ret;
    }

    public String valsToString(int[][] arr){

        String ret = "";
        for (int i = 0; i < arr.length; i++){
            ret += "[";
            for (int j = 0; j < arr[0].length; j++){
                ret += arr[i][j];
                if (j != arr[0].length - 1){
                    ret +=", ";
                }
            }
            ret += "]";
            if (i != arr.length - 1){
                ret += "\n";
            }
        }
        return ret;
        
    }

    public String getType(){
        return set;
    }


}
