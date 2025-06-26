package main.java.other;

import java.util.ArrayList;
import java.util.Arrays;
import main.java.config.ConfigData;
import main.java.modelo.Execucao.CPU;
import main.java.modelo.processo.ImagemProcesso;


public class Conversoes {

    public static long convererterUnidade(long valor, String unidadeAtual, String uniadeDestino) {
       // Por enquanto só faz converões de maior para um menor
        ArrayList<String> unidades = new ArrayList<>(Arrays.asList("b", "B", "KB", "MB", "GB", "TB"));
        int indiceAtual = unidades.indexOf(unidadeAtual);
        int indiceDestino = unidades.indexOf(uniadeDestino);

        if (indiceDestino == 0){
            return valor * 8 * (long) Math.pow(2, 10*(indiceAtual - 1)); //indiceAtual - 1 dist do atual ate o bit
        }
        return valor * (long) Math.pow(2, 10*(indiceAtual - indiceDestino));
    }


    //Endereço logico ->endereco fisico
    public static long[] converterEnderecoLogicoFisico(long enderecoLogico, CPU cpu, ImagemProcesso processo) {

        int qtdBitsOffset = (int) Math.ceil(Math.log(
                Conversoes.convererterUnidade(ConfigData.quadroSize, "B", "b")
        )/Math.log(2));
        long numPag = enderecoLogico/qtdBitsOffset;
        long offset = enderecoLogico % qtdBitsOffset;
        int numQuadro = cpu.acessarEntradaPagina((int)numPag, processo).getNumQuadro();
        long endFisico = numQuadro * (long) ConfigData.quadroSize + offset;

        return new long[] {endFisico, numQuadro};


    }
}
