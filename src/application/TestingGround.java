package application;

import java.util.Scanner;

import javafx.scene.control.TableView;

public class TestingGround {

	public static void main(String[] args) {
		MyERI myERI = new MyERI("myDatabase");
		//why shto kolone test Emri Ermal Mosha 20 kaq\n
		myERI.ekzekuto("krijo tabele test\n"
				+ "shto kolona test Emri VARCHAR Mbiemri VARCHAR Mosha INT kaq\n"
				+ "shto rreshta test Ermal, Arapi, 20; Arjon, Arapi, 10 kaq\n");
		myERI.ekzekuto("shfaq gjithcka nga test");//

		System.out.println(myERI.getMyTabela().get("test").getKolona().get("Emri"));
		System.out.println(myERI.getMyTabela().get("test").getKolona().get("Mosha"));

		System.out.println(myERI.shfaqTab("test"));

		
//myERI.getTabela().get("test").kerkoRreshta("Emri", "Ermal");
		
		//MyERI test = new MyERI("Data");
		//Scanner scan = new Scanner(System.in);
		//String k = "";
		//while(!k.equals("stop")) {
			//test.ekzekuto(scan.nextLine());
		//}
	}

}
