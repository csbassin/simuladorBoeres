package modelo.memoriaPrincipal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import config.ConfigData;

public class MemoriaPrincipal {
	private final boolean[] quadrosOcupados;
	private final Queue<Integer> quadrosLivres;
	public final int tamanhoMP;

	public MemoriaPrincipal() {
		this.quadrosOcupados = new boolean[ConfigData.quantidadeQuadros];
		this.quadrosLivres = new LinkedList<>();
		//Todos os quadros estão livres no começo
		for (int i = 0; i < ConfigData.quantidadeQuadros; i++) {
			quadrosLivres.add(i);
		}
		this.tamanhoMP = ConfigData.quantidadeQuadros * ConfigData.quadroSize;
	}

	public Integer alocarQuadroLivre() {
		if (quadrosLivres.isEmpty()) {
			return null;
		}
		int quadro = quadrosLivres.poll(); //remove o primeiro elemento da queue e retorna ele
		quadrosOcupados[quadro] = true;
		return quadro;
	}

	public void liberar(int numQuadro) {
		// Volta com o quadro de volta pra fila de livres
		if (numQuadro >= 0 && numQuadro < quadrosOcupados.length && quadrosOcupados[numQuadro]) {
			quadrosOcupados[numQuadro] = false;
			quadrosLivres.add(numQuadro);
		}
	}

	public boolean[] getQuadrosOcupados() {
		return quadrosOcupados;
	}

	public int getTamanhoMP() {
		return tamanhoMP;
	}

	public int getQuantidadeQuadrosLivres() {
		return quadrosLivres.size();
	}
}