package modelo.tlb;

import modelo.tabelaPaginas.EntradaTP;

public class EntradaTLB {
	private byte valid;	// quando houver uma troca de processo, o bit de validade deve ser setado para 1
    private int numPagina; 
    private int tempoUltimoUso; // essa tem relação com a política de susbtituição da TLB, e não da tabela de páginas
    private EntradaTP entradaTP;
}
