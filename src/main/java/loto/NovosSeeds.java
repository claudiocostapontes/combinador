package loto;

import org.apache.commons.math3.random.MersenneTwister;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class NovosSeeds {

    public static void main(String[] args) throws IOException {
        Integer[] numerosLoteria = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
        try (Scanner scanner = new Scanner(System.in)) {
            int tamanhoAposta;

            do {
                System.out.print("Digite o tamanho da aposta (15 a 20): ");
                tamanhoAposta = scanner.nextInt();
            } while (tamanhoAposta < 15 || tamanhoAposta > 20);

            Combinador combinador = new Combinador(tamanhoAposta, numerosLoteria);

            System.out.print("Digite a quantidade de apostas a serem geradas: ");
            int quantidadeApostas = scanner.nextInt();

            List<Integer[]> apostasGeradas = new ArrayList<>();
            MersenneTwister random = new MersenneTwister();

            for (int i = 0; i < quantidadeApostas; i++) {
                int seed = random.nextInt(quantidadeApostas);
                Integer[] aposta = combinador.pegarCombinacao(seed);
                System.out.println("Aposta " + (i + 1) + ": " + Arrays.toString(aposta));
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

    private static void salvarApostas(List<Integer[]> apostasGeradas, String nomeArquivo) throws IOException {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            for (Integer[] aposta : apostasGeradas) {
                writer.write(Arrays.toString(aposta) + System.lineSeparator());
            }
        }
    }

    private static Integer[] gerarSurpresinha(int tamanhoAposta, Integer[] numerosLoteria) {
        MersenneTwister random = new MersenneTwister();
        Set<Integer> aposta = new HashSet<>();

        while (aposta.size() < tamanhoAposta) {
            int numeroAleatorio = random.nextInt(25) + 1;
            aposta.add(numeroAleatorio);
        }

        return aposta.stream().sorted().toArray(Integer[]::new);
    }
}




