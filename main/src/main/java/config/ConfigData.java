package config;

public class ConfigData {
    public static int tpSize = 300; //linhas da tabela de pagina
    public static int quantidadeQuadros = 200; // quantidade de quadros da MP. É também o tamanho máximo do processo, uma vez que o escopo de substituição é global.
    public static String pathInput = "main/src/teste.txt";
    public static int quadroSize = 4192; //colocar sempre o valor em bytes, por favor
    public static int qntdPagTlB = 30; //quantidade de paginas na TLB
    public static String tipoSubstituicaoPaginas = "LRU"; //LRU ou CLOCK


    public static double tempoAcessoMP = 2.5;
    public static int tempoAcessoMS = 5;
    public static int tempoAcessoTLB = 1;
}
