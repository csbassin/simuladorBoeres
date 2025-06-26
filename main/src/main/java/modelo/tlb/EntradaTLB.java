package main.java.modelo.tlb;

import main.java.modelo.tabelaPaginas.EntradaTP;

public class EntradaTLB {
	private boolean valid;	// quando houver uma troca de processo, o bit de validade deve ser setado para 1
    private int numPagina;

    //Usaremos LRU, em que o tempo de ultimo uso é um contador
    private int tempoUltimoUso; // essa tem relação com a política de susbtituição da TLB, e não da tabela de páginas
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
