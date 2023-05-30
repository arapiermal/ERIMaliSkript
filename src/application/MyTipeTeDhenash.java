package application;

public enum MyTipeTeDhenash {
	INT,
	VARCHAR;
	public boolean perputhje(Object o) {
		switch (this) {
		case INT:
			return o instanceof Integer;
		case VARCHAR:
			return o instanceof String;
		default:
			return false; // Tip i pasuportuar
		}
	}
	
	public static MyTipeTeDhenash merrTipin(Object o) {
	    for (MyTipeTeDhenash type : MyTipeTeDhenash.values()) {
	        if (type.perputhje(o)) {
	            return type;
	        }
	    }
	    return null; // Tip qe s'eshte ne enum
	}

}
