package com.fa.io;

import com.fa.data.fc.Flashcard;
import com.fa.util.PDFUtil;
import org.apache.log4j.Logger;

import java.awt.Image;
import java.io.File;

public class PDFReader {

    private static final Logger LOG = Logger.getLogger(PDFReader.class);

    public static Flashcard[] loadFlashcardsFromPDF(File file, String initialCategoryName) {
        int pageCount = PDFUtil.countPages(file);
        if (pageCount % 2 == 1) {
            LOG.error("Invalid PDF file - odd number of pages.");
            throw new IllegalArgumentException("Invalid PDF file - odd number of pages.");
        }

        Flashcard[] flashcards = new Flashcard[pageCount / 2];
        for (int i = 0; i < flashcards.length; i++) {
            Flashcard flashcard = new Flashcard();
            flashcard.setId(0);
            flashcard.setFileOrdinal(i + 1);
            flashcard.setCategoryName(initialCategoryName);
            flashcard.setSourceFile(file);
            flashcards[i] = flashcard;
        }

        return flashcards;
    }

    public static Image loadAverse(Flashcard flashcard) {
        return PDFUtil.loadImage(flashcard.getSourceFile(),flashcard.getFileOrdinal(),true);
    }

    public static Image loadReverse(Flashcard flashcard) {
        return PDFUtil.loadImage(flashcard.getSourceFile(),flashcard.getFileOrdinal(),false);
    }

}
