package main.java.other;

import main.java.modelo.memoriaPrincipal.MemoriaPrincipal;
import main.java.modelo.memoriaSecundaria.MemoriaSecundaria;
import main.java.modelo.processo.ImagemProcesso;
import java.util.ArrayList;

public class StaticObjects {
	private static MemoriaPrincipal mp = null;
	private static ArrayList<ImagemProcesso> allProcessos = new ArrayList<>();
	private static MemoriaSecundaria memoriaSecundaria = null;




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
