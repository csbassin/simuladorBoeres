package modelo.Execucao;

import config.ConfigData;
import modelo.memoriaPrincipal.MemoriaPrincipal;
import modelo.tabelaPaginas.EntradaTP;
import modelo.tlb.TLB;
import other.*;
import visao.WindowData;

import javax.swing.*;
import java.util.Optional;

import modelo.tabelaPaginas.TabelaDePaginas;
import modelo.processo.ImagemProcesso;

public class CPU extends Thread {

	//partes principais
	private MemoriaPrincipal memoriaPrincipal;
	private Input input;
	private int numInstAtual = 0;
	public final TLB tlb = new TLB(ConfigData.qntdPagTlB);
	private final SubstituicaoPagina substPagina = new SubstituicaoPagina(this);

	// Filas para gerenciar o estado dos processos
	public final Fila<String> prontos = new Fila<>();
	public final Fila<String> bloqueados = new Fila<>();
	public final Fila<String> finalizados = new Fila<>();
	public final Fila<String> prontosSuspensos = new Fila<>();

	// Fila para interrupções do sistema
	private final Fila<Interrupcao> interrupcoes = new Fila<>();
	private String processoNaCPU;


	private boolean aguardandoIO = false;
	private String processoEmIO = null;


	@Override
	public void run() {
		memoriaPrincipal = StaticObjects.getMemoriaPrincipal();
		StaticObjects.getMemoriaSecundaria().start();

		try {
			input = new Input();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// Loop principal
		while (numInstAtual < input.getInstrucoes().size()) {
			try {
				WindowData.tempoLogico++;
				if (WindowData.stepByStepMode) {
					WindowData.stepSemaphore.acquire();
				} else {
					Thread.sleep(ConfigData.cicloCPU);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}

			tratarInterrupcoes();

			//se CPU estava esperando por I/O passa um ciclo
			if (aguardandoIO) {
				System.out.println("[OS] Operação de I/O concluída para o processo " + processoEmIO + ". Gerando interrupção de desbloqueio.");
				addFilaInterrupcoes(new Interrupcao(processoEmIO, Interrupcao.DESBLOQUEAR));
				aguardandoIO = false;
				processoEmIO = null;
				// O continue pula o resto do loop, incluindo o incremento da instrução.

				continue;
			}

			String instrucao = input.getInstrucoes().get(numInstAtual);
			WindowData.currentInstructionText = instrucao;
			String idProcessoDaInstrucao = instrucao.split(" ")[0];

			//caso de um processo que estava suspenso e precisa ser carregado na memória
			if (prontosSuspensos.contains(idProcessoDaInstrucao)) {
				System.out.println("[OS] Processo " + idProcessoDaInstrucao + " requisitado, iniciando operação de swap-in.");
				Optional<ImagemProcesso> processoOpt = StaticObjects.findProcessById(idProcessoDaInstrucao);
				if (processoOpt.isPresent()) {
					garantirPaginaNaMemoria(processoOpt.get(), 0, true);
					prontosSuspensos.remove(idProcessoDaInstrucao);
					prontos.enqueue(idProcessoDaInstrucao);
					processoOpt.get().getPcb().setEstado(Estados.PRONTO);
					System.out.println("[OS] Processo " + idProcessoDaInstrucao + " movido para a fila de Prontos.");
				}
			}

			// Se a instrução atual pertence a um processo que está bloqueado, a CPU fica ociosa (para não implementarmos um escalonador)
			if (bloqueados.contains(idProcessoDaInstrucao)) {
				WindowData.currentInstructionText = "(Aguardando desbloqueio de " + idProcessoDaInstrucao + "...)";
				System.out.println("CPU ociosa. Aguardando desbloqueio de processo...");

				continue;
			}

			executaComando(instrucao);

			numInstAtual++;
		}

		// acaba a simulação
		if (WindowData.btnNextStep != null) {
			SwingUtilities.invokeLater(() -> WindowData.btnNextStep.setEnabled(false));
		}

		WindowData.acabou = true;
		StaticObjects.getMemoriaSecundaria().interrupt();
		WindowData.currentInstructionText = "(simulação finalizada)";
		System.out.println("Simulação finalizada. Todos os processos foram terminados.");
	}

	// ldiar com a fila de interrupções
	private void tratarInterrupcoes() {
		while (!interrupcoes.isEmpty()) {
			Interrupcao interrupt = interrupcoes.unqueue();
			if (interrupt.getTipoInterrupcao() == Interrupcao.DESBLOQUEAR) {
				String idProcesso = interrupt.getIdProcesso();
				if (bloqueados.remove(idProcesso)) {
					prontos.enqueue(idProcesso);
					StaticObjects.findProcessById(idProcesso).ifPresent(p -> p.getPcb().setEstado(Estados.PRONTO));
					System.out.println("INTERRUPÇÃO: Processo " + idProcesso + " foi desbloqueado e movido para a fila de Prontos.");
				}
			}
		}
	}

	private void criaProcesso(String processId, int tamanhoProcessoBytes) {
		if (StaticObjects.findProcessById(processId).isPresent()) {
			System.err.println("Processo " + processId + " já existe.");
			return;
		}

		ImagemProcesso imagemProcesso = new ImagemProcesso();
		imagemProcesso.setIdProcesso(processId);

		int quantidadePaginasProcesso = (int) Math.ceil((double) tamanhoProcessoBytes / ConfigData.quadroSize);
		if (quantidadePaginasProcesso == 0) quantidadePaginasProcesso = 1;

		TabelaDePaginas tp = new TabelaDePaginas(quantidadePaginasProcesso);
		tp.setIdProcesso(processId);
		imagemProcesso.setTabelaDePaginas(tp);

		StaticObjects.getAllProcessos().add(imagemProcesso);
		if(WindowData.vtp != null) WindowData.vtp.addToComboProcessos(processId);

		// Tenta alocar um quadro livre na memória principal
		Integer quadroAlocado = memoriaPrincipal.alocarQuadroLivre();
		if (quadroAlocado != null) {
			// Se tivere espaço, o processo é colocado na fila de prontos
			System.out.println("[OS] Alocando processo " + processId + " na memória principal (Quadro " + quadroAlocado + ").");
			EntradaTP paginaZero = imagemProcesso.getTabelaDePaginas().getEntradaPagina(0);
			paginaZero.setNumQuadro(quadroAlocado);
			paginaZero.setPresenca(true);
			paginaZero.setUso(true);
			paginaZero.setTempoUltimoUso(WindowData.tempoLogico);

			prontos.enqueue(imagemProcesso.getIdProcesso());
			imagemProcesso.getPcb().setEstado(Estados.PRONTO);
			System.out.println("Processo " + processId + " criado com " + quantidadePaginasProcesso + " páginas e movido para Prontos.");
		} else {
			// Senão, o processo vai para a fila de prontos-suspensos.
			prontosSuspensos.enqueue(imagemProcesso.getIdProcesso());
			imagemProcesso.getPcb().setEstado(Estados.PRONTOSUSPENSO);
			System.out.println("Memória principal cheia. Processo " + processId + " criado com " + quantidadePaginasProcesso + " páginas e movido para Pronto-Suspenso.");
		}
	}

	private void garantirPaginaNaMemoria(ImagemProcesso processo, int numPagina, boolean isSwapIn) {
		EntradaTP pagina = processo.getTabelaDePaginas().getEntradaPagina(numPagina);

		if(pagina.getPresenca()){
			return;
		}

		if(isSwapIn) { //isso é pra permitir que ele execute a insturção
			System.out.println("\n[OS] Swap-in forçado para Processo: " + processo.getIdProcesso() + ", Página: " + numPagina);
		}

		Integer quadroAlocado = memoriaPrincipal.alocarQuadroLivre();
		if (quadroAlocado != null) {
			System.out.println("Quadro livre " + quadroAlocado + " alocado para a página " + numPagina);
			pagina.setNumQuadro(quadroAlocado);
		} else {
			//não tem quadros livres -> algoritmo de substituição de página
			System.out.println("Não há quadros livres. Iniciando algoritmo de substituição de página (" + ConfigData.tipoSubstituicaoPaginas + ")...");
			if ("LRU".equals(ConfigData.tipoSubstituicaoPaginas)) {
				substPagina.substituirComLRU(numPagina, processo);
			} else {
				substPagina.substituirComClock(numPagina, processo);
			}
		}

		pagina.setPresenca(true);
		pagina.setUso(true);
		pagina.setTempoUltimoUso(WindowData.tempoLogico);
		tlb.adicionarEntrada(pagina, processo.getIdProcesso());
	}

	private void executaComando(String instrucao) {
		String[] partes = instrucao.split(" ");
		String processoId = partes[0];
		String comando = partes[1];

		Optional<ImagemProcesso> processoSelecionado = StaticObjects.findProcessById(processoId);

		// troca de contexto se a instrução for de um processo diferente do que tá na CPU
		if (processoSelecionado.isEmpty() && !comando.equals("C")) {
			System.err.println("Processo " + processoId + " não encontrado para a instrução: " + instrucao);
			return;
		}

		if (!comando.equals("C") && processoNaCPU != null && !processoId.equals(processoNaCPU)) {
			tlb.trocaDeProcesso(processoId);
			System.out.println("\nTroca de contexto -> TLB invalidada. Novo processo na CPU: " + processoId);
		}
		processoNaCPU = processoId;

		ImagemProcesso processo = processoSelecionado.orElse(null);

		if(processo != null && processo.getPcb().getEstado() != Estados.FINALIZADO) {
			processo.getPcb().setEstado(Estados.EXECUTANDO);
		}

		switch (comando) {
			case "C":
				int tamanhoProcesso = Integer.parseInt(partes[2]);
				if (partes.length > 3) {
					String unidade = partes[3];
					tamanhoProcesso = (int) Conversoes.convererterUnidade(tamanhoProcesso, unidade, "B");
				}
				criaProcesso(processoId, tamanhoProcesso);
				break;

			case "R":
			case "P":
				acessarEndereco(Long.parseLong(partes[2]), processo, false);
				break;

			case "W":
				acessarEndereco(Long.parseLong(partes[2]), processo, true);
				break;

			case "I":
				System.out.println("[OS] Processo " + processo.getIdProcesso() + " iniciou uma operação de I/O. Movendo para Bloqueados.");
				processo.getPcb().setEstado(Estados.BLOQUEADO);
				prontos.remove(processo.getIdProcesso());
				bloqueados.enqueue(processo.getIdProcesso());

				this.aguardandoIO = true;
				this.processoEmIO = processo.getIdProcesso();
				WindowData.currentInstructionText = "(Aguardando I/O do Processo " + processoEmIO + "...)";
				break;

			case "T":

				// Só finaliza o processo se ele ainda não estiver finalizado.
				if (processo != null && processo.getPcb().getEstado() != Estados.FINALIZADO) {
					System.out.println("Processo " + processo.getIdProcesso() + " terminando.");
					for (EntradaTP entrada : processo.getTabelaDePaginas().getEntradas()) {
						if (entrada.getPresenca()) {
							if (entrada.getModificacao()) {
								StaticObjects.getMemoriaSecundaria().gravar(processo.getIdProcesso(), entrada.getNumPagina());
							}
							StaticObjects.getMemoriaPrincipal().liberar(entrada.getNumQuadro());
							tlb.invalidarEntrada(entrada.getNumPagina(), processo.getIdProcesso());
						}
					}
					prontos.remove(processo.getIdProcesso());
					bloqueados.remove(processo.getIdProcesso());
					prontosSuspensos.remove(processo.getIdProcesso());

					finalizados.enqueue(processo.getIdProcesso());
					processo.getPcb().setEstado(Estados.FINALIZADO);
					if(processoId.equals(processoNaCPU)) {
						processoNaCPU = null;
					}
				}

				break;
		}

		if (processo != null && (processo.getPcb().getEstado() == Estados.EXECUTANDO)) {
			processo.getPcb().setEstado(Estados.PRONTO);
		}
	}

	public EntradaTP acessarEndereco(long enderecoLogico, ImagemProcesso processo, boolean modificacao) {
		if (processo == null) return null;

		int numPagina = (int) (enderecoLogico / ConfigData.quadroSize);

		// Verifica se o acesso é a uma página válida do processo
		if (numPagina >= processo.getTabelaDePaginas().getEntradas().length) {
			System.err.println("!!! ERRO: Processo " + processo.getIdProcesso() + " tentou acessar página inválida ("+numPagina+"). SEGMENTATION FAULT.");

			prontos.remove(processo.getIdProcesso());
			bloqueados.remove(processo.getIdProcesso());
			prontosSuspensos.remove(processo.getIdProcesso());

			// Adiciona à fila de finalizados apenas se já não estiver lá
			if(!finalizados.contains(processo.getIdProcesso())) {
				finalizados.enqueue(processo.getIdProcesso());
			}
			processo.getPcb().setEstado(Estados.FINALIZADO);

			for (EntradaTP entrada : processo.getTabelaDePaginas().getEntradas()) {
				if (entrada.getPresenca()) {
					StaticObjects.getMemoriaPrincipal().liberar(entrada.getNumQuadro());
					tlb.invalidarEntrada(entrada.getNumPagina(), processo.getIdProcesso());
				}
			}

			if(processo.getIdProcesso().equals(processoNaCPU)) {
				processoNaCPU = null;
			}
			return null;
		}

		// Primeiro tentatamos encontrar a página na TLB
		Optional<EntradaTP> tlbResult = tlb.consulta(numPagina, processo.getIdProcesso());
		if (tlbResult.isPresent()) {
			System.out.println("TLB HIT! Processo: " + processo.getIdProcesso() + ", Página: " + numPagina);
			EntradaTP entrada = tlbResult.get();
			entrada.setUso(true);
			entrada.setTempoUltimoUso(WindowData.tempoLogico);
			if (modificacao) {
				entrada.setModificacao(true);
			}
			return entrada;
		}

		System.out.println("TLB MISS. Consultando Tabela de Páginas... Processo: " + processo.getIdProcesso() + ", Página: " + numPagina);
		EntradaTP entradaTP = processo.getTabelaDePaginas().getEntradaPagina(numPagina);
		if (entradaTP.getPresenca()) {
			System.out.println("PAGE TABLE HIT! Página " + numPagina + " está no quadro " + entradaTP.getNumQuadro());
			entradaTP.setUso(true);
			entradaTP.setTempoUltimoUso(WindowData.tempoLogico);
			if (modificacao) {
				entradaTP.setModificacao(true);
			}
			tlb.adicionarEntrada(entradaTP, processo.getIdProcesso());
			return entradaTP;
		}

		//page fault se pagina não esta na memoria
		System.out.println("!!! PAGE FAULT! Processo: " + processo.getIdProcesso() + ", Página: " + numPagina);
		WindowData.ocorreuPageFault();

		Integer quadroAlocado = memoriaPrincipal.alocarQuadroLivre();
		if (quadroAlocado != null) {
			System.out.println("Quadro livre " + quadroAlocado + " alocado para a página " + numPagina);
			entradaTP.setNumQuadro(quadroAlocado);
		} else {
			System.out.println("Não há quadros livres. Iniciando algoritmo de substituição de página (" + ConfigData.tipoSubstituicaoPaginas + ")...");
			if ("LRU".equals(ConfigData.tipoSubstituicaoPaginas)) {
				substPagina.substituirComLRU(numPagina, processo);
			} else {
				substPagina.substituirComClock(numPagina, processo);
			}
		}

		entradaTP.setPresenca(true);
		entradaTP.setUso(true);
		entradaTP.setModificacao(modificacao);
		entradaTP.setTempoUltimoUso(WindowData.tempoLogico);
		tlb.adicionarEntrada(entradaTP, processo.getIdProcesso());

		return entradaTP;
	}

	public void addFilaInterrupcoes(Interrupcao interrupcao) {
		this.interrupcoes.enqueue(interrupcao);
	}
}