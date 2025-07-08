package modelo.Execucao;

public class AcessoIO extends Thread {
	private final int tempoAcessoMillis;
	private final CPU execucao;
	private final String idProcesso;

	public AcessoIO(int tempo, CPU execucao, String idProcesso) {
		super();
		this.tempoAcessoMillis = tempo;
		this.execucao = execucao;
		this.idProcesso = idProcesso;
	}

	@Override
	public void run() {
		try {
			System.out.println("Processo " + idProcesso + " iniciou uma operação de I/O de " + tempoAcessoMillis + "ms.");
			sleep(tempoAcessoMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			System.out.println("Processo " + idProcesso + " concluiu a operação de I/O.");
			execucao.addFilaInterrupcoes(new Interrupcao(idProcesso, Interrupcao.DESBLOQUEAR));
		}
	}
}