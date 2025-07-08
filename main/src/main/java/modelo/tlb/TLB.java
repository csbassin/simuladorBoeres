package modelo.tlb;

import modelo.tabelaPaginas.EntradaTP;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class TLB {
    private final LinkedHashMap<Integer, EntradaTP> cache;
    private final int capacidade;
    private String processoIdAtual;

    public TLB(int capacidade) {
        this.capacidade = capacidade;
        this.cache = new LinkedHashMap<>(capacidade, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, EntradaTP> eldest) {
                return size() > TLB.this.capacidade;
            }
        };
    }

    public Optional<EntradaTP> consulta(int numPagina, String processoId) {
        if (!processoId.equals(this.processoIdAtual)) {
            return Optional.empty();
        }
        return Optional.ofNullable(cache.get(numPagina));
    }

    public void adicionarEntrada(EntradaTP entradaTP, String processoId) {
        if (!processoId.equals(this.processoIdAtual)) {
            trocaDeProcesso(processoId);
        }
        cache.put(entradaTP.getNumPagina(), entradaTP);
    }

    public void invalidarEntrada(int numPagina, String processoId) {
        if(processoId.equals(this.processoIdAtual)) {
            cache.remove(numPagina);
        }
    }

    public void trocaDeProcesso(String novoProcessoId) {
        this.cache.clear();
        this.processoIdAtual = novoProcessoId;
    }

    public String getProcessoIdAtual() {
        return processoIdAtual;
    }

    public Map<Integer, EntradaTP> getEntradas() {
        return cache;
    }
}