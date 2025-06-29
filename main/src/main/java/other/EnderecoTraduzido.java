package other;

import config.ConfigData;
import modelo.Execucao.CPU;
import modelo.processo.ImagemProcesso;

public class EnderecoTraduzido {

    private final long enderecoFisico;
    private final long numeroPagina;
    private final int numeroQuadro;
    private final long offset;

    public EnderecoTraduzido(long enderecoLogico, ImagemProcesso processo) throws InterruptedException {


        long qntdBitsOffset = (int) Math.ceil(Math.log(
                Conversoes.convererterUnidade(ConfigData.quadroSize, "B", "b")
        )/Math.log(2));
        this.numeroPagina = enderecoLogico / qntdBitsOffset;
        this.offset = enderecoLogico % qntdBitsOffset;
        this.numeroQuadro = processo.getTabelaDePaginas().getNumQuadro((int) this.numeroPagina);
        this.enderecoFisico = (long) this.numeroQuadro * qntdBitsOffset + this.offset;
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