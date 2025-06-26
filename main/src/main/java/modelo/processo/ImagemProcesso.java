package main.java.modelo.processo;

import java.util.ArrayList;

import main.java.config.ConfigData;
import main.java.modelo.tabelaPaginas.TabelaDePaginas;

public class ImagemProcesso {
    private String idProcesso;
    private TabelaDePaginas tabelaDePaginas;
    private ArrayList<PaginaProcesso> paginasProcesso = new ArrayList<>(ConfigData.quantidadeQuadros); //começa o array com tamanho máximo pra não precisar realocar
    private ArrayList<String> instrucoesProcesso = new ArrayList<>();
    private int instrucaoAtual = 0;
	private ProcessControlBlock pcb = new ProcessControlBlock();
    
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

	public ProcessControlBlock getPcb() {
		return pcb;
	}

	public void incrementInstrucaoAtual() {
		this.instrucaoAtual++;
	}
	
    
    
}
