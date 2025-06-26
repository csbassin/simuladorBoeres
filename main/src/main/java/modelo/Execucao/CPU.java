package main.java.modelo.Execucao;

import main.java.modelo.tabelaPaginas.EntradaTP;
import main.java.modelo.tlb.TLB;
import main.java.other.*;
import main.java.visao.ViewTabelaPaginas;

import java.util.*;
import java.util.stream.Collectors;

import main.java.other.Estados;
import main.java.config.ConfigData;
import main.java.modelo.tabelaPaginas.TabelaDePaginas;
import main.java.modelo.memoriaPrincipal.MemoriaPrincipal;
import main.java.modelo.processo.ImagemProcesso;

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
	
	String executando;
	int processosCriados = 0;
	int processosFinalizados = 0;
	
	//janelas que teremos que interagir
	ViewTabelaPaginas viewTabelaPaginas;
	//
	
	public CPU(ViewTabelaPaginas v) {
		this.viewTabelaPaginas = v;
		
	}
	
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
				executaComando(instrucao);
			}else {
				String processoId = instrucao.substring(0, instrucao.indexOf(' '));
				if(bloqueadosSuspensos.contains(processoId) || bloqueados.contains(processoId) || prontosSuspensos.contains(processoId)) {
					if(!(prontos.isEmpty())) {
						selecionarOutroParaExecucao();
					}
				}else {
					executaComando(instrucao);
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
		imagemProcesso.getPcb().setEstado(String.valueOf(Estados.NOVO));
		novos.enqueue(imagemProcesso.getIdProcesso());
		//sleep algum tempo TODO

		int quadrosLivres = memoriaPrincipal.getQuadrosLivres().size();
		int quantidadeQuadrosProcesso = (int) Math.ceil(tamanhoProcessoBytes/ ConfigData.quadroSize);

		if(quadrosLivres == 0) { // não tem espaço para alocar ao menos uma página do processo (novo->pronto suspenso direto? ou somenete novo)
			imagemProcesso.getPcb().setEstado(String.valueOf(Estados.PRONTOSUSPENSO));
			prontosSuspensos.enqueue(imagemProcesso.getIdProcesso());
			return imagemProcesso;
		}else { // tem espaço para alocar pelo menos uma página do processo
			TabelaDePaginas tp = criaTabelaDePaginas(processId, quantidadeQuadrosProcesso);
			Integer quadroPagina = memoriaPrincipal.getQuadrosLivres().getFirst();
			memoriaPrincipal.ocupar(quadroPagina);
			imagemProcesso.getPcb().setEstado(String.valueOf(Estados.PRONTO));
			prontos.enqueue(imagemProcesso.getIdProcesso());
			imagemProcesso.setTabelaDePaginas(tp);

		}
		viewTabelaPaginas.addToComboProcessos(processId);
		StaticObjects.getAllProcessos().add(imagemProcesso);
		return imagemProcesso;
	}

	private void executaComando(String instrucao) {
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
				ImagemProcesso processo = StaticObjects.getAllProcessos().stream()
						.filter(x -> x.getIdProcesso().equals(dados_instrucao.get("processo_id"))).findFirst().get();

				long[] resultado = Conversoes.converterEnderecoLogicoFisico(Long.parseLong(dados_instrucao.get("valor")), this, processo);
				long endFisico = resultado[0];
				int numQuadro = (int) resultado[1];

				boolean quadroTaNaMP = false; //talvez um erro ou um print
				for (int nQuadroLivre: memoriaPrincipal.getQuadrosOcupados()){
					if (numQuadro == nQuadroLivre) {
						quadroTaNaMP = true;
					}
				}


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

	public EntradaTP acessarEntradaPagina(int numPagina, ImagemProcesso processo){
		boolean consultaTLB = tlb.consulta(numPagina);
		EntradaTP resultado = processo.getTabelaDePaginas().getEntradaPagina(numPagina);
		if (consultaTLB){ //TLB HIT
			return resultado;
		}
		//TLB MISS, por um sleep aqui TODO

		boolean consultaTP = processo.getTabelaDePaginas().consulta(numPagina);
		if (consultaTP){ //TP HIT
			return resultado;
		}
		//TP MISS, por um sleep aqui TODO

		//Algoritmo de substituicao na tp TODO
		tlb.adicionarEntrada(resultado);
		return resultado;
	}


}


