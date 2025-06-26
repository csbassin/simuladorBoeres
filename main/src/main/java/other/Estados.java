package main.java.other;

public enum Estados {
    NOVO("Novo"),
    PRONTO("Pronto"),
    EXECUTANDO("Executando"),
    BLOQUEADO("Bloqueado"),
    BLOQUEADOSUSPENSO("Bloqueado-suspenso"),
    PRONTOSUSPENSO("Pronto-suspenso"),
    FINALIZADO("Finalizado");

    private final String string;

    Estados(String string) {
        this.string = string;
    }
    public String asString(){
        return string;
    }
}
