package modelo.processo;

import java.util.ArrayList;

import config.configData;
import modelo.tabelaPaginas.TabelaDePaginas;

public class ImagemProcesso {
    private String idProcesso;
    private TabelaDePaginas tabelaDePaginas;
    private ArrayList<PaginaProcesso> paginasProcesso = new ArrayList<>(configData.quantidadeQuadros); //começa o array com tamanho máximo pra não precisar realocar
}
