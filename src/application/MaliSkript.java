package application;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MaliSkript {
	private String kodi; // Array me char
	private List<String> rezultati; // LinkedList per te shtuar/hequr me shpejte
	private Map<String, Integer> variabla; // HashMap per aksesim te shpejte me ane te nje fjale kyc
	private Map<String, List<Integer>> array; // ArrayList per aksesim me te shpejte
	private Map<String, MSFunction> funksione;
	private Map<String, Path> files; // varFiles

	
	private int forInFor;
	protected boolean isFunction;

	private MyERI databaza; //ONLY ONE AT A TIME

	public MaliSkript(String kodi) {
		this.kodi = kodi;
		rezultati = new LinkedList<>();
		variabla = new HashMap<>();
		array = new HashMap<>();
		funksione = new HashMap<>();
		files = new HashMap<>();//
		forInFor = 0;//
		isFunction = false;
	}

	public MaliSkript(String kodi, int kthe) {
		this.kodi = kodi;
		rezultati = new LinkedList<>();
		variabla = new HashMap<>();
		array = new HashMap<>();
		funksione = new HashMap<>();
		files = new HashMap<>();
		forInFor = 0;//
		isFunction = true;
	}
	// for MSFunction, make other constructor?

	//
	/////////////////////
	/////////////////////
	// trim() might be neeeded after scan.next() or scan.nextLine()

	// fix
	// perdor isFunction per ("Ne funksion: "+e.getMessage(),currLine)

	public void ekzekuto(String kod) {
		// ndoshta brenda?
		Scanner scan = new Scanner(kod);
		int currLine = 0;

		// per arsye se Scanner nuk lexon \n nqs nuk ndodh nextLine()
		boolean slashN;
		while (scan.hasNextLine()) {
			try {
				slashN = false;
				String k = scan.next();
				if (k.isEmpty() || k.isBlank()) {
					currLine++;
					continue;
				}
				k.toLowerCase().trim();
				if (k.startsWith(Sintaksa.KOMENT.get())) {
					// Qe ne fillim, qe te mos shkoje te kontrolloje komandat tek e tek gjer ne fund

					// NOTHING HERE
				} else if (k.equals(Sintaksa.VAR.get())) {
					try {
						String e = scan.next();
						if (scan.hasNext(Sintaksa.VEPRO.get()) || scan.hasNext("=")) {
							scan.next();
							vendosVar(e, ZgjidhEk.vepro(scan.nextLine(), variabla));
							slashN = true;
						} else
							vendosVar(e, scan.nextInt());
					} catch (Exception e) {
						Kompilatori.komp.reportoError(e, currLine);
					}
				} else if (k.equals(Sintaksa.ARR.get())) {
					try {
						String e = scan.next();
						List<Integer> l = new ArrayList<>();
						while (scan.hasNextInt()) {
							l.add(scan.nextInt());
						}
						vendosArr(e, l);
					} catch (Exception e) {
						Kompilatori.komp.reportoError(e, currLine);
					}
				} else if (k.equals(Sintaksa.FUNKSION.get())) {
					try {
						boolean hasInput = false;
						MSFunction f; // Work in progress
						String e = scan.next(); // emri i funksionit
						/////////////////////////////////
						String temp = scan.nextLine().trim();
						String[] inputVarName = temp.split(" ");
						if (inputVarName.length > 0) {
							hasInput = true;
						}

						/////////////////////////////////
						int kaq = 1;
						StringBuilder sb = new StringBuilder();
						while (kaq > 0 && scan.hasNextLine()) {
							// shto gjera te tjera qe mund te kete?
							if (scan.hasNext(Sintaksa.CIKLI_FOR.get()) || scan.hasNext(Sintaksa.NESE.get())) {
								kaq++;
							}
							sb.append(scan.nextLine());
							sb.append("\n");
							if (scan.hasNext(Sintaksa.MBYLL.get())) {
								// skip fjalen mbyllese
								scan.nextLine();
								kaq--;
								slashN = true;
							}
						}
						if (!hasInput)
							f = new MSFunction(sb.toString());
						else
							f = new MSFunction(sb.toString(), inputVarName);
						funksione.put(e, f);
					} catch (Exception e) {
						Kompilatori.komp.reportoError(e, currLine);
					}

				} else if (k.equals(Sintaksa.FUNKS_THIRR.get())) {
					try {
						String e = scan.next();
						int[] inputVar = null;
						boolean kaInput = false;
						int i = 0;
						if (scan.hasNextLine()) {
							String temp = scan.nextLine().trim();
							String[] rreshti = temp.split("\\s+"); // skip karakteret 'whitespace'
							slashN = true;

							if (rreshti.length > 0) {
								// to be fixed
								inputVar = new int[rreshti.length];

								for (String t : rreshti) {
									if (!t.isBlank())
										if (Character.isDigit(t.charAt(0))) {
											inputVar[i++] = Integer.parseInt(t);
										} else if (!t.isBlank()) {
											inputVar[i++] = variabla.get(t);
										}
								}

							}
						}
						if (i > 0) {
							kaInput = true;
						}
						// error handling ??
						if (funksione.containsKey(e)) {
							// ekzekuto funksionin
							if (!kaInput)
								rezultati.addAll(funksione.get(e).ekzekuto());
							else
								rezultati.addAll(funksione.get(e).ekzekuto(inputVar));
							// po sikur funksion me return

						} else
							throw new IllegalArgumentException("Funksioni nuk u gjet --> " + e);
					} catch (Exception e) {
						Kompilatori.komp.reportoError(e, currLine);
					}
				} else if (k.equals(Sintaksa.CIKLI_FOR.get())) {
					try {
						int kaq = 0;
						int a = 0, b = 1;
						if (scan.hasNextInt())
							a = scan.nextInt();
						else
							a = variabla.get(scan.next());
						scan.next(); // skip: deri
						if (scan.hasNextInt())
							b = scan.nextInt();
						else
							b = variabla.get(scan.next());
						scan.nextLine();
						StringBuilder sb = new StringBuilder();
						while (scan.hasNextLine() && kaq >= 0) {
							sb.append(scan.nextLine());
							sb.append("\n");
							if (scan.hasNext(Sintaksa.CIKLI_FOR.get()) || scan.hasNext(Sintaksa.NESE.get())) {
								kaq++;
							} else if (scan.hasNext(Sintaksa.MBYLL.get())) {
								kaq--;
							}
						}
						String kodTemp = sb.toString();
						///////
						if (scan.hasNext())
							scan.next();
						///////
						// Ekzekutim me funksion rekursiv
						// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						forInFor++;
						if (a < b) {
							for (int i = a; i <= b; i++) {
								variabla.put(Sintaksa.INDEKS.get() + forInFor, i);
								ekzekuto(kodTemp);
							}
						} else {
							for (int i = a; i >= b; i--) {
								variabla.put(Sintaksa.INDEKS.get() + forInFor, i);
								ekzekuto(kodTemp);
							}
						}
						forInFor--;
					} catch (Exception e) {
						rezultati.add("Error ne ciklin for");
						Kompilatori.komp.reportoError(e, currLine);
					}
				} else if (k.equals(Sintaksa.NESE.get())) {
					/////////////////////////////////////////
					/////////////////////////////////////////
					try {
						boolean rezulti = ZgjidhBulean.vepro(scan.nextLine().trim(), variabla);
						boolean perndryshe = false;
						StringBuilder sbNese = new StringBuilder();
						StringBuilder sbPerndryshe = new StringBuilder();
						String kodiTemp;
						while (scan.hasNextLine() && !scan.hasNext(Sintaksa.MBYLL.get())) {
							sbNese.append(scan.nextLine()).append("\n");
							if (scan.hasNext(Sintaksa.PERNDRYSHE.get())) {
								perndryshe = true;
								scan.nextLine();
								break; //////////////////////
							}
						}
						if (perndryshe && !rezulti) {
							while (scan.hasNextLine() && !scan.hasNext(Sintaksa.MBYLL.get())) {
								sbPerndryshe.append(scan.nextLine()).append("\n");
							}
							scan.next();
							kodiTemp = sbPerndryshe.toString();
							ekzekuto(kodiTemp);
						} else {
							while (scan.hasNextLine() && !scan.hasNext(Sintaksa.MBYLL.get())) {
								scan.nextLine();
							}
							scan.next();
							if (rezulti) {
								kodiTemp = sbNese.toString();
								ekzekuto(kodiTemp);
							}
						}

					} catch (Exception e) {
						rezultati.add("Error ne ekuacionin bulean");
						Kompilatori.komp.reportoError(e, currLine);
					}
				} else if (k.equals(Sintaksa.FILE.get())) {
					try {
						String komand = scan.next().trim();
						String path;
						slashN = true;
						switch (komand) {
						case "var":
							String vName = scan.next();
							path = scan.nextLine().trim();
							files.put(vName, MSFile.gjejFile(path));
							break;
						// Numero fjalet
						case "numero":
							path = scan.nextLine().trim();
							// is a variable
							if (files.containsKey(path)) {
								Main.popUpDialog(MSFile.ndertoTabele(MSFile.numeroFjale(files.get(path).toString())),
										"Fjalet e numeruara nga variabla " + path + " - " + files.get(path).toString());
							}
							// is a path
							else {
								Main.popUpDialog(MSFile.ndertoTabele(MSFile.numeroFjale(path)),
										"Fjalet e numeruara nga " + path);
							}
							break;
						case "rendit":
							boolean zbritese = false;
							if(scan.hasNext("mbrapsht")) {
								zbritese = true;
								scan.next();
							}
							path = scan.nextLine().trim();
							// is a variable
							if (files.containsKey(path)) {
								Main.popUpDialog(MSFile.sortInfoRow(files.get(path).toString(), zbritese),
										"Fjalite e renditura nga " + path + " - " + files.get(path).toString());
							}
							// is a path
							else {
								Main.popUpDialog(MSFile.sortInfoRow(path, zbritese),
										"Fjalite e renditura nga " + path);
							}
							break;
						case "gjej":
							path = scan.nextLine().trim();
							Path temp = MSFile.gjejFile(path);
							rezultati.add(temp.toAbsolutePath().toString());
							break;

						default:
							rezultati.add("Nuk ka komande te tille per file-s");
						}
					} catch (Exception e) {
						Kompilatori.komp.reportoError(e, currLine);
					}
				} else if (k.equals(Sintaksa.PRINTO.get())) {
					try {
						StringBuilder sb = new StringBuilder();
						if (scan.hasNext(Sintaksa.VEPRO.get())) {
							scan.next();// Skip komandes vepro
							sb.append(ZgjidhEk.vepro(scan.nextLine(), variabla));
							slashN = true;
						} else {
							// ndoshta vepro per array?
							String[] t = scan.nextLine().split(" ");
							for (int i = 0; i < t.length; i++) {
								// Simboli i array: #
								if (t[i].length() > 0 && t[i].charAt(0) == '#') {
									String arrName = t[i].substring(1);
									// Vlera e array-it ne nje indeks
									if (arrName.matches(".*@\\d+$")) {
										String[] parts = arrName.split("@");
										arrName = parts[0]; // Pjesa para @
										int indeks = 1;
										indeks = Integer.parseInt(parts[1]);
										// Duke marre sikur indeksi fillon nga 1
										sb.append(merrArr(arrName, indeks - 1));
									}
									// Vlera e array-it me indeks te marre nga variabla
									else if (arrName.matches(".*@.*")) {
										String[] parts = arrName.split("@");
										arrName = parts[0];
										int indeks = 1;
										if (variabla.containsKey(parts[1])) {
											indeks = variabla.get(parts[1]);
										}
										// Duke marre sikur indeksi fillon nga 1
										sb.append(merrArr(arrName, indeks - 1));
									}
									// E gjithe array
									else if (array.containsKey(arrName)) {
										sb.append(array.get(arrName).toString());

									}
								}
								// Jep nje variabel
								else if (t[i].length() > 0 && t[i].charAt(0) == '$') {
									String varName = t[i].substring(1);
									if (variabla.containsKey(varName)) {
										sb.append(variabla.get(varName));
									} else {
										sb.append("!!variabla nuk u gjet!!->").append(varName);
									}

								} else
									sb.append(t[i]);
								sb.append(" ");
							}
							slashN = true;
						}
						// .trim() ??
						rezultati.add(sb.toString().trim());
					} catch (Exception e) {
						Kompilatori.komp.reportoError(e, currLine);
					}
				} else if (k.equals(Sintaksa.DATABAZE.get())) {
					try {
						String k2 = scan.next().toLowerCase();
						switch (k2) {
						case "krijo":
							inicioDatabaze(scan.next());
							break;
						/////////////////////////////////////
						case "ekz":
							StringBuilder sb = new StringBuilder();
							while (!scan.hasNext(Sintaksa.STOP.get())) {
								sb.append(scan.nextLine()).append("\n");
							}
							ekzekutoMyERI(sb.toString());
							break;
						default:
							rezultati.add("Instruksion per databaze i panjohur");
						}
					} catch (Exception e) {
						Kompilatori.komp.reportoError("ERROR DATABAZE: " + e.getMessage(), currLine);
					}
				}
				// Vepron mbi variabla direkt, pasi jane deklaruar
				else if (variabla.containsKey(k)) {
					try {
						String op = scan.next();
						if (op.equals("=")) {
							if (scan.hasNext("db")) {
								scan.next();
								variabla.put(k, dbMerrVar(scan.nextLine()));
							} else if (scan.hasNext(Sintaksa.FUNKS_THIRR.get())) {
								scan.next();
								String fName = scan.next();
								if (funksione.containsKey(fName)) {
									int in[] = inFunc(fName, scan.nextLine().split("\\s+"));
									if (in.length == 0)
										funksione.get(fName).ekzekuto();
									else
										funksione.get(fName).ekzekuto(in);
									
									variabla.put(k, funksione.get(fName).ekzKthe()[0]);

								}
							} else
								variabla.put(k, ZgjidhEk.vepro(scan.nextLine(), variabla));
							slashN = true;
						} else if (MSComp.eshteOperatorMeBaraz(op)) {
							char operatori = op.charAt(0);
							String veprimi = k + operatori + scan.nextLine();
							variabla.put(k, ZgjidhEk.vepro(veprimi, variabla));
							slashN = true;
						}
						// contains ++ or -- ?!?
						else if (op.equals("++")) {
							variabla.put(k, variabla.get(k) + 1);
						} else if (op.equals("--")) {
							variabla.put(k, variabla.get(k) - 1);
						}
					} catch (Exception e) {
						Kompilatori.komp.reportoError(e, currLine);
					}
				}
				// #emriArray = 1 2 3 ndryshon array-in plotesisht
				else if (k.charAt(0) == '#') {
					try {
						String aName = k.substring(1); //Heqim #
						if (array.containsKey(aName)) {
							String op = scan.next();
							if (op.equals("=")) {
								scan.next();
								if (scan.hasNext(Sintaksa.FUNKS_THIRR.get())) {
									scan.next();
									String fName = scan.next();
									if (funksione.containsKey(fName)) {
										int in[] = inFunc(fName, scan.nextLine().split("\\s+"));
										if (in.length == 0)
											funksione.get(fName).ekzekuto();
										else
											funksione.get(fName).ekzekuto(in);

										List<Integer> l = new ArrayList<>();
										int[] o = funksione.get(fName).ekzKthe();
										for (int i = 0; i < o.length; i++) {
											l.add((Integer) o[i]);
										}
										vendosArr(aName, l);
									}
								} else {
									List<Integer> l = new ArrayList<>();
									while (scan.hasNextInt()) {
										l.add(scan.nextInt());
									}
									vendosArr(aName, l);
								}
							} else if(op.equals("+=")) {
								List<Integer> l = array.get(aName);
								while (scan.hasNextInt()) {
									l.add(scan.nextInt());
								}
								vendosArr(aName, l);
							} else if(op.equals("-=")) {
								List<Integer> l = array.get(aName);
								while (scan.hasNextInt()) {
									l.remove((Integer) scan.nextInt());
								}
								vendosArr(aName, l);
							}
						}
					} catch (Exception e) {
						Kompilatori.komp.reportoError(e, currLine);
					}
				} else if (isFunction) {
					try {
						if (k.equals(Sintaksa.FUNKS_KTHE.get())) {
							if(scan.hasNext(Sintaksa.VEPRO.get())) {
								scan.next();
								List<Integer> temp = new ArrayList<>();
								int rez = ZgjidhEk.vepro(scan.nextLine(), variabla);
								slashN = true;
								temp.add(rez);
								array.put("perKthim", temp);
								return;
							}
							String[] t = scan.nextLine().split("\\s+");
							slashN = true;
							List<Integer> temp = new ArrayList<>();
							for (int i = 0; i < t.length; i++) {
								if (t[i].trim().isBlank())
									continue;
								int nr = MSComp.stringToInt(t[i]);
								if (nr != Integer.MIN_VALUE) {
									temp.add(nr);
								} else {
									temp.add(variabla.get(t[i]));
								}
							}

							array.put("perKthim", temp);
							return;
						}
					} catch (Exception e) {
						Kompilatori.komp.reportoError("Error  ne kthim te funksionit: " + e.getMessage(), -1);
					}
				} else {
					rezultati.add("Nuk ka komande/variabel te tille");
				}
				if (!slashN && scan.hasNextLine())
					scan.nextLine();
				currLine++;
			} catch (Exception e) {
				Kompilatori.komp.reportoError(e, currLine);
			}
		}
		scan.close();
	}

	// variabla
	public void vendosVar(String emri, int vlera) {
		variabla.put(emri, vlera);
	}

	public int merrVar(String emri) {
		if (variabla.containsKey(emri))
			return variabla.get(emri);
		else
			throw new IllegalArgumentException("Variabla " + emri + " nuk u gjet");
	}

	// array
	public void vendosArr(String emri, List<Integer> vlera) {
		array.put(emri, vlera);
	}

	// shton ne fund
	public void shtoNeArr(String emri, int a) {
		if (array.containsKey(emri)) {
			array.get(emri).add(a);
		} else
			throw new IllegalArgumentException("Array " + emri + " nuk u gjet");
	}

	// shton me indeks
	public void shtoNeArr(String emri, int a, int i) {
		if (array.containsKey(emri)) {
			array.get(emri).add(i, a);
		} else
			throw new IllegalArgumentException("Array " + emri + " nuk u gjet");
	}

	// merr nje vlere me indeks
	public int merrArr(String emri, int i) {
		if (i < 0)
			throw new IndexOutOfBoundsException("Nuk ka indeks negativ");
		else if (array.containsKey(emri))
			if (i < array.get(emri).size())
				return array.get(emri).get(i);
			else
				throw new IndexOutOfBoundsException("Indeksi " + i + " eshte jashte indeksit maksimal");
		else
			throw new IllegalArgumentException("Array " + emri + " nuk u gjet");
	}

	// shto funksion
	public void vendosFunks(String emri, MSFunction funksion) {
		funksione.put(emri, funksion);
	}

	public String getKodi() {
		return kodi;
	}

	public void setKodi(String kodi) {
		this.kodi = kodi;
	}

	// Ndertojme nje String per rezultatin e kodit
	public String shfaqRezultati() {
		StringBuilder sb = new StringBuilder();
		for (String r : rezultati)
			sb.append(r).append("\n");
		return sb.toString();
	}

	public void shtoRezultati(String s) {
		this.rezultati.add(s);
	}

	public void fshiRezultati() {
		this.rezultati.clear();
	}

///////////////////////////////////////////
	public int[] inFunc(String fName, String[] inS) {
		// int in[] = new int[funksione.get(fName).getInputVarName().length];
		int[] in;
		if (inS.length > 0) {
			in = new int[inS.length];
			int i = 0;
			for (String t : inS) {
				if (t.trim().isBlank())
					continue;
				int val = MSComp.stringToInt(t);
				if (val != Integer.MIN_VALUE) {
					in[i++] = val;
				} else if (variabla.containsKey(t)) {
					in[i++] = variabla.get(t);
				}
			}
			if (i == 0)
				return new int[0];
			if (in.length != i) {
				int[] newIn = new int[i];
				for (int j = 0; j < i; j++) {
					newIn[j] = in[j];
				}
				return newIn;
			}
			return in;
		} else {
			in = new int[0];
			return in;
		}
	}

	
	public List<String> getRezultati() {
		return rezultati;
	}

	public void setRezultati(List<String> rezultati) {
		this.rezultati = rezultati;
	}

	public Map<String, Integer> getVariabla() {
		return variabla;
	}

	public void setVariabla(Map<String, Integer> variabla) {
		this.variabla = variabla;
	}

	public Map<String, List<Integer>> getArray() {
		return array;
	}

	public void setArray(Map<String, List<Integer>> array) {
		this.array = array;
	}

	public Map<String, MSFunction> getFunksione() {
		return funksione;
	}

	public void setFunksione(Map<String, MSFunction> funksione) {
		this.funksione = funksione;
	}

	/////////////////////////////////////////////////////////////////////
	// MyERI
	// Ergonomical Resource Introspector
	public void inicioDatabaze(String emri) {
		if (databaza != null) {
			////////////////////////////////////
			rezultati.add("Databaza e meparshme u zhduk");
			////////////////////////////////////
		}
		databaza = new MyERI(emri);
		rezultati.add("Databaza u krijua me sukses: " + emri);

	}

	public void ekzekutoMyERI(String kodi) {
		try {
			databaza.ekzekuto(kodi);
		} catch (Exception e) {
			rezultati.add("Kodi MyERI - ERROR: " + e.getMessage());
		}
	}

	public MyERI getDB() {
		return databaza;
	}

	public String ktheCSVMyERI() {
		return databaza.toCSV();
	}

	// MERR VAR NGA DATABAZA PER MALISKRIPT
	public int dbMerrVar(String in) {
		// String in -> pjesa pas deklarimit var test db ... ose test = db ...
		// test = db test mosha 0 // ose var
		int v = 0, rr = 0;
		Scanner s = new Scanner(in);
		String tabName = s.next().trim();
		String kol = s.next().trim();
		if (s.hasNextInt()) {
			rr = s.nextInt();
		} else {
			String temp = s.next().trim();
			if (variabla.containsKey(temp)) {
				rr = variabla.get(temp);
			}
		}

		v = databaza.ktheVarPerMS(tabName, rr, kol);
		s.close();
		return v;
	}

	// BOSHATIS MALISKRIPT NE VEND TE "NEW" TEK MAIN
	public void boshatis() {
		rezultati.clear();
		variabla.clear();
		array.clear();
		funksione.clear();
		files.clear();
		forInFor = 0;
	}

	// ME KOD TE RI
	public void boshatis(String kodi) {
		this.kodi = kodi;
		rezultati.clear();
		variabla.clear();
		array.clear();
		funksione.clear();
		files.clear();
		forInFor = 0;
	}

	public void appendKodi(String addKodi) {
		kodi += "\n" + addKodi;
	}
}
