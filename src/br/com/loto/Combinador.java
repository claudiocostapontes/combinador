package br.com.loto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}