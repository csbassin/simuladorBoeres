package modelo.tlb;

import modelo.tabelaPaginas.EntradaTP;

public class EntradaTLB {
	private boolean valid;
    private int numPagina;

    private int tempoUltimoUso;
    private EntradaTP entradaTP;

    private boolean modificacao;
    private boolean presenca;
    private int numQuadro;

    public EntradaTLB(EntradaTP entradaTP) {
        this.numPagina = entradaTP.getNumPagina();
        this.entradaTP = entradaTP;

        this.tempoUltimoUso = 0;
        modificacao = entradaTP.getModificacao();
        presenca = entradaTP.getPresenca();
        numQuadro = entradaTP.getNumQuadro();
        valid = true;
    }

    public int getNumPagina() {
        return numPagina;
    }

    public boolean isPresenca() {
        return presenca;
    }

    public int getNumQuadro() {
        return numQuadro;
    }

    public int getTempoUltimoUso() {
        return tempoUltimoUso;
    }

    public void zerarTempoUltimoUso(){
        tempoUltimoUso = 0;
    }
    public void incrementarTempoUltimoUso(){
        tempoUltimoUso++;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isModificacao() {
        return modificacao;
    }

    public void setModificacao(boolean modificacao) {
        this.modificacao = modificacao;
    }


}
