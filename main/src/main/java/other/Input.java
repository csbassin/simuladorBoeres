package other;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import config.configData;

public class Input {
	private ArrayList<String> instrucoes = new ArrayList<String>();
	private String filePath = "";
	
	public Input() throws Exception {
		File input = new File(configData.pathInput);
		if(!input.exists()) {
			throw new Exception("O arquivo não existe.");
		}else {
			Object linhas[] = Files.lines(input.toPath()).toArray();
			for(int i = 0; i<linhas.length; i++) {
				instrucoes.add(((String)linhas[i]).trim());
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
