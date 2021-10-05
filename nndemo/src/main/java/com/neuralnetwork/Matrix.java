package com.neuralnetwork;

import java.lang.Math;

public class Matrix {
    
    private int rows, cols;
    private float[][] matrix;

    //Create Matrix with 2d float array, specifying number of rows and columns
    public Matrix(int rows, int cols, float[][] matrix){
        this.rows = rows;
        this.cols = cols;
        this.matrix = matrix;
    }

    //Initial matrix constructor where Matrix is initialized with random weights
    public Matrix(int rows, int cols, boolean randomize){
        this.rows = rows;
        this.cols = cols;
        matrix = new float[rows][cols];
        if (randomize){
            randomizeMatrix(-1, 1);
        }
    }

    //Populates matrix array in current instance with contents of parameter array
    public void populateMatrix(float[][] floats){
        this.matrix = floats;
    }

    //Returns a 1-dimensional version of 2-d matrix
    public static float[] matrixToArray(Matrix m){
        int current = 0;
        float[] vals = new float[m.matrix.length*m.matrix[0].length];
        for (int i = 0; i < m.matrix.length; i++){
            for (int j = 0;  j < m.matrix[0].length; j++){
                vals[current] = m.matrix[i][j];
                current++;
            }
        }
        return vals;
    }

    //Fill matrix with random values 
    public void randomizeMatrix(int min, int max){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                matrix[i][j] = random(min, max);
            }
        }
    }

    //Transpose function
    public void transpose(){
        float[][] temp = new float[cols][rows];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                temp[j][i] = matrix[i][j];
            }
        }
        matrix = new float[cols][rows];
        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows; j++){
                matrix[i][j] = temp[i][j];
            }
        }
        int coltemp = cols;
        cols = rows;
        rows = coltemp;
    }

    //Returns new transposed Matrix object
    public static Matrix transpose(Matrix m){
        float[][] temp = new float[m.cols][m.rows];
        for (int i = 0; i < m.rows; i++){
            for (int j = 0; j < m.cols; j++){
                temp[j][i] = m.matrix[i][j];
            }
        }
        return new Matrix(m.cols, m.rows, temp);
    }

    //Scale matrix up or down by parameter value
    public void scaleMatrixBy(float scale){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                matrix[i][j] *= scale;
            }
        }
    }

    //Add value of parameter to every number in matrix
    public void add(int num){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                matrix[i][j] += num;
            }
        }
    }

    //Add another matrix to current matrix, changes data of current matrix
    //May want to consider adding static method to return new Matrix
    public void addMatrix(Matrix m){
        if(!(this.isCompatibleForAdditionWith(m))){
            System.out.println("The two matrices are not compatible for addition");
            return;
        }else{
            for (int i = 0; i < rows; i++){
                for (int j = 0; j < cols; j++){
                    matrix[i][j] += m.matrix[i][j];
                }
            }
        }
    }

    //Change 1-d float array to 2-d float array
    public static float[][] inputsToMatrix(float[] inputs){
        float[][] realInputs = new float[inputs.length][1];
        for (int i = 0; i < inputs.length; i++){
            realInputs[i][0] = inputs[i];
        }
        return realInputs;
    }

    //Subtract second Matrix parameter from first
    public static Matrix subtractMatrices(Matrix m, Matrix n){
        if (m.isCompatibleForAdditionWith(n)){
            float[][] matrix = new float[m.rows][m.cols];
            for (int i = 0; i < m.rows; i++){
                for (int j = 0; j < m.cols; j++){
                    matrix[i][j] = m.getMatrix()[i][j] - n.getMatrix()[i][j];
                }
            }
            return new Matrix(m.rows, m.cols, matrix);
        }else{
            System.out.println("The two matrices are not compatible for addition.");
            return null;
        }
    }

    //ACTIVATION FUNCTION


    //ReLU activation function (rectified linear unit)
    public void reLU(){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                float x = matrix[i][j];
                matrix[i][j] = Math.max(0, x);
            }
        }
    }

    //Multiply two matrices together
    public static Matrix matrixProduct(Matrix n, Matrix m){
        Matrix ret = null;
        if (n.isCompatibleWith(m)){
            float[][] mat = new float[n.rows][m.cols];
            for (int k = 0; k < n.rows; k++){
                for (int l = 0; l < m.cols; l++){
                    float num = 0;
                    for (int i = 0; i < n.cols; i++){
                        num += n.matrix[k][i]*m.matrix[i][l];
                    }

                    mat[k][l] = num;
                }
            }
            return new Matrix(n.rows, m.cols, mat);
        }else{
            System.out.println("Matrices not compatible");
            System.out.println("[" + n.rows + ", " + n.cols + "]");
            return ret;
        }
    }

    //Hardamard Matrix Multiplication
    public static Matrix hadamardProduct(Matrix m, Matrix n){
        int rows = m.getMatrix().length, cols = m.getMatrix()[0].length;
        Matrix ret = new Matrix(rows, cols, new float[rows][cols]);
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                ret.matrix[i][j] = m.getMatrix()[i][j] * n.getMatrix()[i][j];
            }
        }
        return ret;
    }

    //Format matrix in String to be printed (readable version for debugging, etc.)
    public String toString(){
        String ret = "[";
        for (int i = 0; i < rows; i++){
            ret += "[";
            for (int j = 0; j < cols; j++){
                if (j < cols - 1){
                    ret += (matrix[i][j] + ", ");
                }else{
                    ret += matrix[i][j];
                }
            }
            if (i < rows - 1){
                ret += "],\n";
            }else{
                ret += "]";
            }
        }
        return ret + "]\n";
    }

    //Returns a String of values to be used in configuration files when Neural Network is saved
    public String toConfigString(){
        String ret = "";
        for (int i = 0; i < this.getRows(); i++){
            for (int j = 0; j < this.getCols(); j++){
                ret += this.getMatrix()[i][j] + "\n";
            }
        }
        return ret;
    }

    //Print out Matrix
    public void printMatrix(){
        System.out.println(this.toString());
    }

    //Print array as 1d float array of values 
    public static void printArr(float[] arr){
        System.out.print("[");
        for (int i = 0; i < arr.length - 1; i++){
            System.out.print(arr[i] + ", ");
        }
        System.out.println(arr[arr.length - 1] + "]");
    }

    //GETTER METHODS

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public float[][] getMatrix(){
        return matrix;
    }

    //Determines whether matrices can be added together
    private boolean isCompatibleForAdditionWith(Matrix m){
        return (this.rows == m.rows && this.cols == m.cols);
    }

    //Determines whether or not matrices can be multiplied (cols of first = rows of second)
    public boolean isCompatibleWith(Matrix m){
        return (this.cols == m.rows);
    }

    //return random float value
    public static float random(int min, int max){
        int range = max - min;
        return (float)(min+Math.random()*range);
    }
}
