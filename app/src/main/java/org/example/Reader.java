package org.example;

public class Reader {
    int numStates;
    char[] sigma;
    Transition[] transitions;
    int start;
    int[] accept; 

    //Constructor
    Reader(int s, char[] sig, Transition[] t, int st, int[] acc){
        numStates = s;
        sigma = sig;
        transitions = t;
        start = st;
        accept = acc;
    }

    
}
