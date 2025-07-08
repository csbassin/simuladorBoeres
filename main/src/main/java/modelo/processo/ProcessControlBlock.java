package modelo.processo;
import other.Estados;

public class ProcessControlBlock {

    private Estados estado;
    private int indiceInstrucao = 0;

    public Estados getEstado(){
        return this.estado;
    }
    
    public int getIndiceInstrucao() {
		return indiceInstrucao;
	}

	public void setIndiceInstrucao(int indiceInstrucao) {
		this.indiceInstrucao = indiceInstrucao;
	}

	public String getEstadoAsString(){
        return estado.asString();
    }
    public void setEstado(String estado){
        String stringTratada = estado.toUpperCase().replace("-", "").replace(" ", "");
        this.estado = Estados.valueOf(stringTratada);
    }
    public void setEstado(Estados estado){
        this.estado = estado;
    }

}
