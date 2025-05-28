package modelo.tabelaPaginas;

public class EntradaTP {
    private boolean modificacao;
    private boolean presenca;
    private boolean uso;
    private int tempoUltimoUso;
    private int numQuadro;
    
    public EntradaTP(boolean modificacao, boolean presenca, boolean uso, int tempoUltimoUso, int numQuadro ) {
    	this.modificacao = modificacao;
    	this.presenca = presenca;
    	this.uso = uso;
    	this.tempoUltimoUso = tempoUltimoUso;
    	this.numQuadro = numQuadro;
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
	public int getTempoUltimoUso() {
		return tempoUltimoUso;
	}
	public void setTempoUltimoUso(int tempoUltimoUso) {
		this.tempoUltimoUso = tempoUltimoUso;
	}
	public int getNumQuadro() {
		return numQuadro;
	}
	public void setNumQuadro(int numQuadro) {
		this.numQuadro = numQuadro;
	}
    
    
}
