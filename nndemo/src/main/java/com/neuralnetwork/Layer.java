package com.neuralnetwork;

public class Layer {
    
    public Matrix weights, biases, inputs, values;
    float[][] matrix, sigmoids;
    public static boolean first = true;

    //Constructor for first hidden layer (hidden layer straight after inputs)
    public Layer(int neuronsIn, int numNeurons){
        biases = new Matrix(numNeurons, 1, first);
        weights = new Matrix(numNeurons, neuronsIn, first);
    }

    //Constructor for the rest of the layers
    public Layer(int neuronsIn, int numNeurons, Matrix input){
        biases = new Matrix(numNeurons, 1, first);
        weights = new Matrix(numNeurons, neuronsIn, first);
        inputs = new Matrix(neuronsIn, 1, input.getMatrix());
    }

    //Matrix Math for feeding values forward (for one layer)
    public Matrix feedForward(float[] input){
        Matrix ret = null;
        float[][] inputMatrix = Matrix.inputsToMatrix(input);
        Matrix inputs = new Matrix(input.length, 1, inputMatrix);
        ret = Matrix.matrixProduct(weights, inputs);
        ret.addMatrix(biases);
        //Multiply entire matrix by sigmoid function
        sigmoid(ret);
        this.values = ret;
        return ret;
    }

    //Sigmoid activation function
    public void sigmoid(Matrix m){
        sigmoids = new float[m.getMatrix().length][m.getMatrix()[0].length];
        for (int i = 0; i < m.getRows(); i++){
            for (int j = 0; j < m.getCols(); j++){
                float x = m.getMatrix()[i][j];
                float num = (float)(1/(1+Math.exp(-x)));
                sigmoids[i][j] = num;
                m.getMatrix()[i][j] *= num;
            }
        }
    }
    

    //Return 2d float array of differentiated sigmoid functions 
    public Matrix dsigmoid(){
        float[][] ret = new float[sigmoids.length][sigmoids[0].length];
        for (int i = 0; i < sigmoids.length; i++){
            for (int j = 0; j < sigmoids[0].length; j++){
                //x(1-x)
                ret[i][j] = sigmoids[i][j]*(1-sigmoids[i][j]);
            }
        }
        return new Matrix(sigmoids.length, sigmoids[0].length, ret);
    }

    public void populateNeurons(Matrix m){
        float[][] temp = new float[m.getRows()][m.getCols()];
        for (int i = 0; i < m.getRows(); i++){
            for (int j = 0; j < m.getCols(); j++){
                temp[i][j] = m.getMatrix()[i][j];
            }
        }
        values = new Matrix(m.getRows(), m.getCols(), temp);
    }
    
    public static void setRandomize(boolean randomize){
        first = randomize;
    }

    //Returns readable String of Layer info (for debugging, etc.)
    public String toString(){
        String ret = "";
        ret += ("Weights:\n" + weights.toString() + "\n");
        ret += ("Biases: \n" + biases.toString() + "\n");
        ret += ("Values:\n" + values.toString() + "\n");
        return ret;
    }

    //Returns String in simple format for configuration
    public String toConfigString(){
        String ret = "";
        ret += ("Weights:\n" + weights.toConfigString());
        ret += ("Biases:\n" + biases.toConfigString());
        ret += ("Values:\n" + values.toConfigString());
        return ret;
    }
}
