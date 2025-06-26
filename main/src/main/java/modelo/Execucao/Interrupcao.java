package main.java.modelo.Execucao;

public class Interrupcao {
	public static int DESBLOQUEAR = 0;
	private String idProcesso;
	private int tipoInterrupcao;
	
	public Interrupcao(String idProcesso, int tipoInterrupcao) {
		this.idProcesso = idProcesso;
		this.tipoInterrupcao = tipoInterrupcao;
	}
	
	public String getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}
	public int getTipoInterrupcao() {
		return tipoInterrupcao;
	}
	public void setTipoInterrupcao(int tipoInterrupcao) {
		this.tipoInterrupcao = tipoInterrupcao;
	}
	
	
}
