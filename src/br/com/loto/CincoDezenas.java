package br.com.loto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

//seis dezenas ímpares e seis dezenas pares é o que esse código faz

public class CincoDezenas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao Gerador de Apostas da Lotofácil!");

        int quantidadeApostas;
        do {
            System.out.print("Digite a quantidade de apostas que deseja gerar (1 a 25): ");
            quantidadeApostas = scanner.nextInt();
        } while (quantidadeApostas < 1 || quantidadeApostas > 25);

        for (int i = 0; i < quantidadeApostas; i++) {
            int[] aposta = gerarApostaParesImpares();
            System.out.println("Aposta " + (i + 1) + ": " + Arrays.toString(aposta));
        }

        scanner.close();
    }

    private static int[] gerarApostaParesImpares() {
        List<Integer> numerosPares = new ArrayList<>();
        List<Integer> numerosImpares = new ArrayList<>();

        for (int j = 1; j <= 25; j++) {
            if (j % 2 == 0) {
                numerosPares.add(j);
            } else {
                numerosImpares.add(j);
            }
        }

        Collections.shuffle(numerosPares);
        Collections.shuffle(numerosImpares);

        List<Integer> apostaList = new ArrayList<>();
        apostaList.addAll(numerosPares.subList(0, 3));
        apostaList.addAll(numerosImpares.subList(0, 3));

        Collections.shuffle(apostaList);

        int[] aposta = new int[6];
        for (int j = 0; j < 6; j++) {
            aposta[j] = apostaList.get(j);
        }

        Arrays.sort(aposta);
        return aposta;
    }
}