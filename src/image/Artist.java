package image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import Jama.Matrix;

public class Artist {
    Matrix[] mat;
    JFrame frame = null;

    public Artist(Matrix[] mat) {
        this.mat = mat;
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height
                / 2 - frame.getSize().height / 2);

    }

    public void draw(int frameNumber) {
        Matrix img = mat[0];

    }

    public static void drawImage(Matrix img) {
        JFrame frame = new JFrame();
        BufferedImage bufferedImage = new BufferedImage(img.getRowDimension(),
                img.getColumnDimension(), BufferedImage.TYPE_INT_BGR);
        for (int i = 0; i < img.getRowDimension(); i++) {
            for (int j = 0; j < img.getColumnDimension(); j++) {
                int colorValue = (int) (img.get(i, j));
                Color color = new Color(colorValue, colorValue, colorValue);
                bufferedImage.setRGB(i, j, color.getRGB());
            }
        }
        frame.add(new ArtistPanel(bufferedImage));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height
                / 2 - frame.getSize().height / 2);
        frame.setVisible(true);
    }

    public static void drawImage(Matrix[] images) {
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(2, 5));
        for (int image = 0; image < images.length; image++) {
            Matrix img = images[image];
            BufferedImage bufferedImage = new BufferedImage(
                    img.getRowDimension(), img.getColumnDimension(),
                    BufferedImage.TYPE_INT_BGR);
            for (int i = 0; i < img.getRowDimension(); i++) {
                for (int j = 0; j < img.getColumnDimension(); j++) {
                    int colorValue = (int) (img.get(i, j));
                    Color color = new Color(colorValue, colorValue, colorValue);
                    bufferedImage.setRGB(i, j, color.getRGB());
                }
            }
            frame.add(new ArtistPanel(bufferedImage));
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 200);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height
                / 2 - frame.getSize().height / 2);
        frame.setVisible(true);

    }
}
