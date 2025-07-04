package modelo.Execucao;

import config.ConfigData;
import modelo.memoriaPrincipal.MemoriaPrincipal;
import modelo.tabelaPaginas.EntradaTP;
import modelo.tlb.TLB;
import other.*;
import visao.ViewTLB;
import visao.ViewTabelaPaginas;

import java.util.HashMap;
import java.util.Objects;

import modelo.tabelaPaginas.TabelaDePaginas;

import modelo.processo.ImagemProcesso;

public class CPU extends Thread{
	
	private MemoriaPrincipal memoriaPrincipal = null;
	private Input input = null;
	private int numInstAtual = 0; // usada apenas para parar a simulação quando acabarem as instruções. Devemos incrementar individualmente o ponteiro em cada processo
	TLB tlb = new TLB(ConfigData.qntdPagTlB);
	Fila<String> prontos = new Fila<>();
	Fila<String> bloqueados = new Fila<>();
	Fila<String> novos = new Fila<>();
	Fila<String> bloqueadosSuspensos = new Fila<>();
	Fila<String> prontosSuspensos = new Fila<>();
	Fila<String> finalizados = new Fila<>();
	private Fila<Interrupcao> interrupcoes = new Fila<>();
	private SubstituicaoPagina substPagina = new SubstituicaoPagina();
	
	String executando;
	int processosCriados = 0;
	int processosFinalizados = 0;
	
	
	@Override
	public void run() {
		/* etapas do algoritimo:
		 	1- ler a instrução. Os dois primeiros representam qual o processo. O primeiro depois do espaço é o tipo de instrução
		 		P: instução de CPU (marca a página do endereço como modificada);
		 		I: instrução de IO (deve bloquear o processo atual)
		 		C: cria um processo
		 		R/W: Leitura/Escrita na memória principal (R altera bit de uso e W altera bit de modificação)
		 		T: Terminação de processo
		 */
		
		memoriaPrincipal = StaticObjects.getMemoriaPrincipal();
		try {
			input = new Input();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// enquanto houver instruções para executar
		while(input.getInstrucoesPendentes()>0) {
			while(!interrupcoes.isEmpty()) {
				Interrupcao interrupt = interrupcoes.unqueue();
				if(interrupt.getTipoInterrupcao() == Interrupcao.DESBLOQUEAR) {
					bloqueados.remove(interrupt.getIdProcesso());
					prontos.enqueue(interrupt.getIdProcesso());
				}
			}
			String instrucao = input.getInstrucoes().get(numInstAtual);
			if(bloqueados.isEmpty() && bloqueadosSuspensos.isEmpty() && prontosSuspensos.isEmpty()) { // se não houver nenhuma restrição
				try {
					executaComando(instrucao);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				String processoId = instrucao.substring(0, instrucao.indexOf(' '));
				if(bloqueadosSuspensos.contains(processoId) || bloqueados.contains(processoId) || prontosSuspensos.contains(processoId)) {
					if(!(prontos.isEmpty())) {
						selecionarOutroParaExecucao();
					}
				}else {
					try {
					executaComando(instrucao);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}			
		}
		
		
		
		
	}
	private TabelaDePaginas criaTabelaDePaginas(String processId, int quantidadePaginasProcesso) {
		TabelaDePaginas tp = new TabelaDePaginas(quantidadePaginasProcesso);

		return tp;
	}
	private ImagemProcesso criaProcesso(String processId, int tamanhoProcessoBytes) {
		ImagemProcesso imagemProcesso = new ImagemProcesso();
		imagemProcesso.setIdProcesso(processId);
		imagemProcesso.getPcb().setEstado(String.valueOf(Estados.NOVO));
		novos.enqueue(imagemProcesso.getIdProcesso());
		//sleep algum tempo TODO


		int quadrosLivres = memoriaPrincipal.getQuadrosLivres().size();
		int quantidadeQuadrosProcesso = (int) Math.ceil(tamanhoProcessoBytes/ ConfigData.quadroSize);

		novos.remove(imagemProcesso.getIdProcesso());

		TabelaDePaginas tp = criaTabelaDePaginas(processId, quantidadeQuadrosProcesso);
		tp.setIdProcesso(processId);
		imagemProcesso.setTabelaDePaginas(tp);
		if(quadrosLivres == 0) { // não tem espaço para alocar ao menos uma página do processo (novo->pronto suspenso direto? ou somenete novo)
			imagemProcesso.getPcb().setEstado(String.valueOf(Estados.PRONTOSUSPENSO));
			prontosSuspensos.enqueue(imagemProcesso.getIdProcesso());
			return imagemProcesso;
		}else { // tem espaço para alocar pelo menos uma página do processo
			Integer quadroPagina = memoriaPrincipal.getQuadrosLivres().getFirst();
			memoriaPrincipal.ocupar(quadroPagina);
			imagemProcesso.getPcb().setEstado(String.valueOf(Estados.PRONTO));
			prontos.enqueue(imagemProcesso.getIdProcesso());
			imagemProcesso.getTabelaDePaginas().getEntradaPagina(0).setNumQuadro(quadroPagina);
			imagemProcesso.getTabelaDePaginas().getEntradaPagina(0).setPresenca(true);
		}
		StaticObjects.getVTP().addToComboProcessos(processId);
		StaticObjects.getAllProcessos().add(imagemProcesso);
		return imagemProcesso;
	}

	private void executaComando(String instrucao) throws InterruptedException {
//		Não implementarei preempção porque precisaríamos fazer um escalonador pra isso
//		 Funcionamento: Mantenho um inteiro em cada processo indicando qual o número da instrução atual.
//		 Mantenho a ordem de qual processo deve vir primeiro na entrada, para podermos manter o momento da submissão

			HashMap<String, String> dados_instrucao = new HashMap<String, String>();
			String[] chaves = {"processo_id", "instrucao", "valor", "unidade"};
			String[] strings_sep = instrucao.split(" ");

			for (int i =0; i < strings_sep.length; i++) {
				dados_instrucao.put(chaves[i], strings_sep[i]);
			}


			if(Objects.equals(dados_instrucao.get("instrucao"), "C")) { // criação do processo


				ImagemProcesso processo = criaProcesso(dados_instrucao.get("processo_id").substring(1),
                        (int) Conversoes.convererterUnidade(Long.parseLong(dados_instrucao.get("valor")),
								dados_instrucao.get("unidade"), "B"));
//				 	Criaremos uma instância de processo e colocaremos seu estado como novo

//				 	varre o arrayList de quadros livres.
//
//				 	Se houver uma quantidade contígua suficiente de quadros livres para colocar a tabela de páginas, aloca a tabela de páginas nesses quadros e cria o ponteiro
//				 	Se não houver, não pode criar o processo
//				 	Passaremos a ignorar tudo desse processo no arquivo de entrada (porque o processo não foi criado)
//
//				 	Se houver, alocamos esse quadro para a página. Seu estado passa para "Pronto"
//				 	Se não houver, o processo permanece com o estado novo e é colocado em uma fila para alocação futura

			}
			else if(Objects.equals(dados_instrucao.get("instrucao"), "P")) { // execuçãp de instrução
				ImagemProcesso processo = buscarProcessoPorID(dados_instrucao.get("processo_id"));

				EnderecoTraduzido endTraduzido = new EnderecoTraduzido(Long.parseLong(dados_instrucao.get("valor")), processo);

				acessarEntradaPagina((int) endTraduzido.getNumeroPagina(), processo, false);
				endTraduzido.recalcularEndFisico();

				//TODO pode ter um tempo de execução de intstrucao aqui

				/*
				boolean quadroTaNaMP = false; //talvez um erro ou um print
				for (int nQuadroLivre: memoriaPrincipal.getQuadrosOcupados()){
					if (numQuadro == nQuadroLivre) {
						quadroTaNaMP = true;
					}
				}
				*/

			} else if (Objects.equals(dados_instrucao.get("instrucao"), "I")) {
				ImagemProcesso processo = buscarProcessoPorID(dados_instrucao.get("processo_id"));

				EnderecoTraduzido endTraduzido = new EnderecoTraduzido(Long.parseLong(dados_instrucao.get("valor")), processo);
				processo.getPcb().setEstado(String.valueOf(Estados.BLOQUEADO));
				bloqueados.enqueue(processo.getIdProcesso());

				acessarEntradaPagina((int) endTraduzido.getNumeroPagina(), processo, false);
				endTraduzido.recalcularEndFisico();

				interrupcoes.enqueue(new Interrupcao(dados_instrucao.get("processo_id"), 3));


			} else if (Objects.equals(dados_instrucao.get("instrucao"), "R")) {
				ImagemProcesso processo = buscarProcessoPorID(dados_instrucao.get("processo_id"));

				EnderecoTraduzido endTraduzido = new EnderecoTraduzido(Long.parseLong(dados_instrucao.get("valor")), processo);
				processo.getPcb().setEstado(String.valueOf(Estados.BLOQUEADO));
				bloqueados.enqueue(processo.getIdProcesso());

				acessarEntradaPagina((int) endTraduzido.getNumeroPagina(), processo, false);
				endTraduzido.recalcularEndFisico();

				interrupcoes.enqueue(new Interrupcao(dados_instrucao.get("processo_id"), 0));


			} else if (Objects.equals(dados_instrucao.get("instrucao"), "W")) {
				ImagemProcesso processo = buscarProcessoPorID(dados_instrucao.get("processo_id"));

				EnderecoTraduzido endTraduzido = new EnderecoTraduzido(Long.parseLong(dados_instrucao.get("valor")), processo);

				processo.getPcb().setEstado(String.valueOf(Estados.BLOQUEADO));
				bloqueados.enqueue(processo.getIdProcesso());
				acessarEntradaPagina((int) endTraduzido.getNumeroPagina(), processo, true);
				endTraduzido.recalcularEndFisico();

				interrupcoes.enqueue(new Interrupcao(dados_instrucao.get("processo_id"), 1));

			} else if (Objects.equals(dados_instrucao.get("instrucao"), "T")) {
				ImagemProcesso processo = buscarProcessoPorID(dados_instrucao.get("processo_id"));
				processo.getPcb().setEstado(String.valueOf(Estados.PRONTO));
				finalizados.enqueue(processo.getIdProcesso());
				processo.setTabelaDePaginas(null);


			}
			input.setInstrucoesPendentes(input.getInstrucoesPendentes()-1);
			numInstAtual ++;
		}


		private void selecionarOutroParaExecucao() {
			executando = prontos.unqueue();
		}
		public void addFilaInterrupcoes(Interrupcao interrupcao) {
			this.interrupcoes.enqueue(interrupcao);
		}

	public EntradaTP acessarEntradaPagina(int numPagina, ImagemProcesso processo, boolean modificacao) throws InterruptedException {
		boolean consultaTLB = tlb.consulta(numPagina);
		EntradaTP resultado = processo.getTabelaDePaginas().getEntradaPagina(numPagina);
		if (modificacao && !tlb.getModificacao(numPagina))
			tlb.marcarModificacao(numPagina);
		if (modificacao && !resultado.getModificacao())
			resultado.setModificacao(true);


		Thread.sleep((long) ConfigData.tempoAcessoTLB*1000);//sleep tempoTLB
		if (consultaTLB){ //TLB HIT
			return resultado;
		}
		//TLB MISS
		Thread.sleep((long) ConfigData.tempoAcessoMP*1000);//sleep tempoMP


		boolean consultaTP = processo.getTabelaDePaginas().consulta(numPagina);
		if (consultaTP){ //TP HIT
			Thread.sleep((long) ConfigData.tempoAcessoMP*1000);//sleep tempoMP
			tlb.adicionarEntrada(resultado);
			return resultado;
		}
		//TP MISS
		Thread.sleep((long) ConfigData.tempoAcessoMS*1000);//sleep tempoMP

		MemoriaPrincipal mp = StaticObjects.getMemoriaPrincipal();
		if (!mp.getQuadrosLivres().isEmpty()) {
			Integer quadroLivre = mp.getQuadrosLivres().getFirst();
			mp.ocupar(quadroLivre);

			// Atualiza a entrada da tabela de páginas do processo
			resultado.setNumQuadro(quadroLivre);
			resultado.setPresenca(true);
			resultado.setUso(true);
			resultado.setModificacao(modificacao); // Define o bit de modificação conforme a instrução (R/W)
			// O tempo do último uso (para LRU) deve ser atualizado aqui também, se aplicável

		} else {
			//Lógica original de substituição
			if (Objects.equals(ConfigData.tipoSubstituicaoPaginas, "LRU"))
				substPagina.substituirComLRU(numPagina, processo.getIdProcesso());
			else if (Objects.equals(ConfigData.tipoSubstituicaoPaginas, "CLOCK")) {
				substPagina.substituirComClockTwoDigits(numPagina, processo.getIdProcesso());
			}
		}

		tlb.adicionarEntrada(resultado);
		return resultado;
	}
	public ImagemProcesso buscarProcessoPorID(String ID){
		return StaticObjects.getAllProcessos().stream()
				.filter(x -> x.getIdProcesso().equals(ID.substring(1))).findFirst().get();
	}


}


