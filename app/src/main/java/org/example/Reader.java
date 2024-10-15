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

    public Set<Integer> getAccept() {
        return accept;
    }

    public int getNumStates() {
        return numStates;
    }

    public Set<Character> getSigma() {
        return sigma;
    }

    public int getStart() {
        return start;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    public String printStates(){
        String s = "{";
        for(int i = 0; i < numStates; i++){
            if(i != numStates - 1)
                s += "q" + i + ", ";
            else
                s += "q" + i + "}";
        }
        return s;
    }

    public String printStartState(){
        return "q" + start;
    }


    public String toString() {
        return "Formal Description:\nQ: " + printStates()  + "\nSigma: " + getSigma().toString() + "\nDelta: " + getTransitions().toString() + "\nStart: " + printStartState() + "\nAccept: " + getAccept().toString();
    }

}
