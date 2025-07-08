package other;

import java.util.ArrayList;
import java.util.Arrays;
import config.ConfigData;
import modelo.Execucao.CPU;
import modelo.processo.ImagemProcesso;


public class Conversoes {

    public static long convererterUnidade(long valor, String unidadeAtual, String uniadeDestino) {
        ArrayList<String> unidades = new ArrayList<>(Arrays.asList("b", "B", "KB", "MB", "GB", "TB"));
        int indiceAtual = unidades.indexOf(unidadeAtual);
        int indiceDestino = unidades.indexOf(uniadeDestino);

        if (indiceDestino == 0){
            return valor * 8 * (long) Math.pow(2, 10*(indiceAtual - 1));
        }
        return valor * (long) Math.pow(2, 10*(indiceAtual - indiceDestino));
    }

}
