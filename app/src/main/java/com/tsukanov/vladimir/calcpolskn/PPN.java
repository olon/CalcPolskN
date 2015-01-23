package com.tsukanov.vladimir.calcpolskn;

import java.util.LinkedList;

public class PPN {
	private static float memoryAdd = 0f;

	static float getMemoryAdd() {
		return memoryAdd;
	}
	
	static void setMemoryAdd(float mMemoryAdd) {
		memoryAdd = mMemoryAdd;
	}
	
	static boolean isDelim(char c) {
	  return c == ' ';
	}
		
	static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^';
	}
	      
	static int priority(char op) {
		switch (op) {
		case '+':
		case '-':
			return 1;
		case '*':
		case '/':
		case '%':
			return 2;
		case '^':
			return 3; 
		default:
			return -1;
		}
	}
		
	static void processOperator(LinkedList<Float> st, char op) {
		Float r = st.removeLast();
		Float l = st.removeLast();
		switch (op) {
			case '+':
				st.add(l + r);
				break;
			case '-':
				st.add(l - r);
				break;
	        case '*':
	        	st.add(l * r);
	        	break;
	        case '/':
	        	st.add(l / r);
	        	break;
	        case '%':
	        	st.add(l % r);
	        	break;
	        case '^':
	        	st.add((float) Math.pow(l, r));
	        	break;
		}
	}
	        
	public static String eval(String s) {
		LinkedList<Float> st = new LinkedList<Float>();
		LinkedList<Character> op = new LinkedList<Character>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isDelim(c))
				continue;
			if (c == '(')
	            op.add('(');
			else if (c == ')') {
				while (op.getLast() != '(')
					processOperator(st,op.removeLast());
	            op.removeLast();
			} 
			else if (isOperator(c) && !(i == 0 && s.charAt(0) == '-' && s.charAt(1) != '(') &&
	        		  !(s.length()>1 && c == '-' && s.charAt(i-1) == '(')) {
	            while (!op.isEmpty() && priority(op.getLast()) >= priority(c))
	            	processOperator(st, op.removeLast());
	            op.add(c);      
			} 
			else {
				String operand = "";
	            while (i < s.length() && (Character.isDigit(s.charAt(i))|| s.charAt(i) == '.' ||
	            		(i == 0 && s.charAt(0) == '-')||
	            		(s.length()>1 &&s.charAt(i) == '-' && s.charAt(i-1) == '('))) 
	            	operand += s.charAt(i++);
	            if (operand.charAt(0) == '-' && operand.charAt(1) == '0') operand = "0";
	            --i;
	            st.add((float) Float.parseFloat(operand));
			}
		}    
		while (!op.isEmpty())
			processOperator(st, op.removeLast());
		setMemoryAdd(st.getFirst());
		return st.toString();
	}
}
