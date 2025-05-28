package other;


import modelo.MemoriaPrincipal.MemoriaPrincipal;

public class StaticObjects {
	private static MemoriaPrincipal mp = null;
	
	public static MemoriaPrincipal getMemoriaPrincipal() {
		if(mp == null) {
			mp = new MemoriaPrincipal();
		}
		return mp;
	}
	
}
