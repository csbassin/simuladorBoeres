package modelo.Execucao;

import other.*;

import java.util.HashMap;
import java.util.Objects;

import config.configData;
import modelo.tabelaPaginas.TabelaDePaginas;
import modelo.MemoriaPrincipal.MemoriaPrincipal;
import modelo.processo.ImagemProcesso;

public class CPU extends Thread{
	
	private MemoriaPrincipal memoriaPrincipal = null;
	private Input input = null;
	private int numInstAtual = 0; // usada apenas para parar a simulação quando acabarem as instruções. Devemos incrementar individualmente o ponteiro em cada processo
	Fila<String> prontos = new Fila<>();
	Fila<String> bloqueados = new Fila<>();
	Fila<String> novos = new Fila<>();
	Fila<String> bloqueadosSuspensos = new Fila<>();
	Fila<String> prontosSuspensos = new Fila<>();
	Fila<String> finalizados = new Fila<>();
	private Fila<Interrupcao> interrupcoes = new Fila<>();
	
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
				executaInstrucao(instrucao);
			}else {
				String processoId = instrucao.substring(0, instrucao.indexOf(' '));
				if(bloqueadosSuspensos.contains(processoId) || bloqueados.contains(processoId) || prontosSuspensos.contains(processoId)) {
					if(!(prontos.isEmpty())) {
						selecionarOutroParaExecucao();
					}
				}else {
					executaInstrucao(instrucao);
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
		imagemProcesso.getPcb().setEstado(Estados.NOVO);
		novos.enqueue(imagemProcesso.getIdProcesso());

		int quadrosLivres = memoriaPrincipal.getQuadrosLivres().size();
		int quantidadeQuadrosProcesso = (int) Math.ceil(tamanhoProcessoBytes/configData.quadroSize);

		if(quadrosLivres == 0) { // não tem espaço para alocar ao menos uma página do processo (novo->pronto suspenso direto? ou somenete novo)
			return null;
		}else { // tem espaço para alocar pelo menos uma página do processo
			TabelaDePaginas tp = criaTabelaDePaginas(processId, quantidadeQuadrosProcesso);
			Integer quadroPagina = memoriaPrincipal.getQuadrosLivres().getFirst();
			memoriaPrincipal.ocupar(quadroPagina);
			imagemProcesso.getPcb().setEstado(Estados.PRONTO);
			prontos.enqueue(imagemProcesso.getIdProcesso());
			imagemProcesso.setTabelaDePaginas(tp);

		}
		return imagemProcesso;
	}

	private void executaInstrucao(String instrucao) {
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
			input.setInstrucoesPendentes(input.getInstrucoesPendentes()-1);
			numInstAtual ++;
		}


		private void selecionarOutroParaExecucao() {
			executando = prontos.unqueue();
		}
		public void addFilaInterrupcoes(Interrupcao interrupcao) {
			this.interrupcoes.enqueue(interrupcao);
		}

	}


