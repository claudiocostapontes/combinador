package br.com.loto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Combinador<T> {
    private final int cnt;
    private final T[] items;
    private final List<int[]> somatorios;

    public Combinador(int cnt, T[] items) {
        this.cnt = cnt;
        this.items = items;
        this.somatorios = new ArrayList<>();

        int[] line0 = new int[cnt + 1];
        line0[0] = 1;
        this.somatorios.add(line0);

        for (int itLine = 1; itLine <= cnt; itLine++) {
            int[] prevLine = this.somatorios.get(itLine - 1);
            int[] newLine = new int[line0.length];
            for (int itCol = 0; itCol < newLine.length; itCol++) {
                newLine[itCol] = (itCol > 0 ? newLine[itCol - 1] : 0) + prevLine[itCol];
            }
            this.somatorios.add(newLine);
        }
    }

    public T[] pegarCombinacao(int seed) {
        List<Integer> indices = gerarIndices(this.somatorios, this.items.length - this.cnt, this.cnt, seed);
        return indices.stream()
                .map(i -> this.items[i])
                .toArray(i -> Arrays.copyOf(this.items, i));
    }

    private static List<Integer> gerarIndices(List<int[]> lines, int fs, int ts, int num) {
        List<Integer> indices = new ArrayList<>();
        if (ts <= 0) {
            return indices;
        }

        int[] line = lines.get(ts);
        int min = 0;

        for (int itFs = 0; itFs <= fs; itFs++) {
            int max = min + line[itFs];
            if (num < max) {
                int num2 = num - min;
                indices.add(fs - itFs);
                List<Integer> subIndices = gerarIndices(lines, itFs, ts - 1, num2);
                for (int idx : subIndices) {
                    indices.add(fs - itFs + idx + 1);
                }
                return indices;
            }
            min = max;
        }

        throw new IllegalArgumentException("O parâmetro deve ser menor que " + min);
    }

    private static Map<Integer, Integer> frequenciaNumeros = new HashMap<>();

    static {
        frequenciaNumeros.put(20, 1776);
        frequenciaNumeros.put(10, 1763);
        frequenciaNumeros.put(11, 1761);
        frequenciaNumeros.put(25, 1751);
        frequenciaNumeros.put(13, 1739);
        frequenciaNumeros.put(14, 1728);
        frequenciaNumeros.put(24, 1728);
        frequenciaNumeros.put(3, 1723);
        frequenciaNumeros.put(5, 1720);
        frequenciaNumeros.put(4, 1706);
        frequenciaNumeros.put(22, 1702);
        frequenciaNumeros.put(12, 1701);
        frequenciaNumeros.put(19, 1696);
        frequenciaNumeros.put(2, 1695);
        frequenciaNumeros.put(18, 1695);
        frequenciaNumeros.put(9, 1695);
        frequenciaNumeros.put(1, 1692);
        frequenciaNumeros.put(21, 1688);
        frequenciaNumeros.put(15, 1686);
        frequenciaNumeros.put(17, 1672);
        frequenciaNumeros.put(23, 1668);
        frequenciaNumeros.put(7, 1660);
        frequenciaNumeros.put(6, 1649);
        frequenciaNumeros.put(8, 1641);
        frequenciaNumeros.put(16, 1635);
    }

    public static List<Integer> getNumerosMaisFrequentes(int quantidade) {
        return frequenciaNumeros.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(quantidade)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<Integer> getNumerosMenosFrequentes(int quantidade) {
        return frequenciaNumeros.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(quantidade)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static Integer[] gerarApostaComEstatisticas(int tamanhoAposta, int quantidadeMaisFrequentes, int quantidadeMenosFrequentes) {
        List<Integer> numerosMais = getNumerosMaisFrequentes(quantidadeMaisFrequentes);
        List<Integer> numerosMenos = getNumerosMenosFrequentes(quantidadeMenosFrequentes);
        Set<Integer> aposta = new HashSet<>();

        aposta.addAll(numerosMais);
        aposta.addAll(numerosMenos);

        Random random = new Random();
        while (aposta.size() < tamanhoAposta) {
            int numeroAleatorio = random.nextInt(25) + 1;
            aposta.add(numeroAleatorio);
        }

        return aposta.stream().sorted().toArray(Integer[]::new);
    }

    public static void main(String[] args) {
        Integer[] numerosLoteria = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
        try (Scanner scanner = new Scanner(System.in)) {
			int tamanhoAposta;

			do {
			    System.out.print("Digite o tamanho da aposta (15 a 20): ");
			    tamanhoAposta = scanner.nextInt();
			} while (tamanhoAposta < 15 || tamanhoAposta > 20);

			Combinador<Integer> combinador = new Combinador<>(tamanhoAposta, numerosLoteria);

			System.out.print("Digite a quantidade de apostas a serem geradas: ");
			int quantidadeApostas = scanner.nextInt();

			List<Integer[]> apostasGeradas = new ArrayList<>();
			for (int seed = 0; seed < quantidadeApostas; seed++) {
			    Integer[] aposta = combinador.pegarCombinacao(seed);
			    System.out.println("Aposta " + (seed + 1) + ": " + Arrays.toString(aposta));
			    apostasGeradas.add(aposta);
			}

			System.out.print("Deseja salvar as apostas em um arquivo? (S/N): ");
			String resposta = scanner.next();

			if (resposta.equalsIgnoreCase("S")) {
			    System.out.print("Digite o nome do arquivo: ");
			    String nomeArquivo = scanner.next();
			    salvarApostas(apostasGeradas, nomeArquivo);
				System.out.println("Apostas salvas com sucesso em " + nomeArquivo);
			}

			System.out.print("Deseja gerar uma aposta 'Surpresinha'? (S/N): ");
			resposta = scanner.next();

			if (resposta.equalsIgnoreCase("S")) {
			    Integer[] apostaSurpresinha = gerarSurpresinha(tamanhoAposta, numerosLoteria);
			    System.out.println("Aposta Surpresinha: " + Arrays.toString(apostaSurpresinha));
			}
		}
    }

    private static void salvarApostas(List<Integer[]> apostasGeradas, String nomeArquivo) {
		
	}

	private static Integer[] gerarSurpresinha(int tamanhoAposta, Integer[] numerosLoteria) {
        Random random = new Random();
        Set<Integer> aposta = new HashSet<>();

        while (aposta.size() < tamanhoAposta) {
            int numeroAleatorio = random.nextInt(25) + 1;
            aposta.add(numeroAleatorio);
        }

        return aposta.stream().sorted().toArray(Integer[]::new);
    }
}        