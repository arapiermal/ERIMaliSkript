package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Scanner;

public class MSFile {
	public static final String FILE_EXTENSION = ".mskript";
	public static String loadFileMS(String path) {
		StringBuilder kodi = new StringBuilder();
		try {
			FileReader fr = new FileReader(path + FILE_EXTENSION);
			BufferedReader br = new BufferedReader(fr);
			String rresht;
			while ((rresht = br.readLine()) != null) {
				kodi.append(rresht).append("\n");
			}
			br.close();
		} catch (IOException e) {
			kodi.append("printo Error ne leximin e file"); //
		}
		return kodi.toString();
	}
	
	public static void ruajFileMS(MaliSkript ms, String emri) {
		try {
			PrintWriter writer = new PrintWriter(emri + FILE_EXTENSION);
			writer.print(ms.getKodi());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void ruajFile(String perRuajtje, String emri) {
		try {
			PrintWriter writer = new PrintWriter(emri);
			writer.print(perRuajtje);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Map<String, Integer> numeroFjale(String path) throws Exception{
		Map<String, Integer> count = new HashMap<>();
		Scanner scan = new Scanner(Paths.get(path));
		while (scan.hasNext()) {
			// behen shkronja te vogla te shtypit dhe hiqen shenjat e pikesimit
			String s = scan.next().toLowerCase().replaceAll("\\p{Punct}", "");
			// Kontrollojme nqs String-u eshte bosh (pas heqjes se shenjave te pikesimit)
			// Ose eshte nje shkronjeshe
			if (!s.isEmpty() || s.length() > 1)
				if (!count.containsKey(s))
					count.put(s, 1);
				else {
					count.put(s, (count.get(s) + 1));
				}
		}


		return count;
	}

	public static String ndertoTabele(Map<String, Integer> a) {
		StringBuilder t = new StringBuilder();
		if (!a.isEmpty()) {
			for (Map.Entry<String, Integer> entry : a.entrySet()) {
				String fjala = entry.getKey();
				int count = entry.getValue();
				t.append(fjala).append("\t\t\t").append(count).append("\n");
			}
		}
		return t.toString();
	}

	//
	public static Path gjejFile(String path) throws Exception{
		 return Paths.get(path);
	}
	// type with . ?
	public static Path gjejFile(String path, String type) throws Exception{
		if(path.endsWith(type)) {
			return Paths.get(path);
		} else {
			return Paths.get(path + type);
		}
	}
	
	//Sorting -> O(nlogn)
	public static String sortInfoRow(String path, boolean descending) throws IOException{
		StringBuilder sb = new StringBuilder();
		Queue<String> q; 
		try (BufferedReader br = new BufferedReader(new FileReader(path))){
			if(!descending) {
				q = new PriorityQueue<String>();
			} else {
				q = new PriorityQueue<String>(Collections.reverseOrder());
			}
			String rresht;
			while((rresht = br.readLine()) != null) {
				if(rresht.isBlank())
					continue;
				q.offer(rresht);
			}
			while(!q.isEmpty()) {
				sb.append(q.poll()).append("\n");
			}
		} 
		
		return sb.toString();
	}
	
	
	//MyFile
	//
	public static Object[][] readCSVTable(String stuff){
		try (Scanner scan = new Scanner(stuff)) {
			scan.useDelimiter(",");
			String colNames[] = scan.nextLine().split(",");
			int n = colNames.length;
			
			List<Object[]> data = new LinkedList<>();
			Object[] cols = new String[n];
			for(int i = 0; i < n ; i++) {
				cols[i] = colNames[i].trim();
			}
			printTest(cols);
			data.add(cols);
			//method differently for columns? return MyERI database?
			while(scan.hasNextLine()){
				Object[] o = new Object[n];
				for(int i = 0; i < n; i++){
					if(scan.hasNextInt()){
						//E kthejme ne Integer (object) (jo primitiv)
						o[i] = Integer.valueOf(scan.nextInt());
					} else if(scan.hasNext()){
						o[i] = scan.next().trim();
 						//it could be blank...
					}
					
				}
				printTest(o);
				data.add(o);
				if(scan.hasNextLine())
					scan.nextLine();
				
			}
			return data.toArray(new Object[0][]);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void printTest(Object[] o) {
		for(Object ob : o) {
			System.out.print(ob.toString()+" --- ");
			
		}
		System.out.println();
	}
}


