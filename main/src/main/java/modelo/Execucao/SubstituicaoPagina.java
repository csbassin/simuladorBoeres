package modelo.Execucao;

import java.util.ArrayList;

import modelo.processo.ImagemProcesso;
import other.StaticObjects;
import modelo.tabelaPaginas.*;
import modelo.tabelaPaginas.EntradaTP;
import modelo.memoriaSecundaria.MemoriaSecundaria;

public class SubstituicaoPagina {

    public void substituirComLRU(int numPagina, String idProcesso){
        
        // pegar todos os processos, já que isso é global
        ArrayList<ImagemProcesso> imagens = StaticObjects.getAllProcessos();
        TabelaDePaginas tpDoSendoCarregado = null;
        String idProcessoDoASubstituir = null;
        EntradaTP entradaDoASubstituir = null;
        int numPaginaDoASubstituir = -1;
        long menorTempoUso = Long.MAX_VALUE;

        for(ImagemProcesso processo:imagens){
            if(idProcesso.equals(processo.getIdProcesso())){ // se pid = pid recebido, a tabela é do processo que vai ser carregado
                tpDoSendoCarregado = processo.getTabelaDePaginas();
            }

            EntradaTP[] entradas = processo.getTabelaDePaginas().getEntradas();
            for(int i = 0; i < entradas.length; i++) {
                EntradaTP entrada = entradas[i];
                if(entrada.getTempoUltimoUso() < menorTempoUso && entrada.getNumQuadro() > -1){
                    menorTempoUso = entrada.getTempoUltimoUso();
                    entradaDoASubstituir = entrada;
                    idProcessoDoASubstituir = processo.getIdProcesso();
                    numPaginaDoASubstituir = i; // LINHA DA CORREÇÃO
                }
            }
            // verificar o bit de modificação na entrada que será alterada
            if(entradaDoASubstituir.getModificacao()){ // precisa gravar em ms
                MemoriaSecundaria ms = StaticObjects.getMemoriaSecundaria();
                ms.gravar(idProcessoDoASubstituir, numPaginaDoASubstituir);
            }
            // depois de gravar em memória secundária, setar o número do quadro para -1
            int numQuadroSubs = entradaDoASubstituir.getNumQuadro();
            entradaDoASubstituir.setNumQuadro(-1);
            // agora, o quadro que liberamos passará a ter a página do cara que precisa ter a página carregada
            tpDoSendoCarregado.getEntradas()[numPagina].setNumQuadro(numQuadroSubs);
        }
    }


    public void substituirComClockTwoDigits(int numPagina, String idProcesso) {
        ArrayList<ImagemProcesso> imagens = StaticObjects.getAllProcessos();
        TabelaDePaginas tpDoSendoCarregado = null;

        // Localiza a tabela do processo que vai receber a nova página
        for (ImagemProcesso processo : imagens) {
            if (processo.getIdProcesso().equals(idProcesso)) {
                tpDoSendoCarregado = processo.getTabelaDePaginas();
                break;
            }
        }
        if (tpDoSendoCarregado == null) return;

        EntradaTP entradaParaSubstituir = null;
        ImagemProcesso processoASerSubstituido = null;
        int numPaginaSubstituida = -1;

        // ==== PASSO 1 ==== verificando se há bit u =0 e bit m=0
        outer:
        for (ImagemProcesso processo : imagens) {
            EntradaTP[] entradas = processo.getTabelaDePaginas().getEntradas();
            for (int i = 0; i < entradas.length; i++) {
                EntradaTP entrada = entradas[i];
                if (entrada.getNumQuadro() != -1 && !entrada.getUso() && !entrada.getModificacao()) {
                    entradaParaSubstituir = entrada;
                    processoASerSubstituido = processo;
                    numPaginaSubstituida = i;
                    break outer;
                }
            }
        }

        // ==== PASSO 2 ==== verificando se há bit u =0 e bit m=1
        if (entradaParaSubstituir == null) {
            outer:
            for (ImagemProcesso processo : imagens) {
                EntradaTP[] entradas = processo.getTabelaDePaginas().getEntradas();
                for (int i = 0; i < entradas.length; i++) {
                    EntradaTP entrada = entradas[i];
                    if (entrada.getNumQuadro() == -1) continue;

                    if (entrada.getUso()) {
                        entrada.setUso(false); // zera o uso
                    } else if (entrada.getModificacao()) {
                        entradaParaSubstituir = entrada;
                        processoASerSubstituido = processo;
                        numPaginaSubstituida = i;
                        break outer;
                    }
                }
            }
        }

        // ==== PASSO 3 ==== Se o passo 2 falhar, retornar ao passo 1
        if (entradaParaSubstituir == null) {
            outer:
            for (ImagemProcesso processo : imagens) {
                EntradaTP[] entradas = processo.getTabelaDePaginas().getEntradas();
                for (int i = 0; i < entradas.length; i++) {
                    EntradaTP entrada = entradas[i];
                    if (entrada.getNumQuadro() != -1 && !entrada.getUso() && !entrada.getModificacao()) {
                        entradaParaSubstituir = entrada;
                        processoASerSubstituido = processo;
                        numPaginaSubstituida = i;
                        break outer;
                    }
                }
            }
        }

        // ==== PASSO 4 ==== se passo 3 falhar executar passo 2
        if (entradaParaSubstituir == null) {
            outer:
            for (ImagemProcesso processo : imagens) {
                EntradaTP[] entradas = processo.getTabelaDePaginas().getEntradas();
                for (int i = 0; i < entradas.length; i++) {
                    EntradaTP entrada = entradas[i];
                    if (entrada.getNumQuadro() != -1 && !entrada.getUso() && entrada.getModificacao()) {
                        entradaParaSubstituir = entrada;
                        processoASerSubstituido = processo;
                        numPaginaSubstituida = i;
                        break outer;
                    }
                }
            }
        }

        // ==== VERIFICAÇÃO FINAL ====
        if (entradaParaSubstituir == null) {
            System.out.println("Nenhuma página elegível encontrada após 4 etapas.");
            return;
        }

        // Se a entrada a ser substituída estiver modificada, salva na MS
        if (entradaParaSubstituir.getModificacao()) {
            MemoriaSecundaria ms = StaticObjects.getMemoriaSecundaria();
            ms.gravar(processoASerSubstituido.getIdProcesso(), numPaginaSubstituida);
        }

        int quadroLiberado = entradaParaSubstituir.getNumQuadro();

        // Libera entrada antiga
        entradaParaSubstituir.setNumQuadro(-1);
        entradaParaSubstituir.setUso(false);
        entradaParaSubstituir.setModificacao(false);
        entradaParaSubstituir.setPresenca(false);

        // Carrega nova página
        EntradaTP novaEntrada = tpDoSendoCarregado.getEntradas()[numPagina];
        novaEntrada.setNumQuadro(quadroLiberado);
        novaEntrada.setUso(true);
        novaEntrada.setModificacao(false); // assume não modificada
        novaEntrada.setPresenca(true);
    }

}
