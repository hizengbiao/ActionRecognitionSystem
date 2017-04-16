package image;

import har.Labels;
import har.DrawVideosDataPoint;
import io.Database;

import java.io.IOException;

import Jama.Matrix;

public class ImageFilters {
    private final static int maskX = 3;
    private final static int maskY = 3;
    private final static int maskZ = 3;
    private final static int cubeSize = 13;
    private final static int cubeTimeSize = 10;

    public static Matrix[] sobel3D(Matrix[] word) {
        // creating this matrix
        // [-1 -2 -1] [0 0 0] [1 2 1]
        // [-2 -4 -2] [0 0 0] [2 4 2]
        // [-1 -2 -1] [0 0 0] [1 2 1]

        final int[][][] sobelMaskZ = new int[][][] {
                new int[][] { new int[] { -1, -2, -1 },
                        new int[] { -2, -4, -2 }, new int[] { -1, -2, -1 } },
                new int[][] { new int[] { 0, 0, 0 }, new int[] { 0, 0, 0 },
                        new int[] { 0, 0, 0 } },
                new int[][] { new int[] { 1, 2, 1 }, new int[] { 2, 4, 2 },
                        new int[] { 1, 2, 1 } } };

        // creating this matrix
        // [-1 0 1] [-2 0 2] [-1 0 1]
        // [-2 0 2] [-4 0 4] [-2 0 2]
        // [-1 0 1] [-2 0 2] [-1 0 1]

        final int[][][] sobelMaskX = new int[][][] {
                new int[][] { new int[] { -1, 0, 1 }, new int[] { -2, 0, 2 },
                        new int[] { -1, 0, 1 } },
                new int[][] { new int[] { -2, 0, 2 }, new int[] { -4, 0, 4 },
                        new int[] { -2, 0, 2 } },
                new int[][] { new int[] { -1, 0, 1 }, new int[] { -2, 0, 2 },
                        new int[] { -1, 0, 1 } } };

        // creating this matrix
        // [-1 -2 -1] [-2 -4 -2] [-1 -2 -1]
        // [ 0 0 0] [ 0 0 0] [ 0 0 0]
        // [ 1 2 1] [ 2 4 2] [ 1 2 1]

        final int[][][] sobelMaskY = new int[][][] {
                new int[][] { new int[] { -1, -2, -1 }, new int[] { 0, 0, 0 },
                        new int[] { 1, 2, 1 } },
                new int[][] { new int[] { -2, -4, -2 }, new int[] { 0, 0, 0 },
                        new int[] { 2, 4, 2 } },
                new int[][] { new int[] { -1, -2, -1 }, new int[] { 0, 0, 0 },
                        new int[] { 1, 2, 1 } } };

        Matrix[] result = new Matrix[word.length];
        for (int i = 0; i < cubeTimeSize; i++) {
            result[i] = new Matrix(cubeSize, cubeSize);
            for (int j = 0; j < cubeSize; j++) {
                for (int k = 0; k < cubeSize; k++) {
                    int finalPixelXValue = 0, finalPixelYValue = 0, finalPixelZValue = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                try {
                                    finalPixelXValue += word[i + x].get(j + y,
                                            k + z)
                                            * sobelMaskX[x + 1][y + 1][z + 1];
                                } catch (IndexOutOfBoundsException e) {
                                }
                                try {
                                    finalPixelYValue += word[i + x].get(j + y,
                                            k + z)
                                            * sobelMaskY[x + 1][y + 1][z + 1];
                                } catch (IndexOutOfBoundsException e) {
                                }
                                try {
                                    finalPixelZValue += word[i + x].get(j + y,
                                            k + z)
                                            * sobelMaskZ[x + 1][y + 1][z + 1];
                                } catch (IndexOutOfBoundsException e) {
                                }
                            }
                        }
                    }
                    int finalValue = 0;
                    finalValue += finalPixelXValue * finalPixelXValue;
                    finalValue += finalPixelYValue * finalPixelYValue;
                    finalValue += finalPixelZValue * finalPixelZValue;
                    finalValue = (int) Math.sqrt(finalValue);
                    result[i].set(j, k, finalValue);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            Matrix[] m = Database.readAWordOfVideo(Labels.BOXING, 1, 123);
            Matrix[] t = sobel3D(m);
            DrawVideosDataPoint.draw(m, 3);
            DrawVideosDataPoint.draw(t, 3);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
