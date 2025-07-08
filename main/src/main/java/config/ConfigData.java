package config;

public class ConfigData {
    public static int quantidadeQuadros = 4;  //número de quadros na memória principal
    public static String pathInput = "main/src/teste.txt"; // arquivo de entrada com as instruções de simulação
    public static int quadroSize = 1024; // Tamanho de cada quadro de memória (e, consequentemente, de cada página) em bytes
    public static int qntdPagTlB = 2; // Número de entradas na TLB
    public static String tipoSubstituicaoPaginas = "LRU"; // ("LRU" ou "CLOCK")

    // Tempos de acesso para os diferentes tipos de memória, em milissegundos.
    //só sao aplciados se não for usado o modo passo a passo
    public static int tempoAcessoMP = 20;
    public static int tempoAcessoMS = 100;
    public static int tempoAcessoTLB = 5;
    public static int cicloCPU = 500;
}