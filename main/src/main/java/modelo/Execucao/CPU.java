package modelo.Execucao;

import other.Input;
import other.StaticObjects;

import java.util.ArrayList;

import modelo.MemoriaPrincipal.MemoriaPrincipal;
import modelo.tabelaPaginas.PonteiroTabelaDePaginas;

public class CPU extends Thread{
	
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
		
		MemoriaPrincipal memoriaPrincipal = StaticObjects.getMemoriaPrincipal();
		ArrayList<PonteiroTabelaDePaginas> ponteirosTP = StaticObjects.getPonteiroTabelaDePaginas();
		ArrayList<String> entrada = null;
		try {
			entrada = new Input().getInstrucoes();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		for(int i = 0; i<entrada.size(); i++) { // para cada linha da entrada
			String instrucao = entrada.get(i);
			char operacao;
			String processo;
			
			//pegando o nome do processo
			int indexEspaco = instrucao.indexOf(' ');
			processo = instrucao.substring(0, i);
			instrucao = instrucao.substring(i+1); // +1 para remover o espaço
			//fim pegando nome do processo
			
			//pegando operação
			indexEspaco = instrucao.indexOf(' ');
			operacao = instrucao.substring(0, i).charAt(0);
			instrucao = instrucao.substring(i+1);
			//fim pegando operação
			
			if(operacao == 'C') { // criação do processo
				/*
				 	verifica o tamanho do processo
				 	verifica a quantidade de quadros necessária para a tabela de páginas
				 	Criaremos uma instância de processo e colocaremos seu estado como novo
				 	
				 	varre o arrayList de quadros livres.
				 	
				 	Se houver uma quantidade contígua suficiente de quadros livres para colocar a tabela de páginas, aloca a tabela de páginas nesses quadros e cria o ponteiro
				 	Se não houver, não pode criar o processo
				 	Passaremos a ignorar tudo desse processo no arquivo de entrada (porque o processo não foi criado)
				 	
				 	Se houver, alocamos esse quadro para a página. Seu estado passa para "Pronto"
				 	Se não houver, o processo permanece com o estado novo e é colocado em uma fila para alocação futura
				*/
			}
			
		}
	}
}
