package br.com.loto;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Combinador {

	private static final Map<Integer, Integer> FREQUENCIA_NUMEROS = new HashMap<>();
    private static final int TOTAL_NUMEROS_LOTERIA = 25;
    
    static {
        FREQUENCIA_NUMEROS.put(20, 1776);
        FREQUENCIA_NUMEROS.put(10, 1763);
        FREQUENCIA_NUMEROS.put(11, 1761);
        FREQUENCIA_NUMEROS.put(25, 1751);
        FREQUENCIA_NUMEROS.put(13, 1739);
        FREQUENCIA_NUMEROS.put(14, 1728);
        FREQUENCIA_NUMEROS.put(24, 1728);
        FREQUENCIA_NUMEROS.put(3, 1723);
        FREQUENCIA_NUMEROS.put(5, 1720);
        FREQUENCIA_NUMEROS.put(4, 1706);
        FREQUENCIA_NUMEROS.put(22, 1702);
        FREQUENCIA_NUMEROS.put(12, 1701);
        FREQUENCIA_NUMEROS.put(19, 1696);
        FREQUENCIA_NUMEROS.put(2, 1695);
        FREQUENCIA_NUMEROS.put(18, 1695);
        FREQUENCIA_NUMEROS.put(9, 1695);
        FREQUENCIA_NUMEROS.put(1, 1692);
        FREQUENCIA_NUMEROS.put(21, 1688);
        FREQUENCIA_NUMEROS.put(15, 1686);
        FREQUENCIA_NUMEROS.put(17, 1672);
        FREQUENCIA_NUMEROS.put(23, 1668);
        FREQUENCIA_NUMEROS.put(7, 1660);
        FREQUENCIA_NUMEROS.put(6, 1649);
        FREQUENCIA_NUMEROS.put(8, 1641);
        FREQUENCIA_NUMEROS.put(16, 1635);
    }

    public Combinador(int tamanhoAposta, Integer[] numerosLoteria) {
	}

	// Métodos de geração de apostas com base na frequência
    
    public static List<Integer> getNumerosMaisFrequentes(int quantidade) {
        return FREQUENCIA_NUMEROS.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(quantidade)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<Integer> getNumerosMenosFrequentes(int quantidade) {
        return FREQUENCIA_NUMEROS.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(quantidade)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static Integer[] gerarApostaMaisFrequentes(int tamanhoAposta) {
        return getNumerosMaisFrequentes(tamanhoAposta).toArray(Integer[]::new);
    }

    public static Integer[] gerarApostaMenosFrequentes(int tamanhoAposta) {
        return getNumerosMenosFrequentes(tamanhoAposta).toArray(Integer[]::new);
    }

    public static Integer[] gerarApostaIntervalo(int inicioFrequencia, int fimFrequencia, int tamanhoAposta) {
        if (inicioFrequencia < 1 || fimFrequencia > FREQUENCIA_NUMEROS.size() || inicioFrequencia > fimFrequencia) {
            throw new IllegalArgumentException("Intervalo de frequência inválido.");
        }

        List<Integer> numerosIntervalo = FREQUENCIA_NUMEROS.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .skip(inicioFrequencia - 1)
                .limit(fimFrequencia - inicioFrequencia + 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Random random = new Random();
        Set<Integer> aposta = new HashSet<>(numerosIntervalo);

        while (aposta.size() < tamanhoAposta) {
            aposta.add(random.nextInt(TOTAL_NUMEROS_LOTERIA) + 1);
        }

        return aposta.stream().sorted().toArray(Integer[]::new);
    }

    public static Integer[] gerarApostaAleatoriaComMaisFrequentes(int tamanhoAposta) {
        Set<Integer> aposta = new HashSet<>();
        Random random = new Random();

        while (aposta.size() < tamanhoAposta) {
            aposta.add(random.nextInt(TOTAL_NUMEROS_LOTERIA) + 1);
        }

        List<Integer> numerosMaisFrequentes = getNumerosMaisFrequentes(tamanhoAposta);
        List<Integer> apostaList = new ArrayList<>(aposta);

        if (apostaList.size() > 15) {
            for (int i = 0; i < numerosMaisFrequentes.size(); i++) {
                apostaList.set(i, numerosMaisFrequentes.get(i));
            }
        }

        return apostaList.stream().sorted().toArray(Integer[]::new);
    }

    public static Integer[] gerarSurpresinha(int tamanhoAposta) {
        Random random = new Random();
        Set<Integer> aposta = new HashSet<>();

        while (aposta.size() < tamanhoAposta) {
            aposta.add(random.nextInt(TOTAL_NUMEROS_LOTERIA) + 1);
        }

        return aposta.stream().sorted().toArray(Integer[]::new);
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int tamanhoAposta = obterTamanhoAposta(scanner);

            exibirApostasFrequencia(tamanhoAposta);

            exibirApostaIntervalo(scanner, tamanhoAposta);

            exibirApostaAleatoriaComMaisFrequentes(tamanhoAposta);

            gerarMultiplasApostas(scanner, tamanhoAposta);

            exibirApostaSurpresinha(scanner, tamanhoAposta);
        }
    }

    private static void gerarMultiplasApostas(Scanner scanner, int tamanhoAposta) {
	}
	private static void exibirApostaSurpresinha(Scanner scanner, int tamanhoAposta) {
	}

	private static int obterTamanhoAposta(Scanner scanner) {
        int tamanhoAposta;
        do {
            System.out.print("Digite o tamanho da aposta (15 a 20): ");
            tamanhoAposta = scanner.nextInt();
        } while (tamanhoAposta < 15 || tamanhoAposta > 20);
        return tamanhoAposta;
    }

    private static void exibirApostasFrequencia(int tamanhoAposta) {
        System.out.println("Aposta com números mais frequentes: " + Arrays.toString(gerarApostaMaisFrequentes(tamanhoAposta)));
        System.out.println("Aposta com números menos frequentes: " + Arrays.toString(gerarApostaMenosFrequentes(tamanhoAposta)));
    }

    private static void exibirApostaIntervalo(Scanner scanner, int tamanhoAposta) {
        int inicioFrequencia, fimFrequencia;
        do {
            System.out.print("Digite o início do intervalo de frequência (1 a 25): ");
            inicioFrequencia = scanner.nextInt();
            System.out.print("Digite o fim do intervalo de frequência (1 a 25): ");
            fimFrequencia = scanner.nextInt();
        } while (inicioFrequencia < 1 || fimFrequencia > TOTAL_NUMEROS_LOTERIA || inicioFrequencia > fimFrequencia);

        try {
            System.out.println("Aposta com números do intervalo de frequência: " + Arrays.toString(gerarApostaIntervalo(inicioFrequencia, fimFrequencia, tamanhoAposta)));
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    
}   


    private static void exibirApostaAleatoriaComMaisFrequentes(int tamanhoAposta) {
        Object inicioFrequencia = null;
		Object fimFrequencia = null;
		System.out.println("Aposta aleatória com números mais frequentes: " + Arrays.toString(gerarApostaAletoria(
        inicioFrequencia, fimFrequencia, tamanhoAposta)));
    }

	private static long[] gerarApostaAletoria(Object inicioFrequencia, Object fimFrequencia, int tamanhoAposta) {
		return null;
	}

	public Integer[] pegarCombinacao(int seed) {
		return null;
	}
	
}	
        
        		
        		