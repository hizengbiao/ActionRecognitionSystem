package utils;

import java.util.Random;

public class ArraysFunctions {
    public static void shuffle(int[] numbers) {
        Random r = new Random();
        for (int i = 1; i < numbers.length; i++) {
            int rand = r.nextInt(i + 1);
            swap(numbers, rand, i);
        }
    }

    public static void swap(int[] numbers, int a, int b) {
        int temp = numbers[a];
        numbers[a] = numbers[b];
        numbers[b] = temp;
    }
}
