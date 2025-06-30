package modelo.tlb;

import modelo.tabelaPaginas.EntradaTP;
import java.util.ArrayList;

public class TLB {
    private int qtdPaginas;
    private ArrayList<EntradaTLB> entradas;


    public TLB(int qtdPaginas) {
        this.qtdPaginas = qtdPaginas;
        entradas = new ArrayList<>(qtdPaginas);
    }

    private void substituicaoLRU(EntradaTLB entradaTLB){
        //Será usado LRU para substituir as entradas da TLB //Por uma logica com a modificação aq tambem?
        int maior_indice = 0;
        for (int i = 0; i<qtdPaginas; i++) {
            if(entradas.get(i).getTempoUltimoUso() > maior_indice)
                maior_indice = i;
        }
        entradas.set(maior_indice, entradaTLB);
    }

    public void adicionarEntrada(EntradaTP entradaTP) {
        //Adiciona se estiver vazio e se não estiver substitui
        EntradaTLB entradaTLB = new EntradaTLB(entradaTP);
        if (entradas.size() < qtdPaginas)
            entradas.add(entradaTLB);
        else
            substituicaoLRU(entradaTLB);
    }

    public boolean consulta(int numPagina) {
        //Consultar entrada na TLB
        boolean consulta = false;
        for (EntradaTLB entrada: entradas) {
            if (numPagina == entrada.getNumPagina() && entrada.isValid()) {
                entrada.zerarTempoUltimoUso();
                consulta = true;
            }
            else
                entrada.incrementarTempoUltimoUso();
        }
        return consulta;
    }

    public void trocaDeProcesso(){
        //No caso de uma troca de processo a valdiade das entradas é zerada
        for (EntradaTLB entrada: entradas) {
            entrada.setValid(false);
            entrada.zerarTempoUltimoUso();
        }

    }

    public boolean getModificacao(int numPagina){
        for (EntradaTLB entrada: entradas) {
            if (numPagina == entrada.getNumPagina())
                return entrada.isModificacao();
        }
        return false;
    }

    public void marcarModificacao(int numPagina) {
        for (EntradaTLB entrada: entradas) {
            if (numPagina == entrada.getNumPagina())
                entrada.setModificacao(true);
        }
    }
}
