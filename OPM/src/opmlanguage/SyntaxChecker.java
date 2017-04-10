package opmlanguage;

import java.util.ArrayList;
import java.util.Stack;

import view.OPM;

public class SyntaxChecker {

	private Stack<String> matchStack;
	private ArrayList<Variable> variables;
	private ArrayList<String> reserved;
	private OPM opm;
	
	public SyntaxChecker(String text, OPM opm) {
		this.opm = opm;
		
		matchStack = new Stack<String>();
		variables = new ArrayList<Variable>();
		reserved = new ArrayList<String>();
		
		addReserved();
				
		if(valid(text) == true) {
			opm.setConsoleText("Code is valid");
		}
		else {
			opm.setConsoleText("Code is invalid");
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
			String line = tokens[x].trim().replaceAll(" \n ", "");
			
			if(line.equals("anak")) {
				matchStack.push("anak");
				opm.setConsoleText("Pushing to Stack");
			}
			if(line.equals("tuldok") && !matchStack.isEmpty()) {
				matchStack.pop();
				opm.setConsoleText("Popping from Stack");
			}
			else if(line.equals("tuldok") && matchStack.isEmpty()) {
				opm.setConsoleText("Unexpected EOF");
				return false;
			}
			
			//comment
			if(line.split(" ")[0].equals("--"))
				opm.setConsoleText("Comment: " + line.substring(3));
			if(line.split(" ")[0].equals("-*")){
				System.out.print("Comment: ");
				comment(tokens, x);
			}
			
			//branch statements
			if(line.split(" ")[0].equals("kung-ako-nalang-sana")) 
				x = branch(tokens, x, line.split(" ")[0]);
			
			//loop statements
			if(line.split(" ")[0].equals("hanggang-kailan"))
				x = whileLoop(tokens, x);
			
			if(line.split(" ")[0].equals("doo-bidoo"))
				x = dowhile(tokens, x);
	
			if(type(line.split(" ")[0])) {
				if(declaration(line))
					continue;
				else {
					opm.setConsoleText("Syntax Error at line " + (x + 1));
					return false;
				}
			}
			
			//if assignment + operation; or assignment alone
			if(!type(line.split(" ")[0]) && line.contains("=")) {
				if(containsOperator(line)) {
					for(Variable v : variables) {
						if(v.getName().equals(line.split(" =")[0])) {
							boolean valid = operation(v, line);
							if(!valid)
								opm.setConsoleText("Syntax error at line " + (x+1));
						}
					}
				}
				else {
					if(!assignment(line));
					opm.setConsoleText("Syntax error at line " + (x+1));
				}
			}
			
			
			if(line.split(":")[0].equals("nandito-ako")) {
				if(scan(line)) {
					continue;
				}
				else
					return false;
			}
			if(line.split(":")[0].equals("sabihin-mo-na")) {
				if(print(line))
					continue;
				else
					return false;
			}
			
			if(x == tokens.length - 1 && matchStack.isEmpty()) {
				opm.setConsoleText("End of File, Stack is Empty");
				return true;
			}
			else if(x == tokens.length - 1 && !matchStack.isEmpty()) {
				opm.setConsoleText("End of File, Stack is not Empty");
				return false;
			}
		}
		return false;
	}
	
	private int dowhile(String[] tokens, int x) {

		boolean condi = true;
		String[] arr;
		int y;

		System.out.println("length: " + tokens.length);
		do{
			y = x+1;
			
			do{
				System.out.println("	Execute Things");
				
				y++;
				String line = tokens[y].replace("\n", "");
				arr = line.split(" ");
				
			}while(!arr[0].equals("muling-ibalik"));
			
			condi = getCondition(arr[1], arr[2], arr[3]);
		}while(condi == true);
		
		return y+2;	 
	}

	private int whileLoop(String[] tokens, int x) {
		
		boolean condi = true;
		String[] arr2;
		int y;
		
		String line = tokens[x].replace("\n", "");
		String[] arr = line.split(" ");
		
		condi = getCondition(arr[1], arr[2], arr[3]);
		y = x+1;
		
		while(condi == true){
			y = x+1;
			
			do{
				System.out.println("	Execute Things");
				
				y++;
				String line2 = tokens[y].replace("\n", "");
				arr2 = line2.split(" ");
			
			}while(!arr2[0].equals("tuldok"));
			
			condi = getCondition(arr[1], arr[2], arr[3]);
		}
		
		return y;	 
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
			if(condi == true){
				System.out.println(" ----->true");
				
				System.out.println("	Execute Things");
				
				x++;
				String line2 = tokens[x].replace("\n", "");
				String[] arr2 = line2.split(" ");
				
				while(!arr2[0].equals("tuldok")){
					x++;
					line2 = tokens[x].replace("\n", "");
					arr2 = line2.split(" ");
				}
				
				return x;
			}
			else{
				System.out.println(" ----->false");
				
				x++;
				String line2 = tokens[x].trim().replaceAll(" \n ", "");
				String[] arr2 = line2.split(" ");
				
				while(!arr2[0].equals("tuldok")){
					x++;
					line2 = tokens[x].replace("\n", "");
					arr2 = line2.split(" ");
					
					if(arr2[0].equals("humanap-ka-ng-panget")){
						x = branch(tokens, x, arr2[0]);
						break;
					}
					else if(arr2[0].equals("only-hope")){
						x = branch(tokens, x, arr2[0]);
						break;
					}
				}
			}
		}
		else if(arr[0].equals("humanap-ka-ng-panget")){
			System.out.print("else if " + arr[1] + " " + arr[2] + " " + arr[3]);
			
			condi = getCondition(arr[1], arr[2], arr[3]);
			if(condi == true){
				System.out.println(" ----->true");
				
				System.out.println("	Execute Things");
				
				x++;
				String line2 = tokens[x].replace("\n", "");
				String[] arr2 = line2.split(" ");
				
				while(!arr2[0].equals("tuldok")){
					x++;
					line2 = tokens[x].replace("\n", "");
					arr2 = line2.split(" ");
				}
				
				return x;
			}
			else{
				System.out.println(" ----->false");
				
				x++;
				String line2 = tokens[x].replace("\n", "");
				String[] arr2 = line2.split(" ");
				
				while(!arr2[0].equals("tuldok")){
					x++;
					line2 = tokens[x].replace("\n", "");
					arr2 = line2.split(" ");
					
					if(arr2[0].equals("humanap-ka-ng-panget")){
						x = branch(tokens, x, arr2[0]);
						break;
					}
					else if(arr2[0].equals("only-hope")){
						x = branch(tokens, x, arr2[0]);
						break;
					}
				}
			}
		}
		else if(arr[0].equals("only-hope")){
			System.out.println("else");
			System.out.println("	Execute Things");
			
			x++;
			String line2 = tokens[x].replace("\n", "");
			String[] arr2 = line2.split(" ");
			
			while(!arr2[0].equals("tuldok")){
				x++;
				line2 = tokens[x].replace("\n", "");
				arr2 = line2.split(" ");
			}
			
			return x;
		}
		
		return x;
	}
	
	private boolean getCondition(String a, String relay, String b) {
		
		for(Variable v : variables) {
			if(a.equals(v.getName())) {
				for(Variable v2 : variables) {
					if(b.equals(v2.getName())) {
						if(relay.equals("==")){
							if(v.getInitValue().equals(v2.getInitValue()))
								return true;
						}
						else if(relay.equals("!=")){
							if((v.getInitValue().equals(v2.getInitValue())))
								return false;
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
						if((v.getInitValue().equals(b)))
							return false;
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
						if((a.equals(v2.getInitValue())))
							return false;
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
			if((a.equals(b)))
				return false;
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
		opm.setConsoleText("\n");
		opm.setConsoleText("\n");

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
				opm.setConsoleText("Duplicate variables not allowed");
				return false;
			}
		}
		for(String s : reserved) {
			if(str.equals(s)) {
				opm.setConsoleText("Reserved word");
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
		
		if(text.contains(",")) {
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
		else if(tokens.length == 2) {
			if(type(tokens[0]) && legal(tokens[1])) {
				Variable var = new Variable(tokens[0], tokens[1]);
				opm.setConsoleText("Variable " + var.getName() + " of type " + var.getType() + " created");
				variables.add(var);
				return true;
			}
		}
		else if(tokens.length == 4 && legal(tokens[1])) {
			if(type(tokens[0]) && tokens[2].equals("=") && tokens[0].equals(getType(tokens[3]))) {
				Variable var = new Variable(tokens[0], tokens[1], tokens[3]);
				opm.setConsoleText("Variable " + var.getName() + " of type " + var.getType() + " with value " + tokens[3] + " created");
				variables.add(var);
				return true;
			}
		}
		else if(containsOperator(text)) {
			if(type(tokens[0]) && tokens[2].equals("=")) {
				Variable var = new Variable(tokens[0], tokens[1]);
				variables.add(var);
				return operation(var, text);
			}
		}
		
		return false;
	}
	
	public boolean operation(Variable var, String text) {
		
		if(var.getType().equals("letra") || var.getType().equals("pag-ibig")) {
			return false;
		}
		
		String[] tokens = text.split("= ")[1].split(" ");
		ArrayList<String> operands = new ArrayList<String>();
		ArrayList<String> operators = new ArrayList<String>();
		
		System.out.println(text);
		for(String t : tokens) {
			if(!containsOperator(t))
				operands.add(t);
			else if(containsOperator(t))
				operators.add(t);
		}
		
		if(operators.size() != operands.size() - 1)
			return false;
		
		for(String o : operands) {
			o = o.trim().replaceAll(" \n ", "").replace(" ", "");
			for(Variable v : variables) {
				if(v.getName().equals(o) && v.getType().equals(var.getType()))
					continue;
				else if(!v.getName().equals(o) && var.getType().equals(getType(o)))
					continue;
				else
					return false;
			}
		}

		opm.setConsoleText("Variable " + var.getName() + "is assigned a value of ");
		opm.setConsoleText(text.split("=")[1]);
		return true;
	}

	private String getType(String value) {
		value = value.trim().replaceAll(" \n ", "");
		if(value.contains("\"")) {
			value = value.trim().replace("\"", "");
			return "salita";
		}
		else if(value.contains("'")) {
			value = value.trim().replace("\'", "");
			System.out.println(value);
			if(value.length() == 1)
				return "letra";
		}
		else if(value.matches(".*\\d+.*")) {
			if(value.contains("."))
				return "jumbo-hotdog";
			else
				return "hotdog";
		}
		else if(value.equals("mahal_ko") | value.equals("mahal_ako"))
			return "pag-ibig";
		
		return "";
	}
	
	public Variable getVariable(String name) {
		for(Variable var : variables) {
			if(name.equals(var.getName()))
				return var;
		}
		
		return null;
	}
	
	private boolean assignment(String line) {
		System.out.println("here in assignment: " + line);
		String [] subs = line.split("=");
		String variable = "", value = "";
		String variable_type = "", value_type = "";
		
		for(Variable v : variables) {
			subs[0] = subs[0].trim().replace(" ", "");
			subs[1] = subs[1].trim().replace(" ", "").replace(" \n ", ""); 
			v.setName(v.getName().trim().replace(" \n ", ""));
			if(subs[0].equals(v.getName())) {
				variable = subs[0];
				variable_type = v.getType();
			}
			if(subs[1].equals(v.getName())) {
				value = subs[1];
				value_type = v.getType();
			}
		}
		
		if(!variable.isEmpty()) {
			if(value.isEmpty()) {
				if(getType(subs[1]).equals(variable_type)) {
					value = subs[1];
					value_type = variable_type;
				}
				if(!value.isEmpty() && variable_type.equals(value_type)) {
					opm.setConsoleText("Variable " + variable + " is given a value of "
							+ value);
					return true;
				}
			}		
		}
		return false;		
	}
	
	public boolean print(String text) {
		
		String[] tokens = text.split(":", 2);
//		int check = 0;
		String lex = tokens[1];

		if(isVariable(lex)) {
			opm.setConsoleText("printing " + getVariable(lex).getName());
			return true;
		}
		else if(lex.trim().startsWith("\"") && lex.trim().endsWith("\"")) {
			opm.setConsoleText("printing " + lex);
			return true;
		}
		
		return false;
	}
	
	public boolean scan(String text) {
		
		String[] tokens = text.split(":", 2);
		String[] lex = tokens[1].split(",");
		
		if(isVariable(lex[0].trim())) {
			getVariable(lex[0]).setInitValue(lex[1]);
			return true;
		}
				
		return false;
	}
}