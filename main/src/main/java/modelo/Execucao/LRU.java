package modelo.Execucao;

import modelo.MemoriaPrincipal.MemoriaPrincipal;
import modelo.processo.ImagemProcesso;
import modelo.processo.PaginaProcesso;
import modelo.tabelaPaginas.EntradaTP;
import modelo.tabelaPaginas.TabelaDePaginas;
import java.util.LinkedList;
import java.util.ArrayList;

public class LRU {

    private int capacidade;               // Capacidade máxima de quadros
    private int faltaDePaginas;           // Contador de falhas de página
    private MemoriaPrincipal memoria;     // Referência à memória principal
    private LinkedList<Integer> ordemDeUsoLRU;  // Lista para armazenar a ordem de uso dos quadros
    private ArrayList<ImagemProcesso> imagensProcesso;  // ArrayList de imagens de processos

    public LRU(int capacidade, int faltaDePaginas, MemoriaPrincipal memoria, LinkedList<Integer> ordemDeUsoLRU, ArrayList<ImagemProcesso> imagensProcesso) {
        this.capacidade = capacidade;
        this.faltaDePaginas = faltaDePaginas;
        this.memoria = memoria;
        this.ordemDeUsoLRU = ordemDeUsoLRU;
        this.imagensProcesso = imagensProcesso;
    }

    // Método para acessar ou ocupar um quadro de memória
    public void acessarOuOcupar(Integer numQuadro, ImagemProcesso imagemProcesso) {
        // Se o quadro não está na memória ocupada, ele é uma falha de página
        if (!memoria.getQuadrosOcupados().contains(numQuadro)) {
            faltaDePaginas++;  // Incrementa a falha de página
            if (memoria.getQuadrosLivres().isEmpty()) {
                // Se não houver quadros livres, remove o quadro com menor tempo de uso
                Integer quadroLRU = removerQuadroComMenorTempo();
                System.out.println("Removendo quadro com menor tempo de uso: " + quadroLRU);
            }
            memoria.ocupar(numQuadro);  // Ocupar o quadro (movendo-o para os ocupados)
        }

        // Atualiza a ordem de uso (move o quadro para o final da lista)
        acessarQuadro(numQuadro);

        // Atualiza o tempo de uso na entrada da tabela de páginas
        atualizarTempoDeUso(numQuadro);

        // Adiciona ou atualiza a imagem do processo associado ao quadro
        imagensProcesso.add(imagemProcesso);
    }

    // Método para acessar o quadro e atualizar sua posição (mais recentemente usado)
    private void acessarQuadro(Integer numQuadro) {
        // Se o quadro já estiver na ordem, remove antes de reinseri-lo no final
        ordemDeUsoLRU.remove(numQuadro);
        ordemDeUsoLRU.addLast(numQuadro);  // Coloca no final, indicando que foi o mais recentemente usado
        System.out.println("Quadro " + numQuadro + " acessado.");
    }

    // Atualiza o tempo de uso da entrada na tabela de páginas
    private void atualizarTempoDeUso(Integer numQuadro) {
        // Aqui você deve encontrar a entrada correspondente ao quadro e atualizar o tempo
        for (ImagemProcesso processo : imagensProcesso) {
            for (PaginaProcesso pagina : processo.getPaginasProcesso()) {
                // ERRO !!!!!!!!!!!!!!!!!!
                if (pagina.getNumQuadro() == numQuadro) {
                    pagina.getEntrada().atualizarTempo();  // Atualiza o tempo de uso da entrada na tabela de páginas
                    return;
                }
            }
        }
    }

    // Método para remover o quadro com menor tempo de uso (mais antigo)
    private Integer removerQuadroComMenorTempo() {
        Integer quadroComMenorTempo = null;
        int menorTempo = Integer.MAX_VALUE;

        // Itera por todas as imagens de processo e suas páginas para encontrar o quadro com o menor tempo de uso
        for (ImagemProcesso processo : imagensProcesso) {
            for (PaginaProcesso pagina : processo.getPaginasProcesso()) {
                // ERRO !!!!!!!!!!!!!!!!!!
                EntradaTP entrada = pagina.getEntrada();
                if (entrada.getTempoUltimoUso() < menorTempo) {
                    menorTempo = entrada.getTempoUltimoUso();
                    // ERRO !!!!!!!!!!!!!!!!!!
                    quadroComMenorTempo = pagina.getNumQuadro();
                }
            }
        }

        if (quadroComMenorTempo != null) {
            memoria.liberar(quadroComMenorTempo);  // Libera o quadro com menor tempo de uso
        }
        return quadroComMenorTempo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public int getFaltaDePaginas() {
        return faltaDePaginas;
    }

    public void setFaltaDePaginas(int faltaDePaginas) {
        this.faltaDePaginas = faltaDePaginas;
    }

    public MemoriaPrincipal getMemoria() {
        return memoria;
    }

    public void setMemoria(MemoriaPrincipal memoria) {
        this.memoria = memoria;
    }

    public LinkedList<Integer> getOrdemDeUsoLRU() {
        return ordemDeUsoLRU;
    }

    public void setOrdemDeUsoLRU(LinkedList<Integer> ordemDeUsoLRU) {
        this.ordemDeUsoLRU = ordemDeUsoLRU;
    }

    public ArrayList<ImagemProcesso> getImagensProcesso() {
        return imagensProcesso;
    }

    public void setImagensProcesso(ArrayList<ImagemProcesso> imagensProcesso) {
        this.imagensProcesso = imagensProcesso;
    }
}
