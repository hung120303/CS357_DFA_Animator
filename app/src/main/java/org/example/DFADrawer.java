package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DFADrawer extends JPanel {
    private final Reader reader;

    public DFADrawer(Reader reader) {
        this.reader = reader;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int numStates = reader.getNumStates();
        Set<Integer> acceptingStates = reader.getAccept();
        int startState = reader.getStart();
        Set<Transition> transitions = reader.getTransitions();

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int radius = 30; // Radius of each state circle

        // Calculate the horizontal spacing between states
        int spacing = (panelWidth - 2 * radius) / (numStates + 1);

        // Vertical position for all states
        int y = panelHeight / 2;

        // Store positions of all states for consistency
        int[] stateXs = new int[numStates];
        int[] stateYs = new int[numStates];

        // Assign positions to states in order
        for (int i = 0; i < numStates; i++) {
            int x = spacing * (i + 1);
            stateXs[i] = x;
            stateYs[i] = y;
        }

        // Group transitions by fromState and toState
        Map<String, List<Character>> transitionMap = new HashMap<>();
        Set<String> bidirectionalPairs = new HashSet<>();

        for (Transition transition : transitions) {
            int fromState = transition.getFrom();
            int toState = transition.getTo();
            char input = transition.getInput();

            String key = fromState + "->" + toState;
            transitionMap.computeIfAbsent(key, k -> new ArrayList<>()).add(input);

            // Check for bidirectional transitions
            String reverseKey = toState + "->" + fromState;
            if (transitionMap.containsKey(reverseKey)) {
                bidirectionalPairs.add(key);
                bidirectionalPairs.add(reverseKey);
            }
        }

        // Draw transitions (lines) between states
        for (String key : transitionMap.keySet()) {
            List<Character> inputs = transitionMap.get(key);
            String[] states = key.split("->");
            int fromState = Integer.parseInt(states[0]);
            int toState = Integer.parseInt(states[1]);

            int fromX = stateXs[fromState];
            int fromY = stateYs[fromState];
            int toX = stateXs[toState];
            int toY = stateYs[toState];

            // Adjust start and end positions to be at the edge of the circles
            double angle = Math.atan2(toY - fromY, toX - fromX);
            int adjustedFromX, adjustedFromY, adjustedToX, adjustedToY;

            // If bidirectional, offset the lines
            boolean isBidirectional = bidirectionalPairs.contains(key);

            if (fromState == toState) {
                // Self-loop
                // Handle self-loop with an arc above the state
                int loopRadius = radius;
                g2d.drawArc(fromX - loopRadius, fromY - 2 * loopRadius, 2 * loopRadius, 2 * loopRadius, 0, 360);
                // Draw the transition labels
                String inputStr = inputs.toString();
                g2d.drawString(inputStr, fromX - 5, fromY - 2 * loopRadius - 5);
            } else {
                if (isBidirectional) {
                    // Offset the lines for bidirectional transitions
                    int offset = 15;
                    double sin = Math.sin(angle);
                    double cos = Math.cos(angle);
                    adjustedFromX = (int) (fromX + radius * cos - offset * sin);
                    adjustedFromY = (int) (fromY + radius * sin + offset * cos);
                    adjustedToX = (int) (toX - radius * cos - offset * sin);
                    adjustedToY = (int) (toY - radius * sin + offset * cos);
                } else {
                    adjustedFromX = (int) (fromX + radius * Math.cos(angle));
                    adjustedFromY = (int) (fromY + radius * Math.sin(angle));
                    adjustedToX = (int) (toX - radius * Math.cos(angle));
                    adjustedToY = (int) (toY - radius * Math.sin(angle));
                }

                // Draw line from fromState to toState
                g2d.drawLine(adjustedFromX, adjustedFromY, adjustedToX, adjustedToY);

                // Draw the transition labels
                StringBuilder inputStrBuilder = new StringBuilder();
                for (char input : inputs) {
                    inputStrBuilder.append(input).append(",");
                }
                String inputStr = inputStrBuilder.substring(0, inputStrBuilder.length() - 1); // Remove trailing comma

                int labelX = (adjustedFromX + adjustedToX) / 2;
                int labelY = (adjustedFromY + adjustedToY) / 2 - 5;

                if (isBidirectional) {
                    labelX += 10; // Offset label position
                    labelY -= 10;
                }

                g2d.drawString(inputStr, labelX, labelY);
            }
        }

        // Draw circles (states) in order
        for (int i = 0; i < numStates; i++) {
            int x = stateXs[i];
            int yPos = stateYs[i];

            // Draw the state circle
            if (acceptingStates.contains(i)) {
                g2d.setColor(Color.GREEN); // Accepting states are green
            } else {
                g2d.setColor(Color.LIGHT_GRAY); // Regular states are light gray
            }
            g2d.fillOval(x - radius, yPos - radius, 2 * radius, 2 * radius);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x - radius, yPos - radius, 2 * radius, 2 * radius);

            // Draw the state label
            String stateLabel = "q" + i;
            g2d.drawString(stateLabel, x - 10, yPos + 5);
        }

        // Indicate the start state
        int startX = stateXs[startState];
        int startY = stateYs[startState];
        // Draw a small line pointing to the start state
        g2d.drawLine(startX - radius - 20, startY, startX - radius, startY);
        // Optionally, draw a label "Start"
        g2d.drawString("Start", startX - radius - 40, startY + 5);
    }
}
