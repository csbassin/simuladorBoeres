package main.java.modelo.memoriaSecundaria;

import java.util.ArrayList;

public class MemoriaSecundaria extends Thread{
	private ArrayList<String> processoPagina = new ArrayList<>();

	public MemoriaSecundaria() {

	}

	public void gravar(String idProcesso, int numPagina) {
		run();
		processoPagina.add(idProcesso+" "+numPagina);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000); // Simula o tempo de gravação
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!processoPagina.isEmpty()) {
				String pagina = processoPagina.remove(0);
				System.out.println("Gravando na memória secundária: " + pagina);
			}
		}
	}
}



/*
A minha net ta uma merda, fiz o L legal. Nascimento, Renan. 15:36


 */