package other;

import config.ConfigData;
import modelo.Execucao.CPU;
import modelo.processo.ImagemProcesso;

public class EnderecoTraduzido {

    private long enderecoFisico;
    private long numeroPagina;
    private int numeroQuadro;
    private long offset;
    private ImagemProcesso processo;

    public EnderecoTraduzido(long enderecoLogico, ImagemProcesso processo) {
        this.processo = processo;

        this.numeroPagina = enderecoLogico / ConfigData.quadroSize;
        this.offset = enderecoLogico % ConfigData.quadroSize;
        this.numeroQuadro = processo.getTabelaDePaginas().getNumQuadro((int) this.numeroPagina);
        this.enderecoFisico = (long) this.numeroQuadro * ConfigData.quadroSize + this.offset;
    }

    public void recalcularEndFisico(){
        this.numeroQuadro = processo.getTabelaDePaginas().getNumQuadro((int) this.numeroPagina);
        this.enderecoFisico = (long) this.numeroQuadro * ConfigData.quadroSize + this.offset;
    }

    public long getEnderecoFisico() {
        return enderecoFisico;
    }

    public long getNumeroPagina() {
        return numeroPagina;
    }

    public int getNumeroQuadro() {
        return numeroQuadro;
    }

    public long getOffset() {
        return offset;
    }
}