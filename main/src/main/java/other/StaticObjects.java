package other;

import modelo.memoriaPrincipal.MemoriaPrincipal;
import modelo.memoriaSecundaria.MemoriaSecundaria;
import modelo.processo.ImagemProcesso;
import visao.ViewTLB;
import visao.ViewTabelaPaginas;

import java.util.ArrayList;

public class StaticObjects {
	private static MemoriaPrincipal mp = null;
	private static ArrayList<ImagemProcesso> allProcessos = new ArrayList<>();
	private static MemoriaSecundaria memoriaSecundaria = null;
	private static ViewTabelaPaginas vtp = null;
	private static ViewTLB vtlb = null;

	
	public static ViewTabelaPaginas getVTP() {
		return vtp;
	}
	public static ViewTLB getVTLB() {
		return vtlb;
	}
	public static ArrayList<ImagemProcesso> getAllProcessos(){
		return allProcessos;
	}
	public static MemoriaPrincipal getMemoriaPrincipal() {
		if(mp == null) {
			mp = new MemoriaPrincipal();
		}
		return mp;
	}
	public static MemoriaSecundaria getMemoriaSecundaria() {
		if(memoriaSecundaria == null) {
			memoriaSecundaria = new MemoriaSecundaria();
		}
		return memoriaSecundaria;
	}
}
