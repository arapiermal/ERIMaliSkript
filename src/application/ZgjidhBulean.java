package application;

import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class ZgjidhBulean {
	//TESTING
	public static void main(String[] args) {
		Map<String, Integer> variables = new HashMap<>();
		variables.put("a", 25);
		variables.put("b", 0);

		String input = "a == 5^2 | b == 0"; //TRUE
		try {
			boolean result = ZgjidhBulean.vepro(input, variables);
			System.out.println(result); // more than 2 --> problem 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static boolean vepro(String input, Map<String, Integer> variabla) throws Exception {
		Stack<Character> stack = new Stack<>();
		String[] output = input.split("([&|!])");
		
		for (int i = 0; i < output.length; i++) {
			output[i] = output[i].trim();
			if (eshteOperatorBulean(output[i].charAt(0))) {
				stack.push(output[i].charAt(0));
			} else if (output[i].charAt(0) == '!') {
				stack.push(!krahaso(output[i + 1], variabla) ? 't' : 'f');
				i++;
			} else {
				stack.push(krahaso(output[i], variabla) ? 't' : 'f');

			}
		}
		while (!stack.isEmpty() && stack.size() >= 3) {
			///////////////////
			//a < 5 & b == 0 & 4 > 5
			//a < 5 & f
			System.out.println(stack);
			stack.push(aplikoOperatorin(stack.pop(), stack.pop(), stack.pop()) ? 't' : 'f');

		}

		return stack.pop() == 't';
	}

	public static boolean krahaso(String input, Map<String, Integer> variabla) {
		Stack<Integer> stack = new Stack<>();
		String[] output = input.split("(?<=!=|==|<=|>=|<>|<|>)|(?=!=|==|<=|>=|<>|<|>)");
		boolean temp = false;
		// krahasimi vetem ndermjet 2
		if(output.length == 3) {
			temp = krahaso(ZgjidhEk.vepro(output[0], variabla), output[1], ZgjidhEk.vepro(output[2], variabla));
		}
		/*for (int i = 0; i < output.length; i++) {
			if (eshteOperatorKrahasimi(output[i])) {
				temp = temp && krahaso(ZgjidhEk.vepro(output[i-1], variabla), ZgjidhEk.vepro(output[i+1], variabla), output[i]);
				i++;
			} else {
				stack.push(ZgjidhEk.vepro(output[i], variabla));
			}
		}*/
		return temp;
	}

	private static boolean eshteOperatorBulean(char c) {
		return c == '&' || c == '|';
	}

	/*
	 * private static boolean kaPrecedence(char op1, char op2) { if (op2 == '(' ||
	 * op2 == ')') { return false; } if ((op1 == '&' || op1 == '|') && (op2 == '!'))
	 * { return false; } return true; }
	 */
	private static boolean aplikoOperatorin(char b, char op, char a) {
		switch (op) {
		case '&':
			return a == 't' && b == 't';
		case '|':
			return a == 't' || b == 't';
		default:
			throw new IllegalArgumentException("Operator i pavlefshem: " + op);
		}
	}

	private static boolean krahaso(int a, String op, int b) {
		switch (op) {
		case "<":
			return a < b;
		case ">":
			return a > b;
		case "<=":
			return a <= b;
		case ">=":
			return a >= b;
		case "==":
			return a == b;
		case "<>":
			return a != b;
		// case "!=": // ! hiqet nga lart
		// return a!= b;
		default:
			throw new IllegalArgumentException("Operator i pavlefshem: " + op);
		}
	}
}
