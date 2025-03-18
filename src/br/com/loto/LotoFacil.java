package br.com.loto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LotoFacil {

    public static class Combinador<T> {
        private final int cnt;
        private final T[] items;
        private final List<int[]> somatorios;

        public Combinador(int cnt, T[] items) {
            this.cnt = cnt;
            this.items = items;
            int[] line0 = IntStream.concat(IntStream.of(1), IntStream.generate(() -> 0).limit(cnt)).toArray();
            List<int[]> lines = new ArrayList<>();
            lines.add(line0);
            for (int itLine = 1; itLine <= cnt; itLine++) {
                int[] prevLine = lines.get(itLine - 1);
                int[] newLine = new int[line0.length];
                for (int itCol = 0; itCol < newLine.length; itCol++) {
                    newLine[itCol] = (itCol > 0 ? newLine[itCol - 1] : 0) + prevLine[itCol];
                }
                lines.add(newLine);
            }
            this.somatorios = lines;
        }

        public T[] PegarCombinacao(int seed) {
            return GerarIndices(somatorios, items.length - cnt, cnt, seed).stream()
                    .map(i -> items[i])
                    .toArray(i -> Arrays.copyOf(items, i));
        }

        private static List<Integer> GerarIndices(List<int[]> lines, int fs, int ts, int num) {
            List<Integer> indices = new ArrayList<>();
            if (ts <= 0) return indices;
            int[] line = lines.get(ts);
            int min = 0;
            for (int itFs = 0; itFs <= fs; itFs++) {
                int max = min + line[itFs];
                if (num < max) {
                    int num2 = num - min;
                    int base = fs - itFs; // Calcula fs - itFs apenas uma vez
                    indices.add(base);
                    indices.addAll(GerarIndices(lines, itFs, ts - 1, num2).stream()
                            .map(idx -> base + idx + 1) // Usa o valor base calculado
                            .collect(Collectors.toList()));
                    return indices;
                }
                min = max;
            }
            throw new IllegalArgumentException("O parâmetro deve ser menor que " + min);
        }

    // Exemplo de uso (adapte para sua necessidade)
    public static void main(String[] args) {
        Integer[] numeros = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        Combinador<Integer> combinador = new Combinador<>(3, numeros);
        Integer[] combinacao = combinador.PegarCombinacao(5);
        System.out.println(Arrays.toString(combinacao));
    }
    
    }
    
}
