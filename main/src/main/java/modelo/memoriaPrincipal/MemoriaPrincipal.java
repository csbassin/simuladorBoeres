package main.java.modelo.memoriaPrincipal;

import java.util.ArrayList;

import main.java.config.ConfigData;

public class MemoriaPrincipal {
	// a memória mais falsa da história
	private ArrayList<Integer> quadrosLivres = new ArrayList<>(ConfigData.quantidadeQuadros);
	private ArrayList<Integer> quadrosOcupados = new ArrayList<>(ConfigData.quantidadeQuadros);
	private int tamanhoMP = ConfigData.quantidadeQuadros * ConfigData.quadroSize;
	
	public MemoriaPrincipal() {
		for(int i = 0; i< ConfigData.quantidadeQuadros; i++) {
			quadrosLivres.add(i);
		}
	}
	public void ocupar(Integer numQuadro) {
		quadrosLivres.remove(numQuadro);
		quadrosOcupados.add(numQuadro);
	}
	public void liberar(Integer numQuadro) {
		quadrosOcupados.remove(numQuadro);
		quadrosLivres.add(numQuadro);
	}
	public String getTamanhoMpAsString() {
		return tamanhoMP+"B";
	}

	public ArrayList<Integer> getQuadrosLivres() {
		return quadrosLivres;
	}

	public void setQuadrosLivres(ArrayList<Integer> quadrosLivres) {
		this.quadrosLivres = quadrosLivres;
	}

	public ArrayList<Integer> getQuadrosOcupados() {
		return quadrosOcupados;
	}

	public void setQuadrosOcupados(ArrayList<Integer> quadrosOcupados) {
		this.quadrosOcupados = quadrosOcupados;
	}

	public int getTamanhoMP() {
		return tamanhoMP;
	}

	public void setTamanhoMP(int tamanhoMP) {
		this.tamanhoMP = tamanhoMP;
	}
	
	
}
