package application;

public enum Sintaksa {
	VAR("var"),
    ARR("arr"),
    FUNKSION("funksion"),
    FUNKS_THIRR("thirr"),
    FUNKS_KTHE("kthe"),
    CIKLI_FOR("per"),
    NESE("nqs"),
    PERNDRYSHE("pn"),
    PRINTO("printo"),
    VEPRO("vepro"),
    INDEKS("ind"),
    MBYLL("kaq"),
    KOMENT("~"),
    UNTIL("deri"),
    LEXO("lexo"),
    RUAJ("ruaj"), 
    FILE("file"),
    DATABAZE("db"),
    KRIJO("krijo"),
    EKZEKUTO("ekz"),
    STOP("stop"),
    RRITESE("parsh"),
    ZBRITESE("mbrapsht");
	
    private String sintaksa;

    private Sintaksa(String sintaksa) {
        this.sintaksa = sintaksa;
    }

    public String get() {
        return sintaksa;
    }
    
    
    public static String menuNdihmese() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Menu ndihmese rreth komandave te MaliSkript.\n");

        for (Sintaksa komanda : Sintaksa.values()) {
        	sb.append(komanda.get()).append(" - ").append(shpjegimi(komanda)).append("\n");
        }
        return sb.toString();
    }

    public static String shpjegimi(Sintaksa komanda) {
        switch (komanda) {
            case VAR:
                return "Deklaron variabel me vlere";
            case ARR:
                return "Deklaron array me vlera";
            case FUNKSION:
                return "Deklaron funksion, mund te kete parametra";
            case FUNKS_THIRR:
                return "Therret nje funksion (me parametra nese ka)";
            case FUNKS_KTHE:
                return "";
            case CIKLI_FOR:
                return "Cikli for, merr nje vlere filluese 'deri' ne nje vlere perfunduese";
            case NESE:
                return "if";
            case PERNDRYSHE:
            	return "else";
            case PRINTO:
                return "Printon tek rezultatet";
            case VEPRO:
                return "Mund te perdoret tek printo per te llogaritur nje shprehje matematikore";
            case MBYLL:
                return "Shenja perfunduese per funksionin, ciklin for dhe if/else";
            case KOMENT:
                return "Koment";
            case UNTIL:
                return "Tek cikli for (kryesisht)";
            case LEXO:
                return "";
            case RUAJ:
                return "";
            case FILE:
                return "Ka nenkomanda: numero (Numeron sa here ndodh nje fjale ne tekst)...";
            case DATABAZE:
            	return "MyERI";
            default:
                return "";
        }
    }
}
