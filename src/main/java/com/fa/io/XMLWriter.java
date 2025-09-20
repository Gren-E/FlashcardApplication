package com.fa.io;

import com.fa.AppEnv;
import com.fa.data.IDAllocator;
import com.fa.data.fc.Flashcard;
import com.fa.data.fc.FlashcardStats;
import com.fa.util.XMLUtil;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.util.function.BiFunction;

public class XMLWriter extends XMLService {

    private static final Logger LOG = Logger.getLogger(XMLWriter.class);


    public static void saveFlashcardsData(Flashcard[] flashcards) {
        for (Flashcard flashcard : flashcards) {
            flashcard.setId(IDAllocator.getNextFlashcardId());
        }

        saveFlashcardsInfo(flashcards);
        saveNewFlashcardsStats(flashcards);
    }

    public static Element createInfoElement(Flashcard flashcard, Document flashcardDataDocument) {
        Element result = flashcardDataDocument.createElement(ELEMENT_FLASHCARD);
        result.setAttribute(ATTRIBUTE_ID, String.valueOf(flashcard.getId()));
        result.setAttribute(ATTRIBUTE_FILE_ORDINAL, String.valueOf(flashcard.getFileOrdinal()));
        result.setAttribute(ATTRIBUTE_CATEGORY_NAME, flashcard.getCategoryName());
        result.setAttribute(ATTRIBUTE_SOURCE_FILE, flashcard.getSourceFile().getAbsolutePath());
        return result;
    }

    public static Element createStatsElement(Flashcard flashcard, Document flashcardDataDocument) {
        Element result = flashcardDataDocument.createElement(ELEMENT_FLASHCARD);
        result.setAttribute(ATTRIBUTE_ID, String.valueOf(flashcard.getId()));
        result.setAttribute(ATTRIBUTE_ANSWERED, String.valueOf(flashcard.getTotalAnswers()));
        result.setAttribute(ATTRIBUTE_ANSWERED_CORRECTLY, String.valueOf(flashcard.getCorrectAnswers()));
        return result;
    }

    private static void saveFlashcardsInfo(Flashcard[] flashcards) {
        File flashcardData = AppEnv.getFlashcardInfoFile();
        appendXmlAndSave(flashcards, flashcardData, XMLWriter::createInfoElement);
    }

    private static void saveNewFlashcardsStats(Flashcard[] flashcards) {
        File flashcardData = AppEnv.getFlashcardStatsFile();
        appendXmlAndSave(flashcards, flashcardData, XMLWriter::createStatsElement);

    }

    private static void appendXmlAndSave(Flashcard[] flashcards, File saveFile, BiFunction<Flashcard, Document, Element> elementCreator) {
        Document flashcardDataDocument = XMLUtil.loadDocumentFromFile(saveFile);
        if (flashcardDataDocument == null) {
            LOG.info("Could not load document from xml file, new document created.");
            flashcardDataDocument = XMLUtil.createEmptyDocument();
        }

        Element root = XMLUtil.getRootElement(flashcardDataDocument);
        if (root == null) {
            root = flashcardDataDocument.createElement(ELEMENT_FLASHCARDS);
            flashcardDataDocument.appendChild(root);
        }

        for (Flashcard flashcard : flashcards) {
            Element flashcardElement = elementCreator.apply(flashcard, flashcardDataDocument);

            root.appendChild(flashcardElement);
        }

        XMLUtil.saveDocument(flashcardDataDocument, saveFile, XMLUtil.DEFAULT_INDENT);
    }

    public static void updateStats(Flashcard flashcard) {
        File flashcardData = AppEnv.getFlashcardStatsFile();
        Document flashcardDataDocument = XMLUtil.loadDocumentFromFile(flashcardData);
        if (flashcardDataDocument == null) {
            LOG.info("Could not load document from xml file, new document created.");
            flashcardDataDocument = XMLUtil.createEmptyDocument();
        }
        Element root = XMLUtil.getRootElement(flashcardDataDocument);
        if (root == null) {
            root = flashcardDataDocument.createElement(ELEMENT_FLASHCARDS);
            flashcardDataDocument.appendChild(root);
        }

        for (Element element : XMLUtil.getElements(root.getChildNodes())) {
            if (Integer.parseInt(element.getAttribute(ATTRIBUTE_ID)) == flashcard.getId()) {
                updateFlashcardStatsElement(element, flashcard);
                XMLUtil.saveDocument(flashcardDataDocument, flashcardData, XMLUtil.DEFAULT_INDENT);
                return;
            }
        }

        Element element = flashcardDataDocument.createElement(ELEMENT_FLASHCARD);
        updateFlashcardStatsElement(element, flashcard);
        root.appendChild(element);
        XMLUtil.saveDocument(flashcardDataDocument, flashcardData, XMLUtil.DEFAULT_INDENT);
    }

    private static void updateFlashcardStatsElement(Element element, Flashcard flashcard) {
        element.setAttribute(ATTRIBUTE_ID, String.valueOf(flashcard.getId()));
        element.setAttribute(ATTRIBUTE_ANSWERED, String.valueOf(flashcard.getTotalAnswers()));
        element.setAttribute(ATTRIBUTE_ANSWERED_CORRECTLY, String.valueOf(flashcard.getCorrectAnswers()));
        element.setAttribute(ATTRIBUTE_LAST_ANSWERED, String.valueOf(flashcard.getLastAnsweredDate()));
    }


}
