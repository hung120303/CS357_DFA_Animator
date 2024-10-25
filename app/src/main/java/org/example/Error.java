package org.example;

import javax.swing.*;
import java.util.Set;
import java.util.HashSet;


public class Error {
	
	
	public int promptStates(){
		boolean valid = false;
		int val = -1; 
		
		//Until we have a valid input
		while(!valid) {
			try  {
				//Store input into String
				String s = JOptionPane.showInputDialog(null, "Enter number of states (Min: 1; Max: 10): ");
				
				//If s is null, user pressed cancel or closed the window. exit the program
				if(s == null){
					JOptionPane.showMessageDialog(null, "Exiting program.");
					System.exit(0);
				}
				
				// Convert input to int 
				val = Integer.parseInt(s);	
				
				// value is between 1 and 10, return
				if(val > 0 && val < 11){
					valid = true;
				}
				// value is not 1-10, error msg
				else{
					JOptionPane.showMessageDialog(null, "Invalid Number. Input must be 1-10. Try again.");
				}
			}
			catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid input. Input must be 1-10. Try again.");
			}
		}	
		return val;
	}
	
	
}