package com.fa.io;

import com.fa.data.fc.Flashcard;
import com.fa.util.PDFUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PDFTest {

    @Test
    public void pdfReaderTest() throws IOException {
        File file = getPDFSampleFile();
        int pagesCount = PDFUtil.countPages(file);
        Assertions.assertEquals(6, pagesCount);
    }

    @Test
    public void imageLoaderTest() throws IOException {
        File file = getPDFSampleFile();
        Image page = PDFUtil.loadImage(file, 1, true);
        Assertions.assertNotNull(page);

        Assertions.assertNull(PDFUtil.loadImage(file, 4, true));

    }

    @Test
    public void loadFlashcardsFromPDFTest() throws IOException {
        File file = getPDFSampleFile();
        Flashcard[] flashcards = PDFReader.loadFlashcardsFromPDF(file, "TestCategory");
        Assertions.assertEquals(3, flashcards.length);
        Assertions.assertEquals(file, flashcards[0].getSourceFile());

        Assertions.assertEquals(1, flashcards[0].getFileOrdinal());
        Assertions.assertEquals(2, flashcards[1].getFileOrdinal());
        Assertions.assertEquals(3, flashcards[2].getFileOrdinal());

        Assertions.assertEquals("TestCategory", flashcards[0].getCategoryName());

        Assertions.assertEquals(0, flashcards[0].getId());
        Assertions.assertEquals(0, flashcards[1].getId());
        Assertions.assertEquals(0, flashcards[2].getId());

        Assertions.assertNotEquals(flashcards[0], flashcards[1]);

        Assertions.assertNotEquals(flashcards[0].hashCode(), flashcards[1].hashCode());

        Assertions.assertNotNull(PDFReader.loadAverse(flashcards[0]));
        Assertions.assertNotNull(PDFReader.loadReverse(flashcards[0]));
    }

    private File getPDFSampleFile() throws IOException {
        URL url = getClass().getResource("/testData/PDFTestReader.pdf");
        if (url == null) {
            throw new IOException("Could not find PDF sample file.");
        }
        return new File(url.getFile());
    }

}
