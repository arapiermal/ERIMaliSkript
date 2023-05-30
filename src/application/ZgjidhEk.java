package application;

import java.util.Map;
import java.util.Stack;

public class ZgjidhEk {
	//Suport per array jo ende
	// . for Double?
	public static int vepro(String ekuacion, Map<String, Integer> var) {
		Stack<Integer> numra = new Stack<>();
		Stack<Character> operator = new Stack<>();

		for (int i = 0; i < ekuacion.length(); i++) {
			char c = ekuacion.charAt(i);
			if (Character.isWhitespace(c)) {
				continue; // Hapesirat bosh behen skip
			} else if (Character.isDigit(c)) {
				// put into separate method to be used elsewhere?
				int num = 0;
				while (i < ekuacion.length() && Character.isDigit(ekuacion.charAt(i))) {
					num = num * 10 + (ekuacion.charAt(i) - '0');
					i++;
				}
				i--;
				numra.push(num);
			} else if (Character.isLetter(c)) {
				StringBuilder varName = new StringBuilder();
				while (i < ekuacion.length() && Character.isLetterOrDigit(ekuacion.charAt(i))) {
					varName.append(ekuacion.charAt(i));
					i++;
				}
				i--;
				int value = var.get(varName.toString());
				numra.push(value);
			}
			// ( [ {, brackets not fully functional
			else if (c == '(' || c == '[' || c == '{') {
				operator.push(c);
			} else if (c == ')') {
				while (!operator.empty() && operator.peek() != '(') {
					int result = aplikoOperator(numra.pop(), numra.pop(), operator.pop());
					numra.push(result);
				}
				operator.pop(); // remove the '('
			}
			// fix it
			else if (c == ']') {
				while (!operator.empty() && operator.peek() != '[') {
					int result = aplikoOperator(numra.pop(), numra.pop(), operator.pop());
					numra.push(result);
				}
				operator.pop();
			} else if (c == '}') {
				while (!operator.empty() && operator.peek() != '{') {
					int result = aplikoOperator(numra.pop(), numra.pop(), operator.pop());
					numra.push(result);
				}
				operator.pop();
			} else if (eshteOperator(c)) {
				while (!operator.empty() && kaPrecedence(c, operator.peek())) {
					int result = aplikoOperator(numra.pop(), numra.pop(), operator.pop());
					numra.push(result);
				}
				operator.push(c);
			} else {
				throw new IllegalArgumentException("Karakter i pavlefshem: " + c);
			}
		}
		//
		while (!operator.empty() && !eshteKllapeHapese(operator.peek())) {
			int result = aplikoOperator(numra.pop(), numra.pop(), operator.pop());
			numra.push(result);
		}

		return numra.pop();
	}

	private static boolean eshteKllapeHapese(char c) {
		return c == '{' || c == '[' || c == '(';
	}

	private static boolean eshteOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^' || c == '_';
	}

	private static boolean kaPrecedence(char op1, char op2) {
		// change
		if (op2 == '(' || op2 == ')' || op2 == '[' || op2 == ']' || op2 == '{' || op2 == '}') {
			return false;
		}
		if ((op1 == '*' || op1 == '/' || op1 == '%' || op1 == '^' || op1 == '_') && (op2 == '+' || op2 == '-')) {
			return false;
		}
		// ^ per fuqine, _ per rrenjen (ndoshta ndrysho)
		if ((op1 == '^' || op1 == '_')&& (op2 == '+' || op2 == '-' || op2 == '*' || op2 == '/' || op2 == '%')) {
			return true;
		}
		return true;
	}

	private static int aplikoOperator(int b, int a, char op) {
		switch (op) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case '*':
			return a * b;
		case '/':
			return a / b;
		case '%':
			return a % b;
		// fuqi
		case '^':
			return (int) Math.pow(a, b);
		//rrenja a-th root of b, a _ b, KUJDES, jo shume precize
		case '_':
			if(b < 0)
				return 0;
			return (int) Math.round(Math.pow(b, 1.0/a));
		default:
			throw new IllegalArgumentException("Operator i pavlefshem: " + op);
		}
	}

}
