package com.fa.io;

import com.fa.AppEnv;
import com.fa.data.fc.Flashcard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class XMLTest {

    @BeforeAll
    public static void setup() {
        URL dataUrl = XMLTest.class.getResource("/testData");
        AppEnv.setDataDirectory(new File(dataUrl.getFile()));
    }

    @Test
    public void pdfToXMLTest() throws IOException {
        URL pdfUrl = getClass().getResource("/testData/PDFTestReader.pdf");
        Assertions.assertNotNull(pdfUrl);

        File file = new File(pdfUrl.getFile());
        Flashcard[] flashcards = PDFReader.loadFlashcardsFromPDF(file, "TestCategory");

        for (Flashcard flashcard : flashcards) {
            Assertions.assertEquals(0, flashcard.getId());
        }

        XMLWriter.saveFlashcardsData(flashcards);
        for (int i = 0; i < flashcards.length; i++) {
            Assertions.assertEquals(i + 2, flashcards[i].getId());
        }
    }

    @Test
    public void loadFlashcardsFromXml() {
        Flashcard[] flashcards = XMLReader.loadFlashcards();
        Flashcard flashcard = flashcards[0];
        Assertions.assertEquals(1, flashcard.getId());
        Assertions.assertEquals(1, flashcard.getFileOrdinal());
        Assertions.assertEquals("AnotherCategory", flashcard.getCategoryName());
        Assertions.assertEquals("PDFTestReader1.pdf", flashcard.getSourceFile().getName());
        Assertions.assertEquals(2, flashcard.getTotalAnswers());
        Assertions.assertEquals(1, flashcard.getCorrectAnswers());
        Assertions.assertEquals(0.5, flashcard.getSuccessRatio());
    }

    @Test
    public void getCategoriesFromXmlTest() {
        Set<String> categories = XMLReader.getCategories();
        Set<String> expectedCategories = new HashSet<>();
        expectedCategories.add("AnotherCategory");
        Assertions.assertEquals(expectedCategories, categories);
    }

}
