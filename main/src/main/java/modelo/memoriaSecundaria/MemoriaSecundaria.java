package modelo.memoriaSecundaria;

import config.ConfigData;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MemoriaSecundaria extends Thread {
	// Uma fila bloqueante para gurdar as páginas que precisam ser gravadas no disco.
	private final BlockingQueue<String> paginasParaGravar = new LinkedBlockingQueue<>(); //pra lidar com as threads

	public void gravar(String idProcesso, int numPagina) {
		String paginaInfo = "Processo: " + idProcesso + ", Página: " + numPagina;
		try {
			paginasParaGravar.put(paginaInfo); // vai bloquear se a fila estiver cheia
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}//

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				String pagina = paginasParaGravar.take();
				System.out.println("Gravando na memória secundária -> " + pagina);
				Thread.sleep(ConfigData.tempoAcessoMS);
				System.out.println("Gravação concluída: " + pagina);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		System.out.println("Thread da Memória Secundária encerrada.");
	}
}