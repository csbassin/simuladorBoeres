package modelo.tabelaPaginas;

public class EntradaTP {
	private boolean modificacao; // M: indica se a página foi modificada
	private boolean presenca;    // P: indica se a página está na memória principal
	private boolean uso;         // Bit de uso -> algoritmo do Relógio
	private long tempoUltimoUso; // tempo do último acesso -> algoritmo LRU.
	private int numQuadro;       // número do quadro na memória principal
	private int numPagina;       // Número da página

	public EntradaTP(boolean modificacao, boolean presenca, boolean uso, long tempoUltimoUso, int numQuadro, int numPagina) {
		this.modificacao = modificacao;
		this.presenca = presenca;
		this.uso = uso;
		this.tempoUltimoUso = tempoUltimoUso;
		this.numQuadro = numQuadro;
		this.numPagina = numPagina;
	}

	public boolean getModificacao() {
		return modificacao;
	}
	public void setModificacao(boolean modificacao) {
		this.modificacao = modificacao;
	}
	public boolean getPresenca() {
		return presenca;
	}
	public void setPresenca(boolean presenca) {
		this.presenca = presenca;
	}
	public boolean getUso() {
		return uso;
	}
	public void setUso(boolean uso) {
		this.uso = uso;
	}
	public long getTempoUltimoUso() {
		return tempoUltimoUso;
	}
	public void setTempoUltimoUso(long tempoUltimoUso) {
		this.tempoUltimoUso = tempoUltimoUso;
	}
	public int getNumQuadro() {
		return numQuadro;
	}
	public void setNumQuadro(int numQuadro) {
		this.numQuadro = numQuadro;
	}
	public int getNumPagina() {
		return numPagina;
	}
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}
}