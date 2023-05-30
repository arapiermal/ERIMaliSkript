package application;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//serializable ruaj file, lexo?
public class MyTabela {
	/*
	 * psh shtohet mosha ID EMRI MBIEMRI MOSHA 1 Ermal Arapi null 2 Arjon Arapi null
	 */
	// private String emri;
	private Map<String, MyTipeTeDhenash> kolona;
	private List<Map<String, Object>> rreshta;

	public MyTabela() {
		// pasi kishte problem qe hash function kur vendoste element tjeter ndryshon
		// rradha
		this.kolona = new LinkedHashMap<>(); // Ka eficiencen e nje HashMap-- dhe radhitjen e nje LinkedList

		this.rreshta = new LinkedList<>(); // Rradhit me shpejt

	}

	public MyTabela(Map<String, MyTipeTeDhenash> kolona, List<Map<String, Object>> rreshta) {
		this.setKolona(kolona);
		this.rreshta = rreshta;
	}
	
	public MyTabela(Object[][] o) {
		this.kolona = new LinkedHashMap<>();
		this.rreshta = new LinkedList<>();
		for(int i = 0; i < o[0].length; i++) {
			shtoKolone((String) o[0][i], MyTipeTeDhenash.merrTipin(o[1][i]));
			
		}
		for(int r = 1; r < o.length; r++) {
			shtoRresht(o[r]);
		}
	}

	// shtoDisaKolona
	public void shtoKolone(String emri, MyTipeTeDhenash tipi) {
		if (getKolona().containsKey(emri)) {
			throw new IllegalArgumentException("Ekziston kolone me emrin: " + emri);
		}
		getKolona().put(emri, tipi);
		for (Map<String, Object> rresht : rreshta) {
			rresht.put(emri, null); // Po shtojme vlera bosh ne fund te cdo rreshti
		}
	}

	/////////////////////////////////
	public void shtoDisaKolona(String[] emri, MyTipeTeDhenash tipi) {

	}

	public void hiqKolone(String emri) {
		if (kolona.containsKey(emri)) {
			kolona.remove(emri);
			for (Map<String, Object> rresht : rreshta) {
				rresht.remove(emri); // Po heqim vleren e kolones nga cdo rresht
			}
		}
	}

	public void shtoRresht(Object[] o) {
		if (o.length == getKolona().size()) {
			Map<String, Object> rresht = new LinkedHashMap<>();
			int i = 0;
			for (String emerKolone : getKolona().keySet()) {
				if (getKolona().get(emerKolone).perputhje(o[i]))
					rresht.put(emerKolone, o[i++]);
				else
					throw new IllegalArgumentException("Tipi nuk perputhet me kolonen");
			}
			rreshta.add(rresht);
		} else
			throw new IllegalArgumentException("Nuk perputhen numri i te dhenave me kolonat");
	}

	//
	public void shtoDisaRreshta(Object[][] o) {
		Map<String, Object> rresht = new LinkedHashMap<>();
		for (int i = 0; i < o.length; i++) {
			if (o[i].length == getKolona().size()) {
				rresht.clear();
				int j = 0;
				for (String emerKolone : getKolona().keySet()) {
					if (getKolona().get(emerKolone).perputhje(o[i][j]))
						rresht.put(emerKolone, o[i][j++]);
					else
						throw new IllegalArgumentException("Tipi nuk perputhet me kolonen");
				}
				rreshta.add(new LinkedHashMap<>(rresht));
			} else {
				throw new IllegalArgumentException("Nuk perputhen numri i te dhenave me kolonat");
			}
		}
	}

	public void hiqRresht(int i) {
		if (i < 0 || i >= rreshta.size())
			throw new IllegalArgumentException("Indeks i palejuar");
		rreshta.remove(i);
	}

	// Si SELECT * FROM thistable WHERE emri = Ermal
	public List<Map<String, Object>> kerkoRreshta(String kolone, Object vlera) {
		if (!getKolona().containsKey(kolone)) {
			throw new IllegalArgumentException("Kjo kolone nuk ekziston");
		}
		List<Map<String, Object>> rezultati = new LinkedList<>(); // ArrayList? Si e duam rezultatin
		for (Map<String, Object> rresht : rreshta) {
			if (vlera.equals(rresht.get(kolone))) {
				rezultati.add(rresht);
			}
		}
		return rezultati;

	}

	// DOUBLE?!?
	public Integer shumeKolone(String emriKol) {
		int shuma = 0;
		if (getKolona().get(emriKol) == MyTipeTeDhenash.INT) {
			for (Map<String, Object> rresht : rreshta) {
				shuma += (Integer) rresht.get(emriKol);
			}
		}
		// Ndoshta per double
		return shuma;
	}

	// toString(int i)
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String emerKolone : getKolona().keySet()) {
			sb.append(emerKolone).append("\t");
		}
		sb.append("\n");
		for (Map<String, Object> rresht : rreshta) {
			/*
			 * for(Object o : rresht.values()) { sb.append(o.toString()).append("\t"); }
			 */
			for (String e : rresht.keySet()) {
				sb.append(rresht.get(e).toString()).append("\t");
			}
			sb.append("\n");

		}
		return sb.toString();
	}

	public String toString(String lloji) {
		StringBuilder sb = new StringBuilder();
		if (lloji.equals("CSV")) {
			for (String k : kolona.keySet()) {
				sb.append(k).append(",");
			}
			sb.deleteCharAt(sb.length() - 1); // HEQIM PRESJEN E FUNDIT
			sb.append("\n");
			for (Map<String, Object> rreshti : rreshta) {
				for (Object o : rreshti.values()) {
					sb.append(o.toString()).append(",");
				}
				sb.deleteCharAt(sb.length() - 1); // HEQIM PRESJEN E FUNDIT
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public String[][] toTable() {
		String[][] tabele = new String[rreshta.size() + 1][getKolona().size()];
		int i = 0;
		for (String emerKolone : getKolona().keySet()) {
			tabele[0][i++] = emerKolone;
		}
		i = 1;
		for (Map<String, Object> rresht : rreshta) {
			int j = 0;
			for (String e : rresht.keySet()) {
				var tmp = rresht.get(e);
				// Kthejme toString-un e Objektit ose bosh nqs eshte null
				tabele[i][j++] = (tmp != null) ? tmp.toString() : "";
				// tabele[i][j++] = rresht.get(e).toString();
			}
			i++;
		}

		return tabele;
	}

	//
	public String[][] toTable(List<Map<String, Object>> listRreshta) {
		String[][] tabele = new String[listRreshta.size() + 1][getKolona().size()];
		int i = 0;
		for (String emerKolone : getKolona().keySet()) {
			tabele[0][i++] = emerKolone;
		}
		i = 1;
		for (Map<String, Object> rresht : listRreshta) {
			int j = 0;
			for (String e : rresht.keySet()) {
				tabele[i][j++] = rresht.get(e).toString(); // Kthejme toString-un e Objektit
			}
			i++;
		}

		return tabele;
	}

	// LISTKOLONA MIGHT BE REDUNDANT, OTHER METHOD RETURNS LISTRRESHTA
	public String[][] toTable(List<Map<String, Object>> listRreshta, Map<String, MyTipeTeDhenash> listKolona) {
		String[][] tabele = new String[listRreshta.size() + 1][listKolona.size()];
		int i = 0;
		for (String emerKolone : listKolona.keySet()) {
			tabele[0][i++] = emerKolone;
		}
		i = 1;
		for (Map<String, Object> rresht : listRreshta) {
			int j = 0;
			// Ketu shikojme keyset te kolonave
			for (String e : listKolona.keySet()) {
				tabele[i][j++] = rresht.get(e).toString(); // Kthejme toString-un e Objektit
			}
			i++;
		}

		return tabele;
	}
	
	public String[][] toTableFromKolona(List<String> kol){
		String[][] tab = new String[rreshta.size() + 1][kol.size()];
		int i = 0;
		for(String k : kol) {
			tab[0][i++] = k;
		}
		i = 1;
		for(Map<String, Object> rr : rreshta) {
			int j = 0;
			for(String k : kol) {
				var tmp = rr.get(k);
				tab[i][j++] = (tmp != null) ? tmp.toString() : "";
			}
			i++;
		}
		
		return tab;
	}
	public Object ktheObjekt(int rr, String kol) {
		try{
			return rreshta.get(rr).get(kol);
		} catch(Exception e) {
			// No such thing
		}
		return null;
	}
	///////////////////////
	public Map<String, MyTipeTeDhenash> getKolona() {
		return kolona;
	}

	public void setKolona(Map<String, MyTipeTeDhenash> kolona) {
		this.kolona = kolona;
	}
	
	public boolean checkKolona(List<String> kol) {
		int i = 0;
		for(String k : kol)
			if(kolona.containsKey(k))
				i++;
		if(i == kol.size())
			return true;
		return false;
	}
}
