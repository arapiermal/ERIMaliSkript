package application;

public enum MySint {
	KRIJO("krijo"), TABELE("tabele"), SHTO("shto"), HIQ("hiq"), KOLONA("kolona"), RRESHTA("rreshta"), SHFAQ("shfaq"),
	GJITHCKA("gjithcka"), NGA("nga"), TEK("tek"), MBYLLESE("kaq");
	private String mySint;

	MySint(String mySint) {
		this.mySint = mySint;
	}

	public String get() {
		return mySint;
	}

	public static String menuNdihmese() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Menu ndihmese rreth komandave te MaliSkript.\n");

        for (MySint komanda : MySint.values()) {
        	sb.append(komanda.get()).append(" - ").append(shpjegimi(komanda)).append("\n");
        }
        return sb.toString();
    }
	

    public static String shpjegimi(MySint komanda) {
        switch (komanda) {
            case KRIJO:
                return "Pas mund te kete: " + TABELE.get();
            case TABELE:
            	return "Akseson tabele";
            case SHTO:
            	return "Pas mund te kete: " + KOLONA.get() +", "+ RRESHTA.get();
            case KOLONA:
            	return "EmriKolones1 Tipi1 EmriKolones2 Tipi2 ... "+ MBYLLESE.get();
            case RRESHTA:
            	return "Kolonat e rreshtit ndahen me presje, rresht i ri pikepresje: Ermal, 20; Emerson, 21 " + MBYLLESE.get();
            case SHFAQ:
            	return "Shfaq tabele, psh: shfaq " + GJITHCKA.get() + " " + NGA.get() + " tabela1";
            case GJITHCKA:
            	return "Te gjithe te dhenat e tabeles";
            case NGA:
            	return "Ndermjetese shfaq ... nga ...";
            case HIQ:
               	return "kolona emerKolone1 emerKolon2 ose rreshta 1 2 3";
            case MBYLLESE:
            	return "Perfundon komanda (per arsye qe te kesh mundesi te besh nje komand ne disa rreshta)";
            default:
                return "";
        }
    }
}
