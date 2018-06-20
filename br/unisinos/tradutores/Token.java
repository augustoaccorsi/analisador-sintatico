package br.unisinos.tradutores;

public class Token {
	private Types type;
	private String value;
	private int line;
	
	public Token(Types type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public String toString() {
		return "["+type+", "+value+"]";
	}

	public Types getType() {
		return type;
	}

	public void setType(Types type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}	
}
