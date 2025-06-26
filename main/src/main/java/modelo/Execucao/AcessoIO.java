package main.java.modelo.Execucao;

public class AcessoIO extends Thread{
	private int tempoAcessoMillis = 0;
	private CPU execucao;
	private String idProcesso;
	
	public AcessoIO(int tempo, CPU execucao, String idProcesso) {
		super();
		this.tempoAcessoMillis = tempo;
		this.execucao = execucao;
		this.idProcesso = idProcesso;
	}
	
	@Override
	public void run() {
		try {
			sleep(tempoAcessoMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			execucao.addFilaInterrupcoes(new Interrupcao(idProcesso, Interrupcao.DESBLOQUEAR));
		}
		
	}
}
