package image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ArtistPanel extends JPanel {
    private BufferedImage bufferedImage;

    public ArtistPanel(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Image img = bufferedImage;
        img = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        g.drawImage(img, 20, 20, this);
    }
}
