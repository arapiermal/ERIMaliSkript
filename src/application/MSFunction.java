package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MSFunction {
	MaliSkript f;
	private String kodi;
	private String[] inputVarName;
	private int outNr; //IMPLEMENT
	
	public MSFunction(String kodi) {
		this.kodi = kodi;
		
	}
	public MSFunction(String kodi, String[] inputVarName) {
		this.kodi = kodi;
		this.setInputVarName(inputVarName);
	}
	public MSFunction(String kodi, int outputs) {
		this.kodi = kodi;
		this.outNr = outputs;
	}
	public MSFunction(String kodi, String[] inputVarName, int outputs) {
		this.kodi = kodi;
		this.setInputVarName(inputVarName);
		this.outNr = outputs;
	}
	//hmmmmm
	
	//this would work for a function that doesn't return or have inputs?
	public List<String> ekzekuto() {
		//perdor ekzekuto te MaliSkript, por me ndryshim ?
		f = new MaliSkript(kodi, 1); /////////////////////////////////isFunction
		//variablat lokale te jene mbi ato perjashta?
		f.ekzekuto(f.getKodi());
		return f.getRezultati();
	}
	public List<String> ekzekuto(int[] inputVar) {
		if(inputVar.length != getInputVarName().length) {
			List<String> temp = new LinkedList<>();
			temp.add("ERROR: THERRITET FUNKSIONIN ME " + inputVar.length +" INPUT-E, POR AI KA " + getInputVarName().length);
			return temp;
		}
		//perdor ekzekuto te MaliSkript, por me ndryshim ?
		f = new MaliSkript(kodi, 1);
		//variablat lokale te jene mbi ato perjashta?
		for(int i = 0; i < getInputVarName().length; i++) {
			f.vendosVar(getInputVarName()[i], inputVar[i]);
		}
		f.ekzekuto(f.getKodi());
		return f.getRezultati();
	}
	 
	//KTHE LLOGJIK  in MaliSkript
	////
	public int[] ekzKthe() {
		Object[] ob = f.getArray().get("perKthim").toArray();
		int[] out = new int[ob.length];
		int i = 0;
		for(Object o : ob) {
			out[i++] = (Integer) o;
		}
		return out;
	}
	public String getKodi() {
		return kodi;
	}
	public void setKodi(String kodi) {
		this.kodi = kodi;
	}
	public int getOutNr() {
		return outNr;
	}
	public void setOutNr(int outNr) {
		this.outNr = outNr;
	}
	public String[] getInputVarName() {
		return inputVarName;
	}
	public void setInputVarName(String[] inputVarName) {
		this.inputVarName = inputVarName;
	}
}
