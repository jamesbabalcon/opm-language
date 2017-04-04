package opmlanguage;

import java.io.IOException;
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
			
			//comment
			if(line.split(" ")[0].equals("--"))
				System.out.println("Comment: " + line.substring(3));
			if(line.split(" ")[0].equals("-*")){
				System.out.print("Comment: ");
				comment(tokens, x);
			}
			
			//branch statements
			if(line.split(" ")[0].equals("kung-ako-nalang-sana")) 
				x = branch(tokens, x, line.split(" ")[0]);
			
			if(type(line.split(" ")[0])) {
				if(declaration(line))
					continue;
				else {
					System.out.println("Syntax Error at line " + (x + 1));
					return false;
				}
			}
			if(line.split(" ")[0].equals("sabihin-mo-na")) {
				if(print(line))
					continue;
				else
					return false;
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
	
	private int branch(String[] tokens, int x, String condition) {
		
		boolean condi = false;
		
		String line = tokens[x].replace("\n", "");
		String[] arr = line.split(" ");
		//System.out.println(x + " " + arr[0]);
		if(condition.equals("tuldok")){
			System.out.println("end");
			System.out.println();
			matchStack.pop();
		}
		else if(arr[0].equals("kung-ako-nalang-sana")){
			matchStack.push("kung-ako-nalang-sana");
			System.out.print("if " + arr[1] + " " + arr[2] + " " + arr[3]);
			
			condi = getCondition(arr[1], arr[2], arr[3]);
			if(condi == true)
				System.out.println(" ----->true");
			else
				System.out.println(" ----->false");
			
			System.out.println("	Execute Things");
			
			x++;
			String line2 = tokens[x].replace("\n", "");
			String[] arr2 = line2.split(" ");
			
			x = branch(tokens, x, arr2[0]);
		}
		else if(arr[0].equals("humanap-ka-ng-panget")){
			System.out.print("else if " + arr[1] + " " + arr[2] + " " + arr[3]);
			
			condi = getCondition(arr[1], arr[2], arr[3]);
			if(condi == true)
				System.out.println(" ----->true");
			else
				System.out.println(" ----->false");
			
			System.out.println("	Execute Things");
			
			x++;
			String line2 = tokens[x].replace("\n", "");
			String[] arr2 = line2.split(" ");
			
			x = branch(tokens, x, arr2[0]);
		}
		else if(arr[0].equals("only-hope")){
			System.out.println("else");
			
			System.out.println("	Execute Things");
			
			x++;
			String line2 = tokens[x].replace("\n", "");
			String[] arr2 = line2.split(" ");
			
			x = branch(tokens, x, arr[0]);
		}
		else{
			x++;
			String line2 = tokens[x].replace("\n", "");
			String[] arr2 = line2.split(" ");
			x = branch(tokens, x, arr[0]);
		}
		
		return x;
	}
	
	private boolean getCondition(String a, String relay, String b) {
		// TODO Auto-generated method stub
		
		for(Variable v : variables) {
			if(a.equals(v.getName())) {
				for(Variable v2 : variables) {
					if(b.equals(v2.getName())) {
						if(relay.equals("==")){
							if(v.getInitValue().equals(v2.getInitValue()))
								return true;
						}
						else if(relay.equals("!=")){
							if(!(v.getInitValue().equals(v2.getInitValue())))
								return true;
						}
						else if(relay.equals(">") || relay.equals(">=") || relay.equals("<") || relay.equals("<=")){
							try{
								int k = Integer.parseInt(v.getInitValue());
								int l = Integer.parseInt(v2.getInitValue());
								if(relay.equals(">") && k > l)
									return true;
								else if(relay.equals("<") && k < l)
									return true;
								else if(relay.equals(">=") && k >= l)
									return true;
								else if(relay.equals("<=") && k <= l)
									return true;
							}catch(IllegalArgumentException e){
								return false;
							}
						}
					}
				}
				if(b.equals(b)) {
					if(relay.equals("==")){
						if(v.getInitValue().equals(b))
							return true;
					}
					else if(relay.equals("!=")){
						if(!(v.getInitValue().equals(b)))
							return true;
					}
					else if(relay.equals(">") || relay.equals(">=") || relay.equals("<") || relay.equals("<=")){
						try{
							int k = Integer.parseInt(v.getInitValue());
							int l = Integer.parseInt(b);
							if(relay.equals(">") && k > l)
								return true;
							else if(relay.equals("<") && k < l)
								return true;
							else if(relay.equals(">=") && k >= l)
								return true;
							else if(relay.equals("<=") && k <= l)
								return true;
						}catch(IllegalArgumentException e){
							return false;
						}
					}
				}
			}
		}
		
		for(Variable v2 : variables) {
			if(b.equals(v2.getName())) {					
				if(b.equals(v2.getName())) {
					if(relay.equals("==")){
						if(a.equals(v2.getInitValue()))
							return true;
					}
					else if(relay.equals("!=")){
						if(!(a.equals(v2.getInitValue())))
							return true;
					}
					else if(relay.equals(">") || relay.equals(">=") || relay.equals("<") || relay.equals("<=")){
						try{
							int k = Integer.parseInt(a);
							int l = Integer.parseInt(v2.getInitValue());
							if(relay.equals(">") && k > l)
								return true;
							else if(relay.equals("<") && k < l)
								return true;
							else if(relay.equals(">=") && k >= l)
								return true;
							else if(relay.equals("<=") && k <= l)
								return true;
						}catch(IllegalArgumentException e){
							return false;
						}
					}
				}	
			}
		}
		if(relay.equals("==")){
			if(a.equals(b))
				return true;
		}
		else if(relay.equals("!=")){
			if(!(a.equals(b)))
				return true;
		}
		else if(relay.equals(">") || relay.equals(">=") || relay.equals("<") || relay.equals("<=")){
			try{
				int k = Integer.parseInt(a);
				int l = Integer.parseInt(b);
				if(relay.equals(">") && k > l)
					return true;
				else if(relay.equals("<") && k < l)
					return true;
				else if(relay.equals(">=") && k >= l)
					return true;
				else if(relay.equals("<=") && k <= l)
					return true;
			}catch(IllegalArgumentException e){
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
	
	private void comment(String[] tokens, int x) {

		boolean endComment = false;
		
		for(; endComment != true; x++) {
			String line = tokens[x].replace("\n", "").replace("\r", "");
			
			String[] arr = line.split(" ");
			
			for(int i = 0 ; i < arr.length; i++){
				if(arr[i].equals("*-")){
					endComment = true;
					break;
				}
				else if(arr[i].equals("-*"))
					continue;
				else
					System.out.print(arr[i] + " ");
			}
			
		}
		System.out.println();
		System.out.println();

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
                                                                                                         
//			}
//		}
//		
//		return false;
//	}
	
	public boolean assignment(String text) {
		
		
		
		return false;
	}
	
	public boolean print(String text) {
		
		String[] tokens = text.split(" ");
		Stack<Integer> check = new Stack<Integer>();
		
		System.out.println(text);
		
		if(!tokens[0].equals("sabihin-mo-na")) {
			if(!tokens[1].equals("(") && !tokens[tokens.length - 1].equals(")")) {
				System.out.println("invalid print format");
				return false;
			}
		}
		
		for(int x = 0; x < tokens.length; x++) {
			if(tokens[x].equals("\"") && check.isEmpty()) {
				check.push(1);
				System.out.print(tokens[x]);
			}
			else if(tokens[x].equals("\"") && !check.isEmpty()) {
				check.pop();
			}
			
			if(check.isEmpty())
				return true;
		}
		
		return false;
	}
}