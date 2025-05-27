package other;

import java.util.ArrayList;

import modelo.MemoriaPrincipal.MemoriaPrincipal;
import modelo.tabelaPaginas.PonteiroTabelaDePaginas;

public class StaticObjects {
	private static MemoriaPrincipal mp = null;
	private static ArrayList<PonteiroTabelaDePaginas> ponteiroTabelaDePaginas = null;
	
	public static MemoriaPrincipal getMemoriaPrincipal() {
		if(mp == null) {
			mp = new MemoriaPrincipal();
		}
		return mp;
	}
	public static ArrayList<PonteiroTabelaDePaginas> getPonteiroTabelaDePaginas() {
		if(ponteiroTabelaDePaginas == null) {
			ponteiroTabelaDePaginas = new ArrayList<>();
		}
		return ponteiroTabelaDePaginas;
	}
}
