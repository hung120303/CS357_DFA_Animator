package org.example;

public class Transition {
    char input;
    int from;
    int to;
    public Transition(char i, int f, int t){
        input = i;
        from = f;
        to = t;
    }

    public char getInput(){
        return input; 
    }
    public int getFrom(){
        return from; 
    }
    public int getTo(){
        return to; 
    }

    public String toString(){
        return "(" + input + ", " + from + ", " + to +")";
    }

}
