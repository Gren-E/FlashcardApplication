package com.fa.gui.panels;

import com.fa.data.fc.Flashcard;
import com.fa.io.PDFReader;
import com.fa.util.gui.ImageUtil;

import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FlashcardDisplayPanel extends JPanel {

    private Image image;

    private Flashcard flashcard;

    private boolean isAverse;

    private String noImageMessage;

    public FlashcardDisplayPanel() {
        setOpaque(false);
        addMouseListener(new DisplayMouseAdapter());
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
        setToAverse();
    }

    public void setNoImageMessage(String text) {
        noImageMessage = text;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    private void setToAverse() {
        image = (flashcard == null) ? null : PDFReader.loadAverse(flashcard);
        isAverse = true;
        repaint();
    }

    private void setToReverse() {
        image = flashcard == null ? null : PDFReader.loadReverse(flashcard);
        isAverse = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (flashcard == null) {
            paintText(g);
        } else {
            paintFlashcard(g);
        }
    }

    private void paintText(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <=0 || noImageMessage == null) {
            return;
        }

        Font font = getFont();
        g.setFont(font);
        int textWidth = g.getFontMetrics().stringWidth(noImageMessage);
        int textHeight = g.getFontMetrics().getHeight();
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2;
        g.drawString(noImageMessage, x, y);
    }

    private void paintFlashcard(Graphics g) {
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
            if (isEnabled()) {
                if (isAverse) {
                    setToReverse();
                } else {
                    setToAverse();
                }
            }
        }

    }

}
