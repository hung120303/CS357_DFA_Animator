package org.example;
import java.util.Set;
import java.util.HashSet;

public class Reader {
    int numStates;
    Set<Character> sigma;
    Set<Transition> transitions;
    int start;
    Set<Integer> accept; 

    //Constructor
    Reader(int s, Set<Character> sig, Set<Transition> t, int st, Set<Integer> acc){
        numStates = s;
        sigma = sig;
        transitions = t;
        start = st;
        accept = acc;
    }


}
