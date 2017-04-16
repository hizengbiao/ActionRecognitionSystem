package utils;

import Jama.Matrix;
import har.Labels;

public class Utils {
    public static Labels getVideoClass(int videoNumber) {
        for (Labels c : Labels.values()) {
            if (videoNumber > c.getNumberOfVideos()) {
                videoNumber -= c.getNumberOfVideos();
            } else {
                return c;
            }
        }
        return null;
    }

    public static int getVideoNumberInClass(int videoNumber) {
        for (Labels c : Labels.values()) {
            if (videoNumber > c.getNumberOfVideos()) {
                videoNumber -= c.getNumberOfVideos();
            } else {
                return videoNumber;
            }
        }
        return -1;
    }

    public static double findMax(Matrix m) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                if (m.get(i, j) > max) {
                    max = m.get(i, j);
                }
            }
        }
        return max;
    }

    public static double findMin(Matrix m) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                if (m.get(i, j) < min) {
                    min = m.get(i, j);
                }
            }
        }
        return min;
    }
}
