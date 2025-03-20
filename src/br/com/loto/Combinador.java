package br.com.loto;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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

    public static void main(String[] args) {
        Integer[] numerosLoteria = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
        Scanner scanner = new Scanner(System.in);
        int tamanhoAposta;

        do {
            System.out.print("Digite o tamanho da aposta (15 a 18): ");
            tamanhoAposta = scanner.nextInt();
        } while (tamanhoAposta < 15 || tamanhoAposta > 18);

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
            try {
                salvarApostas(apostasGeradas, nomeArquivo);
                System.out.println("Apostas salvas com sucesso em " + nomeArquivo);
            } catch (IOException e) {
                System.err.println("Erro ao salvar apostas: " + e.getMessage());
            }
        }

        System.out.print("Deseja gerar uma aposta 'Surpresinha'? (S/N): ");
        resposta = scanner.next();

        if (resposta.equalsIgnoreCase("S")) {
            Integer[] apostaSurpresinha = gerarSurpresinha(tamanhoAposta, numerosLoteria);
            System.out.println("Aposta Surpresinha: " + Arrays.toString(apostaSurpresinha));
        }

        System.out.print("Deseja verificar a premiação de uma aposta? (S/N): ");
        resposta = scanner.next();

        if (resposta.equalsIgnoreCase("S")) {
            Integer[] numerosSorteados = new Integer[15];
            System.out.println("Digite os 15 números sorteados:");
            for (int i = 0; i < 15; i++) {
                numerosSorteados[i] = scanner.nextInt();
            }

            System.out.println("Digite os números da aposta para verificar a premiação:");
            Integer[] apostaVerificacao = new Integer[tamanhoAposta];
            for(int i = 0; i < tamanhoAposta; i++){
                apostaVerificacao[i] = scanner.nextInt();
            }

            int acertos = verificarPremiacao(apostaVerificacao, numerosSorteados);
            System.out.println("Número de acertos: " + acertos);
            if (acertos >= 11) {
                System.out.println("Aposta premiada!");
            } else {
                System.out.println("Aposta não premiada.");
            }

        }
        scanner.close();
    }

    public static int verificarPremiacao(Integer[] aposta, Integer[] numerosSorteados) {
        int acertos = 0;
        for (int numeroAposta : aposta) {
            for (int numeroSorteado : numerosSorteados) {
                if (numeroAposta == numeroSorteado) {
                    acertos++;
                    break;
                }
            }
        }
        return acertos;
    }

    public static void salvarApostas(List<Integer[]> apostas, String nomeArquivo) throws IOException {
        FileWriter writer = new FileWriter(nomeArquivo);
        for (Integer[] aposta : apostas) {
            writer.write(Arrays.toString(aposta) + System.lineSeparator());
        }
        writer.close();
    }

    public static Integer[] gerarSurpresinha(int tamanhoAposta, Integer[] numerosLoteria) {
        Random random = new Random();
        Set<Integer> aposta = new HashSet<>();
        while (aposta.size() < tamanhoAposta) {
            int indice = random.nextInt(numerosLoteria.length);
            aposta.add(numerosLoteria[indice]);
        }
        return aposta.toArray(new Integer[0]);
    }
}