package other;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

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
			Object[] linhas = Files.lines(input.toPath()).toArray();

			// Aplica o trim nas linhas e coloca em um array
			instrucoes = (ArrayList<String>) Arrays.stream(linhas).map(
					x -> ((String) x).trim()
						  ).collect(Collectors.toList());

			//Cria uma lista somente com os processos existentes
			HashSet<String> processos = instrucoes.stream().map(
								  x -> x.substring(0, x.indexOf(' ')
										)).collect(Collectors.toCollection(HashSet::new));
			quantidadeProcessos = processos.size();
			}
			instrucoesPendentes = instrucoes.size();
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
