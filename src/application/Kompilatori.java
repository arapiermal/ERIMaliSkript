package application;

import java.util.ArrayDeque;
import java.util.Deque;

class ErrorKompilimi {
	private String mesazhi;
	private int lineNr;

	public ErrorKompilimi(String mesazhi, int lineNr) {
		this.mesazhi = mesazhi;
		this.lineNr = lineNr;
	}

	public String getMesazhi() {
		return mesazhi;
	}

	public int getLineNr() {
		return lineNr;
	}

	public String toString() {
		return mesazhi + " - Rreshti " + lineNr;
	}
}

public class Kompilatori {
	protected static Kompilatori komp = new Kompilatori();
	// Mund te perdoret si queue por edhe si stack
	private Deque<ErrorKompilimi> erroret;

	public Kompilatori() {
		erroret = new ArrayDeque<>();
	}

	public void reportoError(String mesazhi, int lineNr) {
		erroret.offer(new ErrorKompilimi(mesazhi, lineNr));
	}

	public void reportoError(Exception e, int lineNr) {
		erroret.offer(new ErrorKompilimi(e.getMessage(), lineNr));
	}

	public String zbrazErroret() {
		StringBuilder sb = new StringBuilder();
		int i = 1;
		while (!erroret.isEmpty()) {
			sb.append(i++).append(". ").append(erroret.poll().toString()).append("\n"); 
			// Si Queue, FIFO
		}
		return sb.toString();
	}

	public String zbrazErroretMbrapsht() {
		StringBuilder sb = new StringBuilder();
		int i = 1;
		while (!erroret.isEmpty()) {
			sb.append(i++).append(". ").append(erroret.pollLast().toString()).append("\n"); 
			// Si Stack, LIFO
		}
		return sb.toString();
	}
	public String erroriPare() {
		return erroret.poll().toString(); // Si Queue, FIFO 
	}
	public String erroriFundit() {
		return erroret.pollLast().toString(); // Si Stack, LIFO
	}

	public boolean kaErrore() {
		if (erroret.size() > 0)
			return true;
		return false;
	}
}
