package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import java.util.HashSet;

public class App {
    private static DFADrawer drawer; // Make drawer a class-level variable
    private static JPanel mainPanel; // Main panel to hold components

    public static void main(String[] args) {
        // Create and configure the JFrame to visualize the DFA
        JFrame frame = new JFrame("DFA Visualizer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 600); // Adjusted height for horizontal layout
        frame.setLocationRelativeTo(null);

        // Create the main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());

        // Create a panel for the restart button aligned to the right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Create the restart button
        JButton restartButton = new JButton("Restart");
        buttonPanel.add(restartButton);

        // Add the button panel to the top of the main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // First time setup: create and display the DFA
        createAndDisplayDFA(frame);

        // Add action listener to the restart button
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove the current DFADrawer
                mainPanel.remove(drawer);

                // Prompt the user for new DFA input and display it
                createAndDisplayDFA(frame);
            }
        });

        // Add the main panel to the frame and display it
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void createAndDisplayDFA(JFrame frame) {
        // Error class to check for valid input
        Error e = new Error();

        // Get user input for DFA configuration
        int states = e.promptStates();
        String sigma = e.promptSigma();
        String delta = JOptionPane.showInputDialog("Enter transitions (Ex: {(a,0,1),(b,0,2),(a,1,1)}): ");
        int startState = e.promptStartState();
        String accState = e.promptAcceptingStates();

        Set<Character> sig = App.getAlphabet(sigma);
        Set<Transition> t = App.getTransitions(delta, states, sig);
        Set<Integer> acc = App.getAccStates(accState, states);

        // Validate the DFA configuration
        if (t == null) {
            JOptionPane.showMessageDialog(null, "Invalid transition table.");
            return;
        }

        // Create a Reader instance based on user input
        Reader reader = new Reader(states, sig, t, startState, acc);

        // Create the DFADrawer and add it to the main panel
        drawer = new DFADrawer(reader);
        mainPanel.add(drawer, BorderLayout.CENTER);

        // Refresh the main panel
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static Set<Character> getAlphabet(String s) {
        Set<Character> sigma = new HashSet<Character>();
        for (int i = 0; i < s.length(); i++) { // Enumerate through each char element of the input string
            char cur = s.charAt(i);
            if (((int) cur > 47 && (int) cur < 58) || ((int) cur > 64 && (int) cur < 91) || ((int) cur > 96 && (int) cur < 123)) { // If ASCII value is 0-9, A-Z, a-z
                sigma.add(cur); // Add char to sigma
            }
        }
        return sigma;
    }

    public static Set<Integer> getAccStates(String s, int numStates) {
        Set<Integer> qAcc = new HashSet<Integer>();
        for (int i = 0; i < s.length(); i++) { // Enumerate through each char element of the input string
            char cur = s.charAt(i);
            if (((int) cur > 47 && (int) cur < 58)) { // If ASCII value is 0-9
                int add = cur - '0';
                if (add < numStates) {
                    qAcc.add(add); // Add int to qAcc
                }
            }
        }
        return qAcc;
    }

    public static Set<Transition> getTransitions(String s, int numStates, Set<Character> sigma) {
        Set<Transition> delta = new HashSet<Transition>();
        if (s.charAt(0) != '{') { // Input should start with '{'
            return null; // This should be an error message for invalid format
        }
        int i = 1;
        while (i < s.length()) { // Until reach end of string
            if ((int) (s.charAt(i)) == 40) { // Check for '('
                char input = '~';
                int from = -1;
                int to = -1;
                i++;
                while ((int) (s.charAt(i)) != 41) { // Check for ')'
                    if (input == '~') {
                        if (((int) (s.charAt(i)) > 47 && (int) (s.charAt(i)) < 58) || ((int) (s.charAt(i)) > 64 && (int) (s.charAt(i)) < 91) || ((int) (s.charAt(i)) > 96 && (int) (s.charAt(i)) < 123)) {
                            input = s.charAt(i);
                        }
                    } else if (from == -1) {
                        if (((int) (s.charAt(i)) > 47 && (int) (s.charAt(i)) < 58)) {
                            from = s.charAt(i) - '0';
                        }
                    } else if (to == -1) {
                        if (((int) (s.charAt(i)) > 47 && (int) (s.charAt(i)) < 58)) {
                            to = s.charAt(i) - '0';
                        }
                    }
                    i++;
                }
                if (input != '~' && from != -1 && to != -1) {
                    delta.add(new Transition(input, from, to));
                } else {
                    return null;
                }
            }
            i++;
        }
        if (isValidTransitions(delta, numStates, sigma)) {
            return delta;
        } else {
            System.out.println("This transition table is invalid");
            return null;
        }
    }

    public static boolean isValidTransitions(Set<Transition> tTable, int numStates, Set<Character> sigma) {
        // There should be one of each alphabet transitions for each state
        for (char c : sigma) {
            int[] check = new int[numStates];
            for (Transition t : tTable) {

                char curChar = t.getInput(); // We only care about the transition symbol and the state it's coming from
                int curFrom = t.getFrom();

                if (curChar == c) {
                    check[curFrom] += 1;
                }

            }
            for (int i : check) {
                if (i != 1) {
                    return false; // There is not a single transition for one symbol for one state (0 or more than 1)
                }
            }
        }

        // There should be (# of states) * (# of characters in alphabet) transitions
        if (tTable.size() != (numStates * sigma.size())) {
            return false;
        }

        return true;
    }
}
