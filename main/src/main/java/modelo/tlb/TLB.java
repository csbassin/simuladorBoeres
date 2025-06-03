package modelo.tlb;

import java.util.ArrayList;

public class TLB {
    private int qtdPaginas;
    private ArrayList<EntradaTLB> entradas;


    public TLB(int qtdPaginas) {
        this.qtdPaginas = qtdPaginas;
        entradas = new ArrayList<>(qtdPaginas);
    }

    private void substituicaoLRU(EntradaTLB entradaTLB){
        int maior_indice = 0;
        for (int i = 0; i<qtdPaginas; i++) {
            if(entradas.get(i).getTempoUltimoUso() > maior_indice)
                maior_indice = i;
        }
        entradas.set(maior_indice, entradaTLB);
    }

    public void adicionarEntrada(EntradaTLB entradaTLB) {
        if (entradas.size() < qtdPaginas)
            entradas.add(entradaTLB);
        else
            substituicaoLRU(entradaTLB);
    }



}
