package org.example;

import javax.swing.*;
import java.util.Set;
import java.util.HashSet;
import javax.swing.JOptionPane;


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

	public String promptSigma(){
		boolean valid = false;
		String s = "";

		while(!valid) {
			try {
				//Store input into String
				s = JOptionPane.showInputDialog("Enter your alphabet (Use numbers (0-9), uppercase/lowercase letters (A-Z, a-z))(Ex: a,b,1,0): ");

				//If s is null, user pressed cancel or closed the window. exit the program
				if(s == null){
					JOptionPane.showMessageDialog(null, "Exiting program.");
					System.exit(0);
				}


				int i = 0;
				boolean pass = true;
				while(i < s.length()){ //enumerate through each char element of the intput string
					char cur = s.charAt(i);
					if( ((int)cur > 47 && (int)cur < 58) || ((int)cur > 64 && (int)cur < 91) || ((int)cur > 96 && (int)cur < 123)){ // If ascii value is 0-9, A-Za-z
						i++;
						if(i < s.length()){
							//check char after
							cur = s.charAt(i);
							if((int)cur == 44){ // ,
								i++;
							}
							else{
								pass = false;
							}
						}
					}
					else if( (int)cur == 32){ // space
						i++;
					}
				}
				if(!pass){
					JOptionPane.showMessageDialog(null, "Invalid input (Use numbers (0-9), uppercase/lowercase letters (A-Z, a-z))(Ex: a,b,1,0). Try again.");
				}
				else{
					valid = true;
				}
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Invalid input (Use numbers (0-9), uppercase/lowercase letters (A-Z, a-z))(Ex: a,b,1,0). Try again.");
			}
		}
		return s;
	}

	public String promptDelta(){
		boolean valid = false;
		String s = "";


		while(!valid) {
			try {
				//Store input into String
				s = JOptionPane.showInputDialog("Enter transitions (Ex: {(a,0,1),(b,0,2),(a,1,1)}): ");

				//If s is null, user pressed cancel or closed the window. exit the program
				if(s == null){
					JOptionPane.showMessageDialog(null, "Exiting program.");
					System.exit(0);
				}

				if (s.charAt(0) != '{') { // Input should start with '{'
					JOptionPane.showMessageDialog(null, "Invalid input (Enter transitions (Ex: {(a,0,1),(b,0,2),(a,1,1)} ). Try again."); // This should be an error message for invalid format
        		}
				else{
					boolean pass = true;
					int i = 1;
					char cur;
					while (i < s.length()) { // Until reach end of string
						cur = s.charAt(i);
						//input (0,0,0)
						if( (int)cur == 40){ // '('
							i++;
							if(i < s.length()){
								cur = s.charAt(i);
								if (((int)cur > 47 && (int)cur < 58) || ((int)cur > 64 && (int)cur < 91) || ((int)cur > 96 && (int)cur < 123)){ // input 0-9, A-Za-z
									i++;
									if(i < s.length()){
										cur = s.charAt(i);
										if((int)cur == 44){ // ,
											i++;
											if(i < s.length()){
												cur = s.charAt(i);
												if((int)cur > 47 && (int)cur < 58){ // input 0-9
													i++;
													if(i < s.length()){
														cur = s.charAt(i);
														if((int)cur == 44){ // ,
															i++;
															if(i < s.length()){
																cur = s.charAt(i);
																if((int)cur > 47 && (int)cur < 58){ // input 0-9
																	i++;
																	if(i < s.length()){
																		cur = s.charAt(i);
																		if((int)cur == 41){ // ')''
																			i++;
																		}
																	}
																	else{
																		pass = false;
																	}
																}
																else{
																	pass = false;
																}
															}
															else{
																pass = false;
															}
														}
													}
													else{
														pass = false;
													}
												}
												else{
													pass = false;
												}
											}
											else{
												pass = false;
											}
										}
										else{
											pass = false;
										}
									}
									else{
										pass = false;
									}
								}
								else{
									pass = false;
								}
							}
						}
						else if( cur == '}' && i == s.length() - 1){
							i++;
							valid = true;
						}
						else {
							pass = false;
						}
						// , and spaces
						if(i < s.length()){
							cur = s.charAt(i);
							if( (int)cur == 44){
								i++;
							}
							if(i < s.length()){
								cur = s.charAt(i);
								if( (int)cur == 32){
									i++;
								}
							}
						}
						else{
							pass = false;
						}
					}
					if(pass){
						valid = true;
					}
					if(!valid){
						JOptionPane.showMessageDialog(null, "Invalid input (Enter transitions (Ex: {(a,0,1),(b,0,2),(a,1,1)} ). Try again.");
					}
				}
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Invalid input (Enter transitions (Ex: {(a,0,1),(b,0,2),(a,1,1)} ). Try again.");
			}
		}



		return s;
	}

	public String promptAcceptingStates() {
        boolean valid = false;
        String s = "";

        while (!valid) {
            try {

                s = JOptionPane.showInputDialog("Enter the accepting state(s) (Use numbers 1-10, separated by commas) (Ex: 1,2,3): ");


                if (s == null) {
                    JOptionPane.showMessageDialog(null, "Exiting program.");
                    System.exit(0);
                }

                int i = 0;
                boolean pass = true;
                while (i < s.length()) { 
                    char cur = s.charAt(i);


                    if (Character.isDigit(cur)) {
                        int number = Character.getNumericValue(cur);


                        if (number == 1 && i + 1 < s.length() && s.charAt(i + 1) == '0') {
                            i += 2; 
                        } else if (number >= 1 && number <= 9) {
                            i++;
                        } else {
                            pass = false;
                            break;
                        }


                        if (i < s.length() && s.charAt(i) == ',') {
                            i++; 
                        } else if (i < s.length() && s.charAt(i) != ' ') {

                            pass = false;
                            break;
                        }
                    } else if (cur == ' ') {

                        i++;
                    } else {
                        pass = false;
                        break;
                    }
                }

                if (!pass) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Use numbers 1-10 separated by commas (Ex: 1,2,3). Try again.");
                } else {
                    valid = true; 
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Use numbers 1-10 separated by commas (Ex: 1,2,3). Try again.");
            }
        }
        return s;
    }

	public int promptStartState(){
        boolean valid = false;
        int val = -1;

        //Until we have a valid input
        while(!valid) {
            try  {
                //Store input into String
                String s = JOptionPane.showInputDialog("Enter the start state (0-10");

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