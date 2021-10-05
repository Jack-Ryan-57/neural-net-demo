# Demonstration of Scratch-Built Neural Network
Simple application built with Java

## Introduction 
A visual demonstration of a simple neural network using stochastic gradient descent. A basic Java GUI demonstrates functionality of network by solving the simple problem of categorizing colors. Given an RGB pixel value, the network will output its best guess as to which basic color the value resembles, picking from the following list: red, orange, yellow, green, blue, purple, pink, brown, black, gray, white. Additionally, the user may also create a "Doodle" out of the image "painting.png" found in the "Neural Net Demos" folder. This "Doodle" classifies each pixel of "painting.png" using the neural net, and outputs it in the file "painting-condensed.png."

In the future, I plan on adding additional demonstrations, the next being digit recognition using the MNIST dataset. Admittedly, however, there are no shortage of bugs to fix either. 

I started this project during a lonely, COVID-conscious Winter Break last December, originally as a simple test of my abilities. Without the use of Python's numpy, however, (a tool which would have been extremely handy) I quickly realized this project would be bigger than expected. As a result, however, I had an excuse to truly dig into the math and inner workings of neural networks at a basic level. I brushed up again on my knowledge of Matrix math, as well as using some calculus knowledge. So while using basic Java made the process longer, this is not a decision I regret; it forced me to truly understand from the ground up how the network was working. 

### Setup
To run this project:
    -Clone repository
    -Run main method in nndemo\src\main\java\com\nndemo\Demo.java

#### Technologies
Built with Java version 15.0.1

