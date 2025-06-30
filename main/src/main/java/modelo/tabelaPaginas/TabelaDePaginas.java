package modelo.tabelaPaginas;

import modelo.memoriaPrincipal.MemoriaPrincipal;

import java.util.ArrayList;

import static other.StaticObjects.getMemoriaPrincipal;

public class TabelaDePaginas {
    private String idProcesso;
    private EntradaTP[] entradas;
    
    public TabelaDePaginas(int quantidadePaginasProcesso) {
    	entradas = new EntradaTP[quantidadePaginasProcesso];

    	for(int i = 0; i<quantidadePaginasProcesso; i++) {
    		entradas[i] = new EntradaTP(false, false, false, 0, -1, i);
    	}
    }

    public String getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(String idProcesso) {
        this.idProcesso = idProcesso;
    }

    public EntradaTP[] getEntradas() {
        return entradas;
    }

    public void setEntradas(EntradaTP[] entradas) {
        this.entradas = entradas;
    }

    public void setNumQuadro(int numQuadro, int pagina) {
    	entradas[pagina].setNumQuadro(numQuadro);
    }
    public void setModificado(boolean modificado, int pagina) {
    	entradas[pagina].setModificacao(modificado);
    }
    public void setPresenca(boolean presente, int pagina) {
    	entradas[pagina].setPresenca(presente);
    }
    public void setUltimoUso(int ultimoUso, int pagina) {
    	entradas[pagina].setTempoUltimoUso(ultimoUso);
    }
    public void setUso(boolean uso, int pagina) {
    	entradas[pagina].setUso(uso);
    }
    
    public int getNumQuadro(int pagina) {
    	return entradas[pagina].getNumQuadro();
    }
    public boolean getModificado(int pagina) {
    	return entradas[pagina].getModificacao();
    }
    public boolean getPresenca(int pagina) {
    	return entradas[pagina].getPresenca();
    }
    public int getUltimoUso(int ultimoUso, int pagina) {
    	return entradas[pagina].getTempoUltimoUso();
    }
    public boolean getUso(int pagina) {
    	return entradas[pagina].getUso();
    }

    public EntradaTP getEntradaPagina(int numPag) {
        return entradas[numPag];
    }

    public boolean consulta(int numPagina) {
        //Consultar entrada na tp
        return entradas[numPagina].getPresenca();
    }
}
