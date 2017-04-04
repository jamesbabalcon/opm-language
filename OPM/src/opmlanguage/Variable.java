package opmlanguage;

public class Variable {
	
	private String name = "";
	private String type = "";
	private String initValue = "";
	
	private int intValue;
	private float floatValue;
	private String stringValue;
	private char charValue;
	private boolean booleanValue;
	
	public Variable(String type, String name, String initValue) {
		this.setType(type);
		this.setName(name);
		this.setInitValue(initValue);
	}
	
	public Variable(String type, String name) {
		this.setType(type);
		this.setName(name);
	}
	
	public void setValue() {
		if(type.equals("hotdog")) {
			intValue = Integer.parseInt(initValue);
		}
		else if(type.equals("jumbo-hotdog")) {
			floatValue = Float.parseFloat("initValue");
		}
		else if(type.equals("salita")) {
			stringValue = initValue;
		}
		else if(type.equals("letra")) {
			charValue = initValue.charAt(0);
		}
		else if(type.equals("pag-ibig")) {
			if(initValue.equals("true")) {
				booleanValue = true;
			}
			else if(initValue.equals("false")) {
				booleanValue = false;
			}
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInitValue() {
		return initValue;
	}

	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public char getCharValue() {
		return charValue;
	}
	
	public boolean isBooleanValue() {
		return booleanValue;
	}
}
