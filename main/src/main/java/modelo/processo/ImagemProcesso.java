package modelo.processo;

import config.configData;
import modelo.tabelaPaginas.TabelaDePaginas;

public class ImagemProcesso {
    private int idProcesso;

    private TabelaDePaginas tabelaDePaginas;
    private PaginaProcesso[] paginasProcesso = new PaginaProcesso[configData.quantidadePaginas];
}
