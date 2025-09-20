package com.fa.gui.panels;

import com.fa.data.fc.Flashcard;
import com.fa.io.PDFReader;
import com.fa.util.gui.ImageUtil;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FlashcardDisplayPanel extends JPanel {

    private Image image;

    private Flashcard flashcard;

    private boolean isAverse;

    public FlashcardDisplayPanel() {
        addMouseListener(new DisplayMouseAdapter());
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
        setToAverse();
    }

    public void setToAverse() {
        image = PDFReader.loadAverse(flashcard);
        isAverse = true;
        repaint();
    }

    public void setToReverse() {
        image = PDFReader.loadReverse(flashcard);
        isAverse = false;
        repaint();
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() - 50;
        int height = getHeight() - 50;

        if (width <= 0 || height <=0 || image == null) {
            return;
        }

        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        if ((float) width / imageWidth > (float) height / imageHeight) {
            imageWidth = (int) (imageWidth * ((float) height / imageHeight));
            imageHeight = height;
        } else {
            imageHeight = (int) (imageHeight * ((float) width / imageWidth));
            imageWidth = width;
        }

        Image resizedImage = ImageUtil.resize(image, imageWidth, imageHeight);
        g.drawImage(resizedImage, (width + 50 - imageWidth) / 2, (height + 50 - imageHeight) / 2, null);
    }

    class DisplayMouseAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {
            if (isAverse) {
                setToReverse();
            } else {
                setToAverse();
            }
        }

    }

}
