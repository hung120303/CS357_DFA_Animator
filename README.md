# CS357-DFA-Animator

## Overview
**CS357-DFA-Animator** is a Deterministic Finite Automaton (DFA) visualization tool. Users provide the DFA's specifications as input, press "OK," and the tool dynamically generates a graphical representation of the DFA.

The transitions in the DFA are categorized and drawn in a way that maintains clarity and prevents overlap. The visualizations include states, transitions, and labels, ensuring a clean and professional appearance.

---

## Transition Categorization and Drawing

The tool categorizes transitions based on their characteristics and draws them accordingly:

### Categories
- **Self-Loops**: 
  - Transitions where the `fromState` and `toState` are the same.
- **Transitions Between Adjacent States**: 
  - States that are next to each other.
- **Transitions Between Non-Adjacent States**: 
  - States that are not next to each other.
- **Bidirectional Transitions**: 
  - Transitions that exist in both directions between two states.

### Drawing Rules
- **Self-Loops**: Drawn as an arc above the state.
- **Transitions Between Adjacent States**: Represented by straight lines with arrowheads.
- **Transitions Between Non-Adjacent States**: Depicted using quadratic Bézier curves defined by three points.
- **Bidirectional Transitions**: Drawn as curves or lines with offsets to prevent overlap.

### Label Management
- The positions of labels are carefully tracked to avoid overlap.
- Additional offsets are applied dynamically if a label's position conflicts with another element.

---

## Features
- **Dynamic Visualization**: Automatically generates the DFA diagram based on user input.
- **Collision-Free Design**: Ensures transitions, arrows, and labels do not overlap for a clean layout.
- **Support for Complex DFAs**: Handles bidirectional transitions, self-loops, and non-adjacent state connections.

---

## Example Visualization
![image](https://github.com/user-attachments/assets/72bcd190-fa93-4d32-86d3-b88974e65034)


---

## Requirements
- **Java Swing**: The tool uses Java's Swing library for GUI and graphics.
- **JDK Version**: Ensure you have JDK 8 or higher installed.

---

## Getting Started
1. Clone the repository:
   ```bash
   git clone https://github.com/hung120303/CS357-DFA-Animator.git

## How to Use
1. Provide input for the DFA in the following format:
    - **Number of states**: Total number of states in the DFA.
    - **Alphabet**: A comma-separated list of valid inputs.
    - **Transitions**: A set of transitions in the form `(input, fromState, toState)`.
    - **Start state**: The starting state of the DFA.
    - **Accepting states**: A list of states that are accepting (final).

2. Press **OK** to generate the visualization.

### Example Input
```text
3
a, b
{(a,0,1),(b,0,2),(a,1,1),(b,1,2),(a,2,0),(b,2,2)}
0
2


