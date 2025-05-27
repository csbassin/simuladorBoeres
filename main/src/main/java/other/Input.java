package other;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Input {
	private ArrayList<String> instrucoes = new ArrayList<String>();
	private String filePath = "";
	
	public Input(String path) throws Exception {
		File input = new File(path);
		if(!input.exists()) {
			throw new Exception("O arquivo não existe.");
		}else {
			Object linhas[] = Files.lines(input.toPath()).toArray();
			for(int i = 0; i<linhas.length; i++) {
				instrucoes.add((String)linhas[i]);
			}
		}
	}

	public ArrayList<String> getInstrucoes() {
		return instrucoes;
	}

	public void setInstrucoes(ArrayList<String> instrucoes) {
		this.instrucoes = instrucoes;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	

}
