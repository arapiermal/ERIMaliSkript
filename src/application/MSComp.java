package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MSComp {

	public static boolean njejtaVlere(Object a, Object b) {
		return a.equals(b);
	}

	public static int krahaso(int a, int b) {
		if (a > b)
			return 1;
		else if (a < b)
			return -1;
		return 0;
	}

	public static boolean eshteOperatorMeBaraz(String op) {
		if (op.length() == 2)
			if (op.charAt(1) == '=') {
				switch (op.charAt(0)) {
				case '+':
					return true;
				case '-':
					return true;
				case '*':
					return true;
				case '/':
					return true;
				case '%':
					return true;
				case '^':
					return true;
				}
			}

		return false;
	}
	public static int stringToInt(String s) {        
        try {
        	int nr = Integer.parseInt(s);
        	return nr;
        } catch (NumberFormatException e) {
        	return Integer.MIN_VALUE;
        }
    }
	
	public static List<Integer> bashkoArrMblidh(List<Integer> l1, List<Integer> l2) {
		if(l1.isEmpty() || l2.isEmpty() || (l1.size() != l2.size()))
			return null;
		List<Integer> shuma = new ArrayList<>();
		for(int i = 0; i < l1.size(); i ++) {
			shuma.add(l1.get(i) + l2.get(i));
		}
		return shuma;
	}
	public static List<Integer> bashkoArrZbrit(List<Integer> l1, List<Integer> l2) {
		if(l1.isEmpty() || l2.isEmpty() || (l1.size() != l2.size()))
			return null;
		List<Integer> dif = new ArrayList<>();
		for(int i = 0; i < l1.size(); i ++) {
			dif.add(l1.get(i) - l2.get(i));
		}
		return dif;
	}
	public static int shumaArr(List<Integer> l) {
		int shuma = 0;
		for(int a : l) {
			shuma += a;
		}
		return shuma;
	}
	
}
