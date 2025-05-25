package modelo.processo;

public class ProcessControlBlock {
    public static final byte NOVO = 0;
    public static final byte PRONTO = 1;
    public static final byte EXECUTANDO = 2;
    public static final byte BLOQUEADO = 3;
    public static final byte BLOQUEADOSUSPENSO = 4;
    public static final byte PRONTOSUSPENSO = 5;
    public static final byte FINALIZADO = 6;

    private byte estado;
    //private pilha

    public byte getEstado(){
        return this.estado;
    }
    public String getEstadoAsString(){
        switch(estado) {
            case 0:
                return "Novo";
            case 1:
                return "Pronto";
            case 2:
                return "Executando";
            case 3:
                return "Bloqueado";
            case 4:
                return "Bloqueado-suspenso";
            case 5:
                return "Pronto-suspenso";
            case 6:
                return "Finalizado";
            default:
                return null;
        }

    }
    public void setEstado(String estado){
        switch(estado.toLowerCase().replace("-", "").replace(" ", "")){ // pode passar maiúsculo, minúsculo, com ou sem traço, com ou sem espaço
            case "novo":
                this.estado = 0;
                break;
            case "pronto":
                this.estado = 1;
                break;
            case "executando":
                this.estado = 2;
                break;
            case "bloqueado":
                this.estado = 3;
                break;
            case "bloqueadosuspenso":
                this.estado = 4;
                break;
            case "prontosuspenso":
                this.estado = 5;
                break;
            case "finalizado":
                this.estado = 6;
                break;
        }
    }
    public void setEstado(byte estado){
        this.estado = estado;
    }

}
