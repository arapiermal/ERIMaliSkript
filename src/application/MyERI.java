package application;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.scene.control.TableView;

//Ergonomical Resource Introspector
public class MyERI {
	private String emriDatabazes;
	private Map<String, MyTabela> tabela;

	public MyERI(String emriDatabazes) {
		this.setEmriDatabazes(emriDatabazes);
		this.tabela = new HashMap<>();
	}

	public void shtoTabeleTePlote(String emriTab, Object[][] o) {
		if(o == null || o.length < 2)
			return;
		krijoTabele(emriTab, o);
	}
	
	public void fromCSV(String content) {
		Scanner s = new Scanner(content);
		if(s.hasNext("TABELE")) {
			StringBuilder sb;
			while(s.hasNextLine()) {
				sb = new StringBuilder();
				s.next();
				String tabName = s.nextLine();
				while(!s.hasNext("EOT")) {
					sb.append(s.nextLine()).append("\n");
				}
				shtoTabeleTePlote(tabName, MSFile.readCSVTable(sb.toString()));
				if(s.hasNextLine())
					s.nextLine();
				//shfaqTab(tabName);
				//System.out.println(tabName);
			}
			
		} else {
			shtoTabeleTePlote(emriDatabazes, MSFile.readCSVTable(content));
		}
		s.close();
	}

	public void krijoTabele(String emriTab) {
		tabela.put(emriTab, new MyTabela());
	}
	public void krijoTabele(String emriTab, Object[][] o) {
		tabela.put(emriTab, new MyTabela(o));
	}
	public void shtoKoloneNeTab(String emriTab, String emri, MyTipeTeDhenash tipi) {
		tabela.get(emriTab).shtoKolone(emri, tipi);
	}
	public void shtoKoloneNeTab(String emriTab, String emri, String tipi) {
		tabela.get(emriTab).shtoKolone(emri, MyTipeTeDhenash.valueOf(tipi.toUpperCase()));
	}

	public void shtoRreshtNeTab(String emri, String input) {
		tabela.get(emri).shtoRresht(nxirrRresht(input));
	}

	public void shtoDisaRreshtaNeTab(String emri, String inputdisa) {
		tabela.get(emri).shtoDisaRreshta(ekstraktoRreshta(inputdisa));
	}

	////////////////////
	public String shfaqTab(String emriTab) {
		return tabela.get(emriTab).toString();
	}

	// Ermal, Arapi, 20; Arjon, Arapi, 20
	public Object[][] ekstraktoRreshta(String inputdisa) {
		String[] kerko = inputdisa.split(";");
		Object o[][] = new Object[kerko.length][];

		for (int i = 0; i < o.length; i++) {
			o[i] = nxirrRresht(kerko[i].trim());
		}

		return o;
	}

	public Object[] nxirrRresht(String input) {
		Scanner scan = new Scanner(input);
		scan.useDelimiter(",");
		Object o[] = new Object[input.split(",").length];

		int i = 0;
		while (scan.hasNext()) {
			if (scan.hasNextInt()) {
				o[i] = scan.nextInt();
			} else {
				o[i] = scan.next().trim(); // Nqs eshte String, shkurtohet
			}
			i++;
		}
		scan.close();
		return o;
	}
	//Shfaq GJITHCKA
	// 
	public void tabelaFX(String emri) {
		if(tabela.containsKey(emri)) {
			Main.setTableView(new TableView<>());
			String[][] tabelaArr = tabela.get(emri).toTable(); 
	        Main.tableBuilder(emri, tabelaArr, Main.getTableView());
		}
	}
	public void tabelaFX(String emri, List<String> kol){
		if(tabela.containsKey(emri)) {
			MyTabela tab = tabela.get(emri);
			if(tab.checkKolona(kol)) {
				Main.setTableView(new TableView<>());
				String[][] tabelaArr = tabela.get(emri).toTableFromKolona(kol); 
		        Main.tableBuilder(emri, tabelaArr, Main.getTableView());
			}
		}
		else
			System.out.println("Emri i kolonave gabim");
	}
	
	public void ekzekuto(String s) {
		Scanner scan = new Scanner(s);
		String k, k2; // keywords
		Boolean snl = false; // scan.nextLine() nqs duhet, per te mos patur error Scanner
		while (scan.hasNextLine()) {
			k = scan.next().toLowerCase();
			if (k.equals(MySint.KRIJO.get())) {
				k2 = scan.next();
				if (k2.equals(MySint.TABELE.get())) {
					krijoTabele(scan.next());
				}
			} else if (k.equals(MySint.SHTO.get())) {
				k2 = scan.next();
				if (k2.equals(MySint.KOLONA.get())) {
					// Shto kolona emriTab Emri VARCHAR Mbiemri VARCHAR Mosha INT kaq
					String emri = scan.next();
					// &&
					while (!scan.hasNext(MySint.MBYLLESE.get())) {
						shtoKoloneNeTab(emri, scan.next(), scan.next());
					}
				} else if (k2.equals(MySint.RRESHTA.get())) {
					// emriTab Ermal, Arapi, 20; Arjon, Arapi 20 ... kaq (?)
					String emri = scan.next();
					StringBuilder sb = new StringBuilder();
					while (!scan.hasNext(MySint.MBYLLESE.get())) {
						sb.append(scan.next()); // nextLine?
					}
					String input = sb.toString();
					if(input.contains(";")) {
						shtoDisaRreshtaNeTab(emri, input);
					}
					else {
						shtoRreshtNeTab(emri, input);
					}
				}
			}
			// Shfaq gjithcka NGA emriTabeles
			else if (k.equals(MySint.SHFAQ.get())) {
				k2 = scan.next();
				if (k2.equalsIgnoreCase(MySint.GJITHCKA.get())) {
					if (scan.next().equalsIgnoreCase(MySint.NGA.get())) {
						////////////////////////////////////////////////
						////////////////////////////////////////////////
						// SHIKOJME NESE APLIKACIONI NE JAVAFX ESHTE DUKE U EKZEKUTUAR
						String tName = scan.next();
						if(Platform.isFxApplicationThread()) {
							/////////////////////////
							tabelaFX(tName);
							/////////////////////////
						}
						else {
							System.out.println(shfaqTab(tName));
						}
						////////////////////////////////////////////////
						////////////////////////////////////////////////
					}
				} else {
					//Sipas kolones
					List<String> kols = new LinkedList<String>();
					kols.add(k2);
					while(scan.hasNext() && !scan.hasNext(MySint.NGA.get())) {
						kols.add(scan.next());
					}
					scan.next();
					String tName = scan.next();
					if(Platform.isFxApplicationThread()) {
						/////////////////////////
						tabelaFX(tName, kols);
						/////////////////////////
					}
					else {
						//System.out.println(shfaqTab(tName));
					}
				}

			} else if (k.equals(MySint.HIQ.get())) {
				k2 = scan.next();
				if (k2.equals(MySint.KOLONA.get())) {
					// Hiq kolona emriTab Emri Mbiemri Mosha kaq
					String emri = scan.next();
					while (!scan.hasNext(MySint.MBYLLESE.get())) {
						hiqKoloneNgaTab(emri, scan.next());
					}
				} else if (k2.equals(MySint.RRESHTA.get())) {
					//Hiq rreshta emriTab 
					String emri = scan.next();
					
					while (scan.hasNextInt() && !scan.hasNext(MySint.MBYLLESE.get())) {
						hiqRreshtNgaTab(emri,scan.nextInt());
					}
					
				}
			} else {

			}
			if (!snl && scan.hasNextLine())
				scan.nextLine();
		}
		scan.close();
	}
	//
	
	//public int gjejIndeksRreshti(String[] kolValue) {
		
	//}
	public String toCSV() {
		if(tabela.isEmpty()) {
			return ""; //EMPTY DATABASE
		} else if(tabela.size() == 1) {
			MyTabela t = tabela.values().iterator().next();
			return t.toString("CSV");
		} else {
			StringBuilder sb = new StringBuilder();
			for (String tName : tabela.keySet()) {
	            sb.append("TABELE,").append(tName).append("\n");
	            MyTabela t = tabela.get(tName);
	            sb.append(t.toString("CSV")).append("EOT").append("\n");
	        }
			return sb.toString();
		}
	}
	
	public void hiqKoloneNgaTab(String tab, String ek) {
		if(tabela.containsKey(tab)) {
			tabela.get(tab).hiqKolone(ek);
		}
	}
	public void hiqRreshtNgaTab(String tab, int i) {
		if(tabela.containsKey(tab)) {
			tabela.get(tab).hiqRresht(i);
		}
	}
	public int ktheVarPerMS(String tab, int rr, String kol) {
		return (Integer) tabela.get(tab).ktheObjekt(rr, kol);
	}
	
	public String getEmriDatabazes() {
		return emriDatabazes;
	}

	public void setEmriDatabazes(String emriDatabazes) {
		this.emriDatabazes = emriDatabazes;
	}

	public Map<String, MyTabela> getMyTabela() {
		return tabela;
	}
}
