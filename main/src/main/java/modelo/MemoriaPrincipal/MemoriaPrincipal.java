package modelo.MemoriaPrincipal;

import java.util.ArrayList;

import config.configData;

public class MemoriaPrincipal {
	// a memória mais falsa da história
	private ArrayList<Integer> quadrosLivres = new ArrayList<>(configData.quantidadeQuadros);
	private ArrayList<Integer> quadrosOcupados = new ArrayList<>(configData.quantidadeQuadros);
	private int tamanhoMP = configData.quantidadeQuadros * configData.quadroSize;
	
	public MemoriaPrincipal() {
		for(int i = 0; i<configData.quantidadeQuadros; i++) {
			quadrosLivres.add(i);
		}
	}
	
	public String getTamanhoMpAsString() {
		return tamanhoMP+"B";
	}
	
}
