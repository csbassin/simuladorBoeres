package other;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import config.configData;

public class Input {
	//private ArrayList<String> instrucoes = new ArrayList<String>();
	private String filePath = "";
	ArrayList<String> instrucoes = new ArrayList<>();
	private int instrucoesPendentes;
	private int quantidadeProcessos = 0;
	
	public Input() throws Exception {
		File input = new File(configData.pathInput);
		if(!input.exists()) {
			throw new Exception("O arquivo não existe.");
		}else {
			Object linhas[] = Files.lines(input.toPath()).toArray();
			for(int i = 0; i<linhas.length; i++) {
				String instrucao = ((String)linhas[i]).trim();
				instrucoes.add(instrucao);
				String processo = instrucao.substring(0, instrucao.indexOf(' '));
				for(int a = 0; a<instrucoes.size(); a++) {
					int processNum = Integer.parseInt(instrucao.substring(1,  instrucao.indexOf(' ')));
					if( processNum > quantidadeProcessos) {
						quantidadeProcessos = processNum; 
					}
				}
			}
			instrucoesPendentes = instrucoes.size();
		}
	}


	public String getFilePath() {
		return filePath;
	}
	
	public int getInstrucoesPendentes() {
		return instrucoesPendentes;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public void setInstrucoesPendentes(int i) {
		this.instrucoesPendentes = i;
	}


	public ArrayList<String> getInstrucoes() {
		return instrucoes;
	}


	public void setInstrucoes(ArrayList<String> instrucoes) {
		this.instrucoes = instrucoes;
	}


	public int getQuantidadeProcessos() {
		return quantidadeProcessos;
	}


	public void setQuantidadeProcessos(int quantidadeProcessos) {
		this.quantidadeProcessos = quantidadeProcessos;
	}
	
	

}
