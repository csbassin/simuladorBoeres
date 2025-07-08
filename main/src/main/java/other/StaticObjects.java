package other;

import modelo.memoriaPrincipal.MemoriaPrincipal;
import modelo.memoriaSecundaria.MemoriaSecundaria;
import modelo.processo.ImagemProcesso;
import visao.ViewTabelaPaginas;
import visao.ViewTLB;
import visao.WindowData;

import java.util.ArrayList;
import java.util.Optional;

public class StaticObjects {
	private static MemoriaPrincipal mp = null;
	private static final ArrayList<ImagemProcesso> allProcessos = new ArrayList<>();
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

	public static Optional<ImagemProcesso> findProcessById(String id) {
		if (id == null) {
			return Optional.empty();
		}
		for (ImagemProcesso processo : allProcessos) {
			if (id.equals(processo.getIdProcesso())) {
				return Optional.of(processo);
			}
		}
		return Optional.empty();
	}
}