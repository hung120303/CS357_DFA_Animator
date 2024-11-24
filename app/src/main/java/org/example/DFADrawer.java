package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
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

        // Enable anti-aliasing for smoother lines and text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Set stroke for thicker lines (optional)
        g2d.setStroke(new BasicStroke(2));

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

        // **New code to keep track of label positions to prevent overlaps**
        List<Rectangle> labelBounds = new ArrayList<>();

        // Draw transitions (lines/arcs) between states
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
                Point labelPos = new Point(fromX - 5, fromY - 2 * loopRadius - 5);
                labelPos = adjustLabelPosition(labelPos, inputStr, g2d, labelBounds);
                g2d.drawString(inputStr, labelPos.x, labelPos.y);
            } else {
                if (isBidirectional) {
                    // Offset the arcs for bidirectional transitions
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

                // Determine if states are adjacent
                boolean areAdjacent = Math.abs(fromState - toState) == 1;

                if (areAdjacent) {
                    // For adjacent states, draw a straight line with an arrow
                    drawArrowLine(g2d, adjustedFromX, adjustedFromY, adjustedToX, adjustedToY, 10, 7);

                    // Calculate label position for straight line
                    int labelX = (adjustedFromX + adjustedToX) / 2;
                    int labelY = (adjustedFromY + adjustedToY) / 2 - 5;

                    // Draw the transition labels
                    String inputStr = formatInputs(inputs);
                    Point labelPos = new Point(labelX, labelY);
                    labelPos = adjustLabelPosition(labelPos, inputStr, g2d, labelBounds);
                    g2d.drawString(inputStr, labelPos.x, labelPos.y);
                } else {
                    // For non-adjacent states, draw an arc with an arrow
                    drawArrowArc(g2d, adjustedFromX, adjustedFromY, adjustedToX, adjustedToY, 10, 7, isBidirectional);

                    // **Calculate the midpoint of the arc for label positioning**
                    Point2D.Float midpoint = getQuadCurveMidpoint(adjustedFromX, adjustedFromY, adjustedToX, adjustedToY, isBidirectional);

                    // Draw the transition labels
                    String inputStr = formatInputs(inputs);
                    Point labelPos = new Point((int) midpoint.x, (int) midpoint.y - 10); // Adjust label above the arc
                    labelPos = adjustLabelPosition(labelPos, inputStr, g2d, labelBounds);
                    g2d.drawString(inputStr, labelPos.x, labelPos.y);
                }
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
        // Draw a small arrow pointing to the start state
        drawArrowLine(g2d, startX - radius - 30, startY, startX - radius, startY, 10, 7);
        // Optionally, draw a label "Start"
        g2d.drawString("Start", startX - radius - 50, startY + 5);
    }

    /**
     * Draws a directed line (arrow) between two points.
     *
     * @param g2d            Graphics2D object to draw with.
     * @param x1             X-coordinate of the starting point.
     * @param y1             Y-coordinate of the starting point.
     * @param x2             X-coordinate of the ending point.
     * @param y2             Y-coordinate of the ending point.
     * @param arrowHeadLength Length of the arrowhead.
     * @param arrowHeadWidth  Width of the arrowhead.
     */
    private void drawArrowLine(Graphics2D g2d, int x1, int y1, int x2, int y2, int arrowHeadLength, int arrowHeadWidth) {
        // Draw the main line
        g2d.drawLine(x1, y1, x2, y2);

        // Calculate the angle of the line
        double angle = Math.atan2(y2 - y1, x2 - x1);

        // Calculate the coordinates for the arrowhead
        int arrowX1 = x2 - (int) (arrowHeadLength * Math.cos(angle - Math.PI / 6));
        int arrowY1 = y2 - (int) (arrowHeadLength * Math.sin(angle - Math.PI / 6));
        int arrowX2 = x2 - (int) (arrowHeadLength * Math.cos(angle + Math.PI / 6));
        int arrowY2 = y2 - (int) (arrowHeadLength * Math.sin(angle + Math.PI / 6));

        // Draw the arrowhead
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(x2, y2);
        arrowHead.addPoint(arrowX1, arrowY1);
        arrowHead.addPoint(arrowX2, arrowY2);

        g2d.fillPolygon(arrowHead);
    }

    /**
     * Draws an arc between two points with an arrowhead.
     *
     * @param g2d            Graphics2D object to draw with.
     * @param x1             X-coordinate of the starting point.
     * @param y1             Y-coordinate of the starting point.
     * @param x2             X-coordinate of the ending point.
     * @param y2             Y-coordinate of the ending point.
     * @param arrowHeadLength Length of the arrowhead.
     * @param arrowHeadWidth  Width of the arrowhead.
     * @param isBidirectional Indicates if the arc is part of a bidirectional transition.
     */
    private void drawArrowArc(Graphics2D g2d, int x1, int y1, int x2, int y2, int arrowHeadLength, int arrowHeadWidth, boolean isBidirectional) {
        // Calculate control points for the quadratic curve
        int ctrlX = (x1 + x2) / 2;
        int ctrlY = Math.min(y1, y2) - 100; // Adjust the curvature as needed

        if (isBidirectional) {
            ctrlY -= 50; // Offset for bidirectional arcs
        }

        // Create a quadratic curve (arc) path
        QuadCurve2D.Float q = new QuadCurve2D.Float();
        q.setCurve(x1, y1, ctrlX, ctrlY, x2, y2);

        // Draw the arc
        g2d.draw(q);

        // Calculate the angle of the tangent at the end point
        double t = 1.0; // At the end point
        double[] derivative = quadraticDerivative(x1, ctrlX, x2, y1, ctrlY, y2, t);
        double angle = Math.atan2(derivative[1], derivative[0]);

        // Calculate the coordinates for the arrowhead
        int arrowX1 = x2 - (int) (arrowHeadLength * Math.cos(angle - Math.PI / 6));
        int arrowY1 = y2 - (int) (arrowHeadLength * Math.sin(angle - Math.PI / 6));
        int arrowX2 = x2 - (int) (arrowHeadLength * Math.cos(angle + Math.PI / 6));
        int arrowY2 = y2 - (int) (arrowHeadLength * Math.sin(angle + Math.PI / 6));

        // Draw the arrowhead
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(x2, y2);
        arrowHead.addPoint(arrowX1, arrowY1);
        arrowHead.addPoint(arrowX2, arrowY2);

        g2d.fillPolygon(arrowHead);
    }

    /**
     * Calculates the midpoint of a quadratic curve (arc).
     *
     * @param x1             X-coordinate of the starting point.
     * @param y1             Y-coordinate of the starting point.
     * @param x2             X-coordinate of the ending point.
     * @param y2             Y-coordinate of the ending point.
     * @param isBidirectional Indicates if the arc is part of a bidirectional transition.
     * @return Point2D.Float representing the midpoint of the arc.
     */
    private Point2D.Float getQuadCurveMidpoint(int x1, int y1, int x2, int y2, boolean isBidirectional) {
        // Control point for the quadratic curve
        float ctrlX = (x1 + x2) / 2f;
        float ctrlY = Math.min(y1, y2) - 100f;

        if (isBidirectional) {
            ctrlY -= 50f; // Offset for bidirectional arcs
        }

        // Calculate the point at t = 0.5 (midpoint)
        float t = 0.5f;
        float oneMinusT = 1 - t;

        float midX = oneMinusT * oneMinusT * x1 + 2 * oneMinusT * t * ctrlX + t * t * x2;
        float midY = oneMinusT * oneMinusT * y1 + 2 * oneMinusT * t * ctrlY + t * t * y2;

        return new Point2D.Float(midX, midY);
    }

    /**
     * Calculates the derivative of a quadratic Bezier curve at a given t.
     *
     * @param x0 Start x-coordinate.
     * @param x1 Control x-coordinate.
     * @param x2 End x-coordinate.
     * @param y0 Start y-coordinate.
     * @param y1 Control y-coordinate.
     * @param y2 End y-coordinate.
     * @param t  Parameter t (0 <= t <= 1).
     * @return Array containing dx/dt and dy/dt.
     */
    private double[] quadraticDerivative(double x0, double x1, double x2, double y0, double y1, double y2, double t) {
        double dxdt = 2 * (1 - t) * (x1 - x0) + 2 * t * (x2 - x1);
        double dydt = 2 * (1 - t) * (y1 - y0) + 2 * t * (y2 - y1);
        return new double[]{dxdt, dydt};
    }

    /**
     * Adjusts the label position to prevent overlap with existing labels.
     *
     * @param labelPos   Initial position of the label.
     * @param text       The text of the label.
     * @param g2d        Graphics2D object to measure text dimensions.
     * @param labelBounds List of existing label bounds.
     * @return Adjusted position for the label.
     */
    private Point adjustLabelPosition(Point labelPos, String text, Graphics2D g2d, List<Rectangle> labelBounds) {
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        Rectangle newLabelRect = new Rectangle(labelPos.x, labelPos.y - textHeight, textWidth, textHeight);

        boolean adjusted = false;
        int offsetY = 0;

        // Try moving the label up or down to avoid overlap
        while (true) {
            boolean overlaps = false;
            for (Rectangle rect : labelBounds) {
                if (newLabelRect.intersects(rect)) {
                    overlaps = true;
                    break;
                }
            }
            if (!overlaps) {
                break;
            }
            // Move the label up or down alternately
            offsetY += 15;
            if (!adjusted) {
                newLabelRect.y = labelPos.y - textHeight - offsetY;
                adjusted = true;
            } else {
                newLabelRect.y = labelPos.y - textHeight + offsetY;
                adjusted = false;
            }
        }

        // Add the new label bounds to the list
        labelBounds.add(newLabelRect);

        // Return the adjusted label position
        return new Point(newLabelRect.x, newLabelRect.y + textHeight);
    }

    /**
     * Formats the list of inputs into a string without square brackets and spaces.
     *
     * @param inputs List of input characters.
     * @return Formatted string of inputs.
     */
    private String formatInputs(List<Character> inputs) {
        StringBuilder inputStrBuilder = new StringBuilder();
        for (char input : inputs) {
            inputStrBuilder.append(input).append(",");
        }
        String inputStr = inputStrBuilder.substring(0, inputStrBuilder.length() - 1); // Remove trailing comma
        return inputStr;
    }
}
