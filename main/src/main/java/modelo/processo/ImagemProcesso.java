package modelo.processo;

import java.util.ArrayList;

import config.configData;
import modelo.tabelaPaginas.TabelaDePaginas;

public class ImagemProcesso {
    private String idProcesso;
    private TabelaDePaginas tabelaDePaginas;
    private ArrayList<PaginaProcesso> paginasProcesso = new ArrayList<>(configData.quantidadeQuadros); //começa o array com tamanho máximo pra não precisar realocar
    private ArrayList<String> instrucoesProcesso = new ArrayList<>();
    private int instrucaoAtual = 0;
    
	public String getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}
	public TabelaDePaginas getTabelaDePaginas() {
		return tabelaDePaginas;
	}
	public void setTabelaDePaginas(TabelaDePaginas tabelaDePaginas) {
		this.tabelaDePaginas = tabelaDePaginas;
	}
	public ArrayList<PaginaProcesso> getPaginasProcesso() {
		return paginasProcesso;
	}
	public void setPaginasProcesso(ArrayList<PaginaProcesso> paginasProcesso) {
		this.paginasProcesso = paginasProcesso;
	}
	public ArrayList<String> getInstrucoesProcesso() {
		return instrucoesProcesso;
	}
	public void setInstrucoesProcesso(ArrayList<String> instrucoesProcesso) {
		this.instrucoesProcesso = instrucoesProcesso;
	}
	public int getInstrucaoAtual() {
		return instrucaoAtual;
	}
	public void incrementInstrucaoAtual() {
		this.instrucaoAtual++;
	}
	
    
    
}
