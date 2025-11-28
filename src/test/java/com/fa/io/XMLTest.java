package com.fa.io;

import com.fa.AbstractTest;
import com.fa.AppEnv;
import com.fa.data.AppMode;
import com.fa.data.Box;
import com.fa.data.BoxManager;
import com.fa.data.IDAllocator;
import com.fa.data.Profile;
import com.fa.data.fc.Flashcard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XMLTest extends AbstractTest {

    @Test
    @Order(1)
    public void loadFlashcardsFromXml() {
        Flashcard[] flashcards = XMLReader.loadFlashcards(DataManager.getProfile(2));
        Flashcard flashcard = flashcards[0];
        Assertions.assertEquals(1, flashcard.getId());
        Assertions.assertEquals(1, flashcard.getFileOrdinal());
        Assertions.assertEquals("AnotherCategory", flashcard.getCategoryName());
        Assertions.assertEquals("PDFTestReader1.pdf", flashcard.getSourceFile().getName());
        Assertions.assertEquals(2, flashcard.getTotalAnswers());
        Assertions.assertEquals(1, flashcard.getCorrectAnswers());
        Assertions.assertEquals(0.5, flashcard.getSuccessRatio());

        Flashcard flashcard2 = new Flashcard();
        flashcard2.setId(1);
        flashcard2.setFileOrdinal(1);
        flashcard2.setCategoryName("AnotherCategory");
        flashcard2.setSourceFile(flashcard.getSourceFile());
        Assertions.assertEquals(flashcard, flashcard2);
        Assertions.assertFalse(flashcard.toString().contains("@"));

        flashcard.answer(true, AppMode.CATEGORY_BROWSER);
        XMLWriter.updateFlashcardStats(flashcard, DataManager.getProfile(1));
        Assertions.assertTrue(flashcard.wasAnsweredInCategoryToday());
    }

    @Test
    @Order(2)
    public void getCategoriesFromXmlTest() {
        Set<String> categories = XMLReader.loadCategories(DataManager.getProfile(2));
        Set<String> expectedCategories = new HashSet<>();
        expectedCategories.add("AnotherCategory");
        Assertions.assertEquals(expectedCategories, categories);
    }

    @Test
    @Order(3)
    public void getBoxesFromXmlTest() {
        Box[] boxes = XMLReader.loadBoxes(DataManager.getProfile(2));
        Assertions.assertEquals(3, boxes.length);
        Assertions.assertEquals(0, boxes[0].getIndex());
        Assertions.assertEquals(0, boxes[0].getDuration());
        Assertions.assertEquals(3, boxes[0].size());
        Assertions.assertArrayEquals(new Integer[]{2, 4, 6}, boxes[0].getFlashcardsIds());
        Assertions.assertEquals(1, boxes[1].getIndex());
        Assertions.assertEquals(1, boxes[1].getDuration());
        Assertions.assertEquals(2, boxes[1].size());
        Assertions.assertArrayEquals(new Integer[]{1, 3}, boxes[1].getFlashcardsIds());
        Assertions.assertEquals(2, boxes[2].getIndex());
        Assertions.assertEquals(0, boxes[2].getDuration());
        Assertions.assertEquals(1, boxes[2].size());
        Assertions.assertArrayEquals(new Integer[]{0}, boxes[2].getFlashcardsIds());
    }

    @Test
    @Order(4)
    public void moveFlashcardTest() {
        Flashcard flashcard = BoxManager.getNextUnansweredFlashcard();
        Box originalBox = BoxManager.findBox(flashcard.getId());
        BoxManager.handleSuccessfulAnswer(flashcard.getId());
        Box finalBox = BoxManager.findBox(flashcard.getId());
        Assertions.assertNotEquals(originalBox, finalBox);
        Assertions.assertEquals(finalBox, BoxManager.getNextBox(originalBox));
    }

    @Test
    @Order(5)
    public void pdfToXMLTest() {
        URL pdfUrl = getClass().getResource("/testData/Profile_3/PDFTestReader.pdf");
        Assertions.assertNotNull(pdfUrl);

        File file = new File(pdfUrl.getFile());
        Flashcard[] flashcards = PDFReader.loadFlashcardsFromPDF(file, "TestCategory");

        for (Flashcard flashcard : flashcards) {
            Assertions.assertEquals(0, flashcard.getId());
        }
    }

    @Test
    @Order(6)
    public void addAndDeleteProfileTest() {
        int id = IDAllocator.getNextProfileID();
        Profile profile = new Profile(id);
        XMLWriter.saveProfile(profile);

        File createdDirectory = new File(AppEnv.getDataDirectory(), "Profile_" + id);
        Assertions.assertTrue(createdDirectory.exists());
        XMLWriter.deleteProfile(profile);
        Assertions.assertNull(DataManager.getProfile(id));
        Assertions.assertFalse(createdDirectory.exists());
    }

    @Test
    @Order(7)
    public void addAndDeleteCategoryTest() {
        Profile profile = DataManager.getProfile(1);
        List<Flashcard> flashcards = new ArrayList<>();
        flashcards.add(new Flashcard());
        flashcards.add(new Flashcard());
        flashcards.forEach(flashcard -> {
            flashcard.setId(IDAllocator.getNextFlashcardId(profile));
            flashcard.setCategoryName("TestTemp");
            flashcard.setSourceFile(new File("C:\\Users\\greew\\Desktop\\JavaCert_FC_02_Operators.pdf"));
        });

        XMLWriter.saveFlashcardsData(flashcards.toArray(new Flashcard[0]), profile);
        DataManager.setCurrentProfile(profile);
        Assertions.assertArrayEquals(flashcards.toArray(), DataManager.getAllFlashcardsFromCategory("TestTemp"));

        XMLWriter.deleteCategory("TestTemp", profile);
        DataManager.reloadCurrentProfile();
        Assertions.assertEquals(0, DataManager.getAllFlashcardsFromCategory("TestTemp").length);
    }
}
