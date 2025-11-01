package com.fa.io;

import com.fa.AppEnv;
import com.fa.data.Box;
import com.fa.data.BoxManager;
import com.fa.data.DailyActivity;
import com.fa.data.IDAllocator;
import com.fa.data.Profile;
import com.fa.data.fc.Flashcard;
import com.fa.util.XMLUtil;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.function.BiFunction;

public class XMLWriter extends XMLService {

    private static final Logger LOG = Logger.getLogger(XMLWriter.class);

    public static void saveProfile(Profile profile) {
        File profileFile = AppEnv.getProfileFile();
        Document profileFileDocument = XMLUtil.loadDocumentFromFile(profileFile);
        if (profileFileDocument == null) {
            LOG.info("Could not load document from xml file, new document created.");
            profileFileDocument = XMLUtil.createEmptyDocument();
        }

        Element root = XMLUtil.getRootElement(profileFileDocument);
        if (root == null) {
            root = profileFileDocument.createElement(ELEMENT_PROFILES);
            profileFileDocument.appendChild(root);
        }

        Element profileElement = null;
        for (Element element : XMLUtil.getElements(root.getChildNodes())) {
            if (element.getAttribute(ATTRIBUTE_ID).equals(String.valueOf(profile.getId()))) {
                 profileElement = element;
                 break;
            }
        }

        if (profileElement == null) {
            profileElement = profileFileDocument.createElement(ELEMENT_PROFILE);
            root.appendChild(profileElement);
            profileElement.setAttribute(ATTRIBUTE_ID, String.valueOf(profile.getId()));
        }

        profileElement.setAttribute(ATTRIBUTE_NAME, String.valueOf(profile.getName()));
        profileElement.setAttribute(ATTRIBUTE_DAILY_GOAL, String.valueOf(profile.getDailyGoal()));

        XMLUtil.saveDocument(profileFileDocument, profileFile, XMLUtil.DEFAULT_INDENT);

    }

    public static void saveBoxData(Profile profile) {
        if (profile == null) {
            throw new NullPointerException("Cannot save flashcard data - profile not determined.");
        }
        File boxData = AppEnv.getBoxesFile(profile);
        Document boxDataDocument = XMLUtil.createEmptyDocument();
        Element root = boxDataDocument.createElement(ELEMENT_BOXES);
        boxDataDocument.appendChild(root);

        for (Box box : BoxManager.getBoxes()) {
            Element element = createBoxElement(box, boxDataDocument);
            root.appendChild(element);
        }

        XMLUtil.saveDocument(boxDataDocument, boxData, XMLUtil.DEFAULT_INDENT);
    }

    private static Element createBoxElement(Box box, Document boxDataDocument) {
        Element element = boxDataDocument.createElement(ELEMENT_BOX);
        element.setAttribute(ATTRIBUTE_ID, String.valueOf(box.getBoxId()));
        element.setAttribute(ATTRIBUTE_DURATION, String.valueOf(box.getDuration()));
        for (int flashcardId : box.getFlashcardsIds()) {
            Element flashcardElement = createFlashcardElement(flashcardId, boxDataDocument);
            element.appendChild(flashcardElement);
        }
        return element;
    }

    public static void addNewFlashcardsToBoxes(Flashcard[] flashcards, Profile profile) {
        if (profile == null) {
            throw new NullPointerException("Cannot save flashcard data - profile not determined.");
        }
        File boxData = AppEnv.getBoxesFile(profile);
        Document boxDataDocument = XMLUtil.loadDocumentFromFile(boxData);
        Element root = XMLUtil.getRootElement(boxDataDocument);
        if (root == null) {
            root = boxDataDocument.createElement(ELEMENT_BOXES);
            boxDataDocument.appendChild(root);
        }
        for (Element boxElement : XMLUtil.getElements(root.getChildNodes())) {
            if ("0".equals(boxElement.getAttribute(ATTRIBUTE_ID))) {
                for (Flashcard flashcard : flashcards) {
                    Element flashcardElement = createFlashcardElement(flashcard.getId(), boxDataDocument);
                    boxElement.appendChild(flashcardElement);
                }
                break;
            }
        }
        XMLUtil.saveDocument(boxDataDocument, boxData, XMLUtil.DEFAULT_INDENT);
    }

    private static Element createFlashcardElement(int flashcardId, Document document) {
        Element flashcardElement = document.createElement(ELEMENT_FLASHCARD);
        flashcardElement.setAttribute(ATTRIBUTE_ID, String.valueOf(flashcardId));
        return flashcardElement;
    }

    public static void saveFlashcardsData(Flashcard[] flashcards, Profile profile) {
        if (profile == null) {
            throw new NullPointerException("Cannot save flashcard data - profile not determined.");
        }

        for (Flashcard flashcard : flashcards) {
            flashcard.setId(IDAllocator.getNextFlashcardId(profile));
        }

        saveNewFlashcardsInfo(flashcards, profile);
        saveNewFlashcardsStats(flashcards, profile);
    }

    private static void saveNewFlashcardsInfo(Flashcard[] flashcards, Profile profile) {
        File flashcardData = AppEnv.getFlashcardInfoFile(profile);
        appendXmlAndSave(flashcards, flashcardData, XMLWriter::createInfoElement);
    }

    private static Element createInfoElement(Flashcard flashcard, Document flashcardDataDocument) {
        Element result = flashcardDataDocument.createElement(ELEMENT_FLASHCARD);
        result.setAttribute(ATTRIBUTE_ID, String.valueOf(flashcard.getId()));
        result.setAttribute(ATTRIBUTE_FILE_ORDINAL, String.valueOf(flashcard.getFileOrdinal()));
        result.setAttribute(ATTRIBUTE_CATEGORY_NAME, flashcard.getCategoryName());
        result.setAttribute(ATTRIBUTE_SOURCE_FILE, flashcard.getSourceFile().getAbsolutePath());
        return result;
    }

    private static void saveNewFlashcardsStats(Flashcard[] flashcards, Profile profile) {
        File flashcardData = AppEnv.getFlashcardStatsFile(profile);
        appendXmlAndSave(flashcards, flashcardData, XMLWriter::createStatsElement);
    }

    private static Element createStatsElement(Flashcard flashcard, Document flashcardDataDocument) {
        Element result = flashcardDataDocument.createElement(ELEMENT_FLASHCARD);
        result.setAttribute(ATTRIBUTE_ID, String.valueOf(flashcard.getId()));
        result.setAttribute(ATTRIBUTE_ANSWERED, String.valueOf(flashcard.getTotalAnswers()));
        result.setAttribute(ATTRIBUTE_ANSWERED_CORRECTLY, String.valueOf(flashcard.getCorrectAnswers()));
        return result;
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

    public static void updateFlashcardStats(Flashcard flashcard, Profile profile) {
        if (profile == null) {
            throw new NullPointerException("Cannot update stats - profile not determined.");
        }

        File flashcardData = AppEnv.getFlashcardStatsFile(profile);
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
    
    public static void updateDailyActivity(DailyActivity dailyActivity, Profile profile) {
        String dateAsString = dailyActivity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        File dailyActivityFile = AppEnv.getDailyActivityFile(profile);
        Document dailyActivityDocument = XMLUtil.loadDocumentFromFile(dailyActivityFile);
        if (dailyActivityDocument == null) {
            LOG.info("Could not load document from xml file, new document created.");
            dailyActivityDocument = XMLUtil.createEmptyDocument();
        }

        Element root = XMLUtil.getRootElement(dailyActivityDocument);
        if (root == null) {
            root = dailyActivityDocument.createElement(ELEMENT_DAYS);
            dailyActivityDocument.appendChild(root);
        }

        NodeList children = root.getChildNodes();
        Element[] elements = XMLUtil.getElements(children);
        Element dayElement = null;
        for (Element element : elements) {
            if (dateAsString.equals(element.getAttribute(ATTRIBUTE_DATE))) {
                dayElement = element;
                break;
            }
        }

        if (dayElement == null) {
            dayElement = dailyActivityDocument.createElement(ELEMENT_DAY);
            dayElement.setAttribute(ATTRIBUTE_DATE, dateAsString);
            root.appendChild(dayElement);
        }

        dayElement.setAttribute(ATTRIBUTE_ANSWERED, String.valueOf(dailyActivity.getTotalAnswers()));
        dayElement.setAttribute(ATTRIBUTE_ANSWERED_CORRECTLY, String.valueOf(dailyActivity.getCorrectAnswers()));
        dayElement.setAttribute(ATTRIBUTE_BOX_ANSWERED, String.valueOf(dailyActivity.getBoxTotalAnswers()));
        dayElement.setAttribute(ATTRIBUTE_BOX_ANSWERED_CORRECTLY, String.valueOf(dailyActivity.getBoxCorrectAnswers()));
        
        XMLUtil.saveDocument(dailyActivityDocument, dailyActivityFile, XMLUtil.DEFAULT_INDENT);
    }

}
