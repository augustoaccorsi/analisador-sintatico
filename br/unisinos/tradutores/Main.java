package br.unisinos.tradutores;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	static int identifierCount = 0;
	static Map<String, Integer> identifiers = new HashMap<String, Integer>();

	public static void main(String[] args) throws IOException {
		Reader reader = readFile("./br/unisinos//tradutores//InputCode.txt");
		BufferedReader br = readFile("./br/unisinos//tradutores//ReservedWords.csv");
		
		String line = null;
		Token token = null;
		List<Token> tokenList = new ArrayList<Token>();
		List<String> reservedWords = new ArrayList<String>();
		
		while ((line = br.readLine()) != null) {
			reservedWords.add(line.replace(",", ""));
		}

		StreamTokenizer st = new StreamTokenizer(reader);
		st.slashSlashComments(true);
		st.slashStarComments(true);
		st.eolIsSignificant(true);
		st.ordinaryChar('/');
		int currentToken = st.nextToken();
		while (currentToken != StreamTokenizer.TT_EOF) {
			token = findToken(token, reservedWords, st, currentToken);
			if (token != null) {
			//	System.out.print(token.toString());
				token.setLine(st.lineno());
				tokenList.add(token);
				token = null;
			}

			currentToken = st.nextToken();
		}
		
		SintaticalAnalyses sintatical = new SintaticalAnalyses(tokenList, reservedWords);
	
		
		//aqui teremos os tokens 
	}

	private static Token findToken(Token token, List<String> reservedWords, StreamTokenizer st, int currentToken)
			throws IOException {
		switch (currentToken) {
		case StreamTokenizer.TT_NUMBER:
			token = new Token(Types.num, String.valueOf(st.nval));
			break;
		case StreamTokenizer.TT_WORD:
			String word = st.sval;
			if (reservedWords.contains(word))
				token = new Token(Types.reserved_word, word);
			else {
				token = getIdentifier(word);
			}
			break;
		case '+':
		case '-':
		case '/':
		case '*':
			token = new Token(Types.Arith_Op, Character.toString((char) currentToken));
			break;
		case '[':
			token = new Token(Types.l_bracket, Character.toString((char) currentToken));
			break;
		case ']':
			token = new Token(Types.r_bracket, Character.toString((char) currentToken));
			break;
		case '{':
			token = new Token(Types.l_braces, Character.toString((char) currentToken));
			break;
		case '}':
			token = new Token(Types.r_braces, Character.toString((char) currentToken));
			break;
		case '(':
			token = new Token(Types.l_paren, Character.toString((char) currentToken));
			break;
		case ')':
			token = new Token(Types.r_paren, Character.toString((char) currentToken));
			break;
		case ',':
			token = new Token(Types.comma, Character.toString((char) currentToken));
			break;
		case ';':
			token = new Token(Types.semicolon, Character.toString((char) currentToken));
			break;
		case '<': {
			int t = st.nextToken();
			switch (t) {
			case '=':
				token = new Token(Types.Relational_Op, "<=");
				break;
			case '<':
				token = new Token(Types.Relational_Op, "<<");
				break;
			default:
				st.pushBack();
				token = new Token(Types.Relational_Op, "<");
				break;
			}
			break;
		}
		case '=': {
			int t = st.nextToken();
			switch (t) {
			case '=':
				token = new Token(Types.Relational_Op, "==");
				break;
			default:
				st.pushBack();
				token = new Token(Types.equal, Character.toString((char) currentToken));
				break;
			}
			break;
		}
		case '>': {
			int t = st.nextToken();
			switch (t) {
			case '=':
				token = new Token(Types.Relational_Op, ">=");
				break;
			case '>':
				token = new Token(Types.Relational_Op, ">>");
				break;
			default:
				st.pushBack();
				token = new Token(Types.Relational_Op, ">");
				break;
			}
			break;
		}
		case '!': {
			int t = st.nextToken();
			switch (t) {
			case '=':
				token = new Token(Types.Relational_Op, "!=");
				break;
			default:
				st.pushBack();
				break;
			}
			break;
		}
		case '&': {
			int t = st.nextToken();
			switch (t) {
			case '&':
				token = new Token(Types.logic_op, "&&");
				break;
			default:
				st.pushBack();
				token = new Token(Types.Relational_Op, "&");
				break;
			}
		}
		case '|': {
			int t = st.nextToken();
			switch (t) {
			case '|':
				token = new Token(Types.logic_op, "||");
				break;
			default:
				st.pushBack();
				break;
			}
			break;
		}
		case '"': {
			String value = st.sval;
			token = new Token(Types.string_literal, value);
			break;
		}
		}
		return token;
	}

	private static Token getIdentifier(String word) {
		if (identifiers.get(word) != null) {
			return new Token(Types.Id, String.valueOf(identifiers.get(word)));
		} else {
			identifierCount++;
			identifiers.put(word, identifierCount);
			return new Token(Types.Id, String.valueOf(identifierCount));
		}
	}

	private static BufferedReader readFile(String path) throws FileNotFoundException {
		return new BufferedReader(new FileReader(path));
	}
}