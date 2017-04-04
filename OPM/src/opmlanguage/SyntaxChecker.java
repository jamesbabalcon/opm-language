package opmlanguage;

import java.util.ArrayList;
import java.util.Stack;

public class SyntaxChecker {

	private Stack<String> matchStack;
	private ArrayList<Variable> variables;
	private ArrayList<String> reserved;
	
	public SyntaxChecker(String text) {
		matchStack = new Stack<String>();
		variables = new ArrayList<Variable>();
		reserved = new ArrayList<String>();
		
		addReserved();
				
		if(valid(text) == true) {
			System.out.println("Code is valid");
		}
		else {
			System.out.println("Code is invalid");
		}
	}
	
	public void addReserved() {
		reserved.add("hotdog");
		reserved.add("jumbo-hotdog");
		reserved.add("salita");
		reserved.add("letra");
		reserved.add("pag-ibig");
		reserved.add("mahal-ko");
		reserved.add("mahal-ako");
		reserved.add("sabihin-mo-na");
		reserved.add("kung-ako-nalang-sana");
		reserved.add("humanap-ka-ng-panget");
		reserved.add("only-hope");
		reserved.add("anak");
		reserved.add("tuldok");
		reserved.add("sana-maulit-muli");
		reserved.add("doo-bidoo");
		reserved.add("muling-ibalik");
		reserved.add("tuldok");
	}
	
	public boolean valid(String text) {
		
		String[] tokens = text.split("\n");
		
		for(int x = 0; x < tokens.length; x++) {
			String line = tokens[x];
			
//			String line = tokens[x].replace("\n", "").replace("\r", "");
//			
//			if(x == 0) {
//				if(line.contains(" ")) {
//					System.out.println("Program name cannot contain spaces");
//					return false;
//				}
//				else {
//					System.out.println("Program name is " + line);
//				}
//			}
			
			if(line.equals("anak")) {
				matchStack.push("anak");
				System.out.println("Pushing to Stack");
			}
			if(line.equals("tuldok") && !matchStack.isEmpty()) {
				matchStack.pop();
				System.out.println("Popping from Stack");
			}
			else if(line.equals("tuldok") && matchStack.isEmpty()) {
				System.out.println("Unexpected EOF");
				return false;
			}
			
			if(type(line.split(" ")[0])) {
				if(declaration(line))
					continue;
				else {
					System.out.println("Syntax Error at line " + (x + 1));
					return false;
				}
			}
			
			if(x == tokens.length - 1 && matchStack.isEmpty()) {
				System.out.println("End of File, Stack is Empty");
				return true;
			}
			else if(x == tokens.length - 1 && !matchStack.isEmpty()) {
				System.out.println("End of File, Stack is not Empty");
				return false;
			}
		}
		
		return false;
	}
	
	public boolean type(String type) {
		
		if(type.equals("hotdog"))
			return true;
		else if(type.equals("jumbo-hotdog"))
			return true;
		else if(type.equals("salita"))
			return true;
		else if(type.equals("letra"))
			return true;
		else if(type.equals("pag-ibig"))
			return true;
		
		return false;
	}
	
	public boolean containsOperator(String op) {
		
		if(op.contains("+"))
			return true;
		if(op.contains("-"))
			return true;
		if(op.contains("*"))
			return true;
		if(op.contains("/"))
			return true;
		if(op.contains("%"))
			return true;
		if(op.contains("^"))
			return true;
		
		return false;
	}
	
	public boolean legal(String str) {
		
		for(Variable v : variables) {
			if(str.equals(v.getName())) {
				System.out.println("Duplicate variables not allowed");
				return false;
			}
		}
		for(String s : reserved) {
			if(str.equals(s)) {
				System.out.println("Reserved word");
				return false;
			}
		}
		return true;
	}
	
	public boolean isVariable(String var) {
		for(Variable v : variables) {
			if(var.equals(v.getName()))
				return true;
		}
		
		return false;
	}
	
	public boolean declaration(String text) {
		
		String[] tokens = text.split(" ");
		
		if(tokens.length == 2) {
			if(type(tokens[0]) && legal(tokens[1])) {
				Variable var = new Variable(tokens[0], tokens[1]);
				System.out.println("Variable " + var.getName() + " of type " + var.getType() + " created");
				variables.add(var);
				return true;
			}
		}
		else if(tokens.length == 4 && legal(tokens[1])) {
			if(type(tokens[0]) && tokens[2].equals("=")) {
				Variable var = new Variable(tokens[0], tokens[1], tokens[3]);
				System.out.println("Variable " + var.getName() + " of type " + var.getType() + " with value " + tokens[3] + " created");
				variables.add(var);
				return true;
			}
		}
		else if(containsOperator(text)) {
			if(type(tokens[0]) && tokens[2].equals("=")) {
				Variable var = new Variable(tokens[0], tokens[1]);
				variables.add(var);
				boolean result = operation(var, tokens[3], tokens[4], tokens[5]);
				if(result) {
					System.out.println("Variable " + var.getName() + " of type " + var.getType() + " with value "
				+ tokens[3] + tokens[4] + tokens[5] + " created");
				}
				return result;
			}
		}
		else if(text.contains(",")) {
			String multiple[] = text.split(",");
			String type = "";
			for(int x = 0; x < multiple.length; x++) {
				if(x == 0) {
					String first[] = multiple[x].split(" ");
					type = first[0];
				}
				else {
					multiple[x] = type + multiple[x];
				}
				
				if(!declaration(multiple[x])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
//	public boolean operation(String text) {
//		
//		String[] tokens = text.split(" ");
//		
//		if(tokens.length == 2) {
//			if(operator(tokens[0]) && isVariable(tokens[1])) {
//				                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
//			}
//		}
//		
//		return false;
//	}
	
	private boolean operation(Variable var, String operand1, String operator, String operand2) {
		
		operand1 = operand1.trim().replaceAll(" \n ", "");
		operand2 = operand2.trim().replaceAll(" \n ", "");
		
		boolean valid = false;
		String op1 = "", op2 = "", result = "";
			
		for(Variable v1 : variables) {
			if(v1.getName().equals(operand1) && v1.getType().equals(var.getType())) {
//				op1 = "" + v1.getInitValue();
//				op1 = op1.trim().replaceAll(" \n ", "");
				for(Variable v2 : variables) {
					if(v2.getName().equals(operand2) && v2.getType().equals(var.getType())) {
//						op2 = "" + v2.getInitValue();
//						valid = true;
//						break;
						return true;
					}
				}
				if(var.getType().equals(getType(operand2))) {
					return true;
				}
			}
		}
		
		if(var.getType().equals(getType(operand1))) {
			for(Variable v2 : variables) {
				if(v2.getName().equals(operand2) && v2.getType().equals(var.getType()))
					return true;
			}
		}
		
		if(var.getType().equals(getType(operand1)) && var.getType().equals(getType(operand2)))
			return true;
		
		return false;
			
//				if(var.getType().equals(getType(operand2))) {
//					op2 = "" + operand2;
//					valid = true;
//				}
//			}
//		}
//		
//		if(valid) {
//			if(var.getType().equals("hotdog")) {
//				int value1 = Integer.parseInt(op1);
//				int value2 = Integer.parseInt(op2);
//				result = "" + (value1 + value2);
//			} else if(var.getType().equals("jumbo-hotdog")) {
//				float value1 = Float.parseFloat(op1);
//				float value2 = Float.parseFloat(op2);
//				result = "" + (value1 + value2);
//			} else if(var.getType().equals("letra")) {
//				char value1 = op1.charAt(0);
//				char value2 = op2.charAt(0);
//				result = "" + value1 + value2;
//			} else if(var.getType().equals("salita")) {
//				String value1 = op1;
//				String value2 = op2;
//				result = "" + value1 + value2;
//			}
//			return result;
//		}
//		return "";
	}

	private String getType(String value) {
		value = value.trim().replaceAll(" \n ", "");
		if(value.matches("[a-zA-Z]")) {
			if(value.length() == 1)
				return "letra";
			else if(value.length() > 1)
				return "salita";
			else if(value.matches("true | false | TRUE | FALSE"))
				return "pag-ibig";
		}
		else if(value.matches(".*\\d+.*")) {
			if(value.contains("."))
				return "jumbo-hotdog";
			else
				return "hotdog";
		}
		return "";
	}

	private void getValue() {
		
	}
	public boolean assignment(String text) {
		
		
		
		return false;
	}
}