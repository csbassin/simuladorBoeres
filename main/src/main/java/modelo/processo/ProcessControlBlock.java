package modelo.processo;
import other.Estados;

public class ProcessControlBlock {

    private Estados estado;
    private int indiceInstrucao = 0;
    //private pilha

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
        String stringTratada = estado.toUpperCase().replace("-", "").replace(" ", ""); // pode passar maiúsculo, minúsculo, com ou sem traço, com ou sem espaço
        this.estado = Estados.valueOf(stringTratada); // se nao for um estado valido vai ter um IllegalArgumentException
    }
    public void setEstado(Estados estado){
        this.estado = estado;
    }

}
