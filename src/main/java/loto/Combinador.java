package loto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Ferramenta para gerar combinações de apostas para um jogo de loteria de 25 números.
 * Utiliza dados de frequência histórica para criar apostas com diferentes estratégias.
 */
public class Combinador {

    private static final Map<Integer, Integer> FREQUENCIA_NUMEROS = new HashMap<>();
    private static final int TOTAL_NUMEROS_LOTERIA = 25;

    // Bloco estático para inicializar o mapa de frequências.
    // Estes dados representam a frequência histórica de cada número.
    static {
        FREQUENCIA_NUMEROS.put(20, 1776); FREQUENCIA_NUMEROS.put(10, 1763);
        FREQUENCIA_NUMEROS.put(11, 1761); FREQUENCIA_NUMEROS.put(25, 1751);
        FREQUENCIA_NUMEROS.put(13, 1739); FREQUENCIA_NUMEROS.put(14, 1728);
        FREQUENCIA_NUMEROS.put(24, 1728); FREQUENCIA_NUMEROS.put(3, 1723);
        FREQUENCIA_NUMEROS.put(5, 1720); FREQUENCIA_NUMEROS.put(4, 1706);
        FREQUENCIA_NUMEROS.put(22, 1702); FREQUENCIA_NUMEROS.put(12, 1701);
        FREQUENCIA_NUMEROS.put(19, 1696); FREQUENCIA_NUMEROS.put(2, 1695);
        FREQUENCIA_NUMEROS.put(18, 1695); FREQUENCIA_NUMEROS.put(9, 1695);
        FREQUENCIA_NUMEROS.put(1, 1692); FREQUENCIA_NUMEROS.put(21, 1688);
        FREQUENCIA_NUMEROS.put(15, 1686); FREQUENCIA_NUMEROS.put(17, 1672);
        FREQUENCIA_NUMEROS.put(23, 1668); FREQUENCIA_NUMEROS.put(7, 1660);
        FREQUENCIA_NUMEROS.put(6, 1649); FREQUENCIA_NUMEROS.put(8, 1641);
        FREQUENCIA_NUMEROS.put(16, 1635);
    }

    public Combinador(int tamanhoAposta, Integer[] numerosLoteria) {
    }

    /**
     * Retorna uma lista dos números mais sorteados, ordenados por frequência.
     * @param quantidade O número de resultados a serem retornados.
     * @return Uma lista com os números mais frequentes.
     */
    public static List<Integer> getNumerosMaisFrequentes(int quantidade) {
        return FREQUENCIA_NUMEROS.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(quantidade)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Retorna uma lista dos números menos sorteados, ordenados por frequência.
     * @param quantidade O número de resultados a serem retornados.
     * @return Uma lista com os números menos frequentes.
     */
    public static List<Integer> getNumerosMenosFrequentes(int quantidade) {
        return FREQUENCIA_NUMEROS.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(quantidade)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Gera uma aposta contendo apenas os números mais frequentes.
     * @param tamanhoAposta O tamanho da aposta.
     * @return Um array com a aposta gerada.
     */
    public static Integer[] gerarApostaMaisFrequentes(int tamanhoAposta) {
        return getNumerosMaisFrequentes(tamanhoAposta).toArray(new Integer[0]);
    }

    /**
     * Gera uma aposta contendo apenas os números menos frequentes.
     * @param tamanhoAposta O tamanho da aposta.
     * @return Um array com a aposta gerada.
     */
    public static Integer[] gerarApostaMenosFrequentes(int tamanhoAposta) {
        return getNumerosMenosFrequentes(tamanhoAposta).toArray(new Integer[0]);
    }

    /**
     * Gera uma aposta selecionando aleatoriamente números de um intervalo de ranking de frequência.
     * @param inicioFrequencia Posição inicial no ranking (ex: 1 para o mais frequente).
     * @param fimFrequencia Posição final no ranking.
     * @param tamanhoAposta O tamanho da aposta.
     * @return Um array com a aposta gerada.
     */
    public static Integer[] gerarApostaIntervalo(int inicioFrequencia, int fimFrequencia, int tamanhoAposta) {
        if (inicioFrequencia < 1 || fimFrequencia > FREQUENCIA_NUMEROS.size() || inicioFrequencia > fimFrequencia) {
            throw new IllegalArgumentException("Intervalo de frequência inválido.");
        }

        List<Integer> numerosNoIntervalo = FREQUENCIA_NUMEROS.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .skip(inicioFrequencia - 1)
                .limit(fimFrequencia - inicioFrequencia + 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (numerosNoIntervalo.size() < tamanhoAposta) {
            throw new IllegalArgumentException("O intervalo de frequência selecionado não contém números suficientes para uma aposta de tamanho " + tamanhoAposta);
        }

        Collections.shuffle(numerosNoIntervalo);

        return numerosNoIntervalo.stream().limit(tamanhoAposta).sorted().toArray(Integer[]::new);
    }

    /**
     * Gera uma aposta mista, combinando os números mais frequentes com outros números aleatórios.
     * A estratégia é usar 2/3 dos números mais frequentes e 1/3 de outros números.
     * @param tamanhoAposta O tamanho da aposta.
     * @return Um array com a aposta gerada.
     */
    public static Integer[] gerarApostaMista(int tamanhoAposta) {
        Set<Integer> aposta = new HashSet<>();

        int qtdMaisFrequentes = (tamanhoAposta * 2) / 3;
        int qtdOutros = tamanhoAposta - qtdMaisFrequentes;

        List<Integer> todosNumerosOrdenados = getNumerosMaisFrequentes(TOTAL_NUMEROS_LOTERIA);

        // Adiciona os mais frequentes
        aposta.addAll(todosNumerosOrdenados.subList(0, qtdMaisFrequentes));

        // Pega os números restantes para a seleção aleatória
        List<Integer> numerosRestantes = new ArrayList<>(todosNumerosOrdenados.subList(qtdMaisFrequentes, todosNumerosOrdenados.size()));
        Collections.shuffle(numerosRestantes);

        // Completa a aposta com números aleatórios do restante
        aposta.addAll(numerosRestantes.subList(0, qtdOutros));

        return aposta.stream().sorted().toArray(Integer[]::new);
    }

    /**
     * Gera uma aposta totalmente aleatória (Surpresinha).
     * @param tamanhoAposta O tamanho da aposta.
     * @return Um array com a aposta gerada.
     */
    public static Integer[] gerarSurpresinha(int tamanhoAposta) {
        Random random = new Random();
        Set<Integer> aposta = new HashSet<>();

        while (aposta.size() < tamanhoAposta) {
            aposta.add(random.nextInt(TOTAL_NUMEROS_LOTERIA) + 1);
        }

        return aposta.stream().sorted().toArray(Integer[]::new);
    }

    /**
     * Gera uma combinação previsível baseada em uma "seed". A mesma seed sempre gera a mesma combinação.
     * @param seed Um número inteiro para iniciar o gerador aleatório.
     * @param tamanhoAposta O tamanho da aposta.
     * @return Um array com a combinação gerada.
     */
    public Integer[] pegarCombinacao(int seed, int tamanhoAposta) {
        Random random = new Random(seed);
        Set<Integer> aposta = new HashSet<>();

        while (aposta.size() < tamanhoAposta) {
            aposta.add(random.nextInt(TOTAL_NUMEROS_LOTERIA) + 1);
        }
        return aposta.stream().sorted().toArray(Integer[]::new);
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("--- Gerador de Apostas para Loteria ---");
            int tamanhoAposta = obterTamanhoAposta(scanner);

            System.out.println("\n--- Apostas Baseadas em Frequência ---");
            exibirApostasFrequencia(tamanhoAposta);

            System.out.println("\n--- Aposta com Intervalo de Frequência ---");
            exibirApostaIntervalo(scanner, tamanhoAposta);

            System.out.println("\n--- Aposta Mista (Frequentes + Aleatórios) ---");
            exibirApostaMista(tamanhoAposta);

            System.out.println("\n--- Múltiplas Apostas Aleatórias (Surpresinha) ---");
            gerarMultiplasApostas(scanner, tamanhoAposta);

            System.out.println("\n--- Fim do Programa ---");
        }
    }

    private static int obterTamanhoAposta(Scanner scanner) {
        int tamanhoAposta;
        do {
            System.out.print("Digite o tamanho da aposta (15 a 20): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                System.out.print("Digite o tamanho da aposta (15 a 20): ");
                scanner.next();
            }
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
            System.out.print("Digite o fim do intervalo de frequência (" + inicioFrequencia + " a 25): ");
            fimFrequencia = scanner.nextInt();
        } while (inicioFrequencia < 1 || fimFrequencia > TOTAL_NUMEROS_LOTERIA || inicioFrequencia > fimFrequencia);

        try {
            System.out.println("Aposta com números do intervalo de frequência: " + Arrays.toString(gerarApostaIntervalo(inicioFrequencia, fimFrequencia, tamanhoAposta)));
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void exibirApostaMista(int tamanhoAposta) {
        System.out.println("Aposta mista (2/3 mais frequentes, 1/3 aleatórios): " + Arrays.toString(gerarApostaMista(tamanhoAposta)));
    }

    private static void gerarMultiplasApostas(Scanner scanner, int tamanhoAposta) {
        System.out.print("Deseja gerar múltiplas apostas aleatórias (surpresinha)? (S/N): ");
        String resposta = scanner.next();

        if (resposta.equalsIgnoreCase("S")) {
            System.out.print("Quantas apostas deseja gerar? ");
            int quantidade = scanner.nextInt();
            for (int i = 1; i <= quantidade; i++) {
                System.out.printf("Aposta %d: %s\n", i, Arrays.toString(gerarSurpresinha(tamanhoAposta)));
            }
        }
    }

    public Integer[] pegarCombinacao(int seed) {
        return null;
    }
}