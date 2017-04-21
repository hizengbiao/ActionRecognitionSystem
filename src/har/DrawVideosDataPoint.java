package har;

import image.Artist;
import io.Database;

import java.io.FileNotFoundException;
import java.io.IOException;

import utils.Utils;
import Jama.Matrix;

public class DrawVideosDataPoint {
    public static void main(String[] args) {
        Labels videoClass = Labels.BOXING;
        int videoNumber = 2;
        int wordNumber = 2;
        int frameNumber = 10;
        try {
            draw(videoClass, videoNumber, wordNumber, frameNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void draw(Labels videoClass, int videoNumber,
            int wordNumber, int frameNumber) throws FileNotFoundException,
            IOException {
        Matrix[] word = Database.readAWordOfVideo(videoClass, videoNumber,
                wordNumber);
        draw(word, frameNumber - 1);
    }

    public static void draw(Matrix[] word, int frameNumber) {
        Matrix m = word[frameNumber];
        double max = Utils.findMax(m);
        double min = Utils.findMin(m);
        if (max > 255 || min < 0) {
            normalizeImage(m);
        }
        Artist.drawImage(m);
    }

    private static void normalizeImage(Matrix m) {
        double max = Utils.findMax(m);
        double min = Utils.findMin(m);
        double interval = max - min;
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                m.set(i, j, (m.get(i, j) - min) / interval * 255);
            }
        }
    }

    public static void drawAllWords(Matrix[] word) {
        Matrix[] images = new Matrix[word.length];
        for (int frameNumber = 0; frameNumber < word.length; frameNumber++) {
            Matrix m = word[frameNumber];
            double max = Utils.findMax(m);
            double min = Utils.findMin(m);
            if (max > 255 || min < 0) {
                normalizeImage(m);
            }
            images[frameNumber] = m;
        }
        Artist.drawImage(images);
    }
}
