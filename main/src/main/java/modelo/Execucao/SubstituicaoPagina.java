package modelo.Execucao;

import config.ConfigData;
import modelo.memoriaPrincipal.MemoriaPrincipal;
import modelo.processo.ImagemProcesso;
import other.Estados;
import other.StaticObjects;
import modelo.tabelaPaginas.*;
import visao.WindowData;
import java.util.ArrayList;

public class SubstituicaoPagina {
    private final CPU cpu;
    private int ponteiro_clock = 0;

    public SubstituicaoPagina(CPU cpu) {
        this.cpu = cpu;
    }

    public void substituirComLRU(int numPaginaNova, ImagemProcesso processoNovo) {
        ArrayList<ImagemProcesso> imagens = StaticObjects.getAllProcessos();
        EntradaTP entradaParaSubstituir = null;
        ImagemProcesso processoDaPaginaSubstituida = null;
        long menorTempoUso = Long.MAX_VALUE;
        WindowData.ponteiroClock = -1;
        // Encontra a página menos recentemente usada
        for (ImagemProcesso processo : imagens) {
            for (EntradaTP entrada : processo.getTabelaDePaginas().getEntradas()) {
                if (entrada.getPresenca() && entrada.getTempoUltimoUso() < menorTempoUso) {
                    menorTempoUso = entrada.getTempoUltimoUso();
                    entradaParaSubstituir = entrada;
                    processoDaPaginaSubstituida = processo;
                }
            }
        }

        if (entradaParaSubstituir == null) {
            System.err.println("Erro na substituição LRU: não foi possível encontrar uma página para substituir.");
            return;
        }

        System.out.println("Substituição LRU: Página " + entradaParaSubstituir.getNumPagina() +
                " do Processo " + processoDaPaginaSubstituida.getIdProcesso() + " será substituída.");

        int quadroLiberado = descarregarPagina(entradaParaSubstituir, processoDaPaginaSubstituida);
        carregarPagina(quadroLiberado, numPaginaNova, processoNovo);
    }

    public void substituirComClock(int numPaginaNova, ImagemProcesso processoNovo) {
        MemoriaPrincipal mp = StaticObjects.getMemoriaPrincipal();
        EntradaTP entradaParaSubstituir = null;
        ImagemProcesso processoDaPaginaSubstituida = null;

        while (entradaParaSubstituir == null) {
            int quadroAtual = ponteiro_clock;
            WindowData.ponteiroClock = quadroAtual;
            ImagemProcesso pTemp = null;
            EntradaTP eTemp = null;

            //  Busca a página que ocupa o quadro apontado pelo ponteiro do relógio
            search:
            for (ImagemProcesso p : StaticObjects.getAllProcessos()) {
                for (EntradaTP e : p.getTabelaDePaginas().getEntradas()) {
                    if (e.getPresenca() && e.getNumQuadro() == quadroAtual) {
                        pTemp = p;
                        eTemp = e;
                        break search;
                    }
                }
            }

            // Avança o ponteiro para a próxima iteração
            ponteiro_clock = (ponteiro_clock + 1) % ConfigData.quantidadeQuadros;

            if (eTemp != null) {
                if (!eTemp.getUso()) { // Bit de uso é 0 -> Encontrou o alvo
                    entradaParaSubstituir = eTemp;
                    processoDaPaginaSubstituida = pTemp;
                } else { // Bit de uso é 1 -> "segunda chance"
                    eTemp.setUso(false); // Zera o bit de uso
                }
            }
        }

        System.out.println("Substituição Clock: Página " + entradaParaSubstituir.getNumPagina() +
                " do Processo " + processoDaPaginaSubstituida.getIdProcesso() + " será substituída (Quadro " + entradaParaSubstituir.getNumQuadro() + ").");

        int quadroLiberado = descarregarPagina(entradaParaSubstituir, processoDaPaginaSubstituida);
        carregarPagina(quadroLiberado, numPaginaNova, processoNovo);
    }

    private int descarregarPagina(EntradaTP paginaAntiga, ImagemProcesso processoAntigo) {
        // Se a página foi modificada, salva ela na memória secundária
        if (paginaAntiga.getModificacao()) {
            if (!WindowData.stepByStepMode) {
                try { Thread.sleep(ConfigData.tempoAcessoMS); } catch (InterruptedException e) {}
            }
            StaticObjects.getMemoriaSecundaria().gravar(processoAntigo.getIdProcesso(), paginaAntiga.getNumPagina());
        }

        int numQuadro = paginaAntiga.getNumQuadro();

        // Invalida a entrada da página na tabela de páginas
        paginaAntiga.setPresenca(false);
        paginaAntiga.setUso(false);
        paginaAntiga.setModificacao(false);
        paginaAntiga.setNumQuadro(-1);

        // Remove da TLB se estiver lá
        cpu.tlb.invalidarEntrada(paginaAntiga.getNumPagina(), processoAntigo.getIdProcesso());


        // Verifica se o processo antigo ainda tem alguma página na memória
        boolean algumaPaginaPresente = false;
        for (EntradaTP entrada : processoAntigo.getTabelaDePaginas().getEntradas()) {
            if (entrada.getPresenca()) {
                algumaPaginaPresente = true;
                break;
            }
        }

        // Se nenhuma página estiver presente-> processo suspenso
        if (!algumaPaginaPresente) {
            // Só suspende se não estiver já bloqueado ou finalizado
            if(processoAntigo.getPcb().getEstado() == Estados.PRONTO || processoAntigo.getPcb().getEstado() == Estados.EXECUTANDO) {
                processoAntigo.getPcb().setEstado(Estados.PRONTOSUSPENSO);
                cpu.prontos.remove(processoAntigo.getIdProcesso());
                cpu.prontosSuspensos.enqueue(processoAntigo.getIdProcesso());
                System.out.println("[OS] Processo " + processoAntigo.getIdProcesso() + " foi totalmente removido da memória e movido para Pronto-Suspenso.");
            }
        }


        return numQuadro;
    }

    private void carregarPagina(int numQuadro, int numPaginaNova, ImagemProcesso processoNovo) {
        EntradaTP paginaNova = processoNovo.getTabelaDePaginas().getEntradaPagina(numPaginaNova);
        paginaNova.setNumQuadro(numQuadro);
        // O resto dos atributos (presença, uso...) são setados no método acessarEndereco la na CPU
        System.out.println("Página " + numPaginaNova + " do Processo " + processoNovo.getIdProcesso() + " será carregada no quadro " + numQuadro);
    }
}