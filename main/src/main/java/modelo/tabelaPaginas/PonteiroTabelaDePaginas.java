package modelo.tabelaPaginas;

public class PonteiroTabelaDePaginas {
	//indica em qual parte da mamória está a tabela de páginas, uma vez que elas podem ser alocadas em qualquer bloco contíguo suficiente
	// para manter as coisas simples, vamos manter esse ponteiro fora da memória do simulador
	
	private int numeroQuadroInicio;
	private int numeroQuadroFim;
	private String idProcesso;
	
	
	public int getNumeroQuadroInicio() {
		return numeroQuadroInicio;
	}
	public void setNumeroQuadroInicio(int numeroQuadroInicio) {
		this.numeroQuadroInicio = numeroQuadroInicio;
	}
	public int getNumeroQuadroFim() {
		return numeroQuadroFim;
	}
	public void setNumeroQuadroFim(int numeroQuadroFim) {
		this.numeroQuadroFim = numeroQuadroFim;
	}
	public String getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}
	
	
}
