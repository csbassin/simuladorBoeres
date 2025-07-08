package other;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import config.ConfigData;

public class Input {
	private ArrayList<String> instrucoes = new ArrayList<>();
	private int quantidadeProcessos = 0;

	public Input() throws Exception {
		File input = new File(ConfigData.pathInput);
		if (!input.exists()) {
			throw new Exception("O arquivo de entrada não foi encontrado em: " + ConfigData.pathInput);
		}

		List<String> linhas = Files.readAllLines(input.toPath());

		for (String linha : linhas) {
			String linhaTratada = linha.trim();
			if (!linhaTratada.isEmpty() && !linhaTratada.startsWith("#")) {
				instrucoes.add(linhaTratada);
			}
		}

		HashSet<String> processos = instrucoes.stream()
				.filter(inst -> inst.split(" ")[1].equals("C"))
				.map(inst -> inst.split(" ")[0])
				.collect(Collectors.toCollection(HashSet::new));

		quantidadeProcessos = processos.size();
	}

	public ArrayList<String> getInstrucoes() {
		return instrucoes;
	}

	public int getQuantidadeProcessos() {
		return quantidadeProcessos;
	}
}