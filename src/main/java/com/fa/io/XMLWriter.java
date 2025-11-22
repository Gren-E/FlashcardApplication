package com.fa.io;

import com.fa.AppEnv;
import com.fa.data.Box;
import com.fa.data.BoxManager;
import com.fa.data.DailyActivity;
import com.fa.data.IDAllocator;
import com.fa.data.Profile;
import com.fa.data.fc.Flashcard;
import com.fa.util.FileUtil;
import com.fa.util.XMLUtil;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
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
        profileElement.setAttribute(ATTRIBUTE_DAILY_RELEARNING_GOAL, String.valueOf(profile.getDailyRelearningGoal()));

        XMLUtil.saveDocument(profileFileDocument, profileFile, XMLUtil.DEFAULT_INDENT);

        File boxDataFile = AppEnv.getBoxesFile(profile);
        Document boxDataDocument = XMLUtil.loadDocumentFromFile(boxDataFile);
        if (boxDataDocument == null) {
            LOG.info("Could not load document from xml file, new document created.");
            boxDataDocument = XMLUtil.createEmptyDocument();
        }

        Element boxRoot = XMLUtil.getRootElement(boxDataDocument);
        if (boxRoot == null) {
            boxRoot = boxDataDocument.createElement(ELEMENT_BOXES);
            boxDataDocument.appendChild(boxRoot);
        }

        if (XMLUtil.getElements(boxRoot.getChildNodes()).length == 0) {
            createStandardBoxSet(boxRoot, boxDataDocument);
        }

        XMLUtil.saveDocument(boxDataDocument, boxDataFile, XMLUtil.DEFAULT_INDENT);
    }

    public static void deleteProfile(Profile profile) {
        File profileDirectory = AppEnv.getProfileDirectory(profile);
        try {
            FileUtil.deleteDirectory(profileDirectory);
        } catch (IOException e) {
            LOG.error("Could not delete file.", e);
            return;
        }

        File profileFile = AppEnv.getProfileFile();
        Document profileFileDocument = XMLUtil.loadDocumentFromFile(profileFile);
        if (profileFileDocument == null) {
            LOG.info("Cannot delete profile, document is null.");
            return;
        }

        Element root = XMLUtil.getRootElement(profileFileDocument);
        if (root == null) {
            LOG.info("Cannot delete profile, root is null.");
            return;
        }

        for (Element element : XMLUtil.getElements(root.getChildNodes())) {
            if (Objects.equals(element.getAttribute(ATTRIBUTE_ID), (String.valueOf(profile.getId())))) {
                root.removeChild(element);
                break;
            }
        }

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

    private static void createStandardBoxSet(Element root, Document boxDataDocument) {
        Box reserve = new Box(0);
        Box box1 = new Box(1);
        Box box2 = new Box(2);
        Box box3 = new Box(3);
        Box box4 = new Box(4);
        Box learnt = new Box(5);

        box1.setDuration(1);
        box2.setDuration(3);
        box3.setDuration(7);
        box4.setDuration(14);

        root.appendChild(createBoxElement(reserve, boxDataDocument));
        root.appendChild(createBoxElement(box1, boxDataDocument));
        root.appendChild(createBoxElement(box2, boxDataDocument));
        root.appendChild(createBoxElement(box3, boxDataDocument));
        root.appendChild(createBoxElement(box4, boxDataDocument));
        root.appendChild(createBoxElement(learnt, boxDataDocument));
    }

    private static Element createBoxElement(Box box, Document boxDataDocument) {
        Element element = boxDataDocument.createElement(ELEMENT_BOX);
        element.setAttribute(ATTRIBUTE_ID, String.valueOf(box.getIndex()));
        element.setAttribute(ATTRIBUTE_DURATION, String.valueOf(box.getDuration()));
        for (int flashcardId : box.getFlashcardsIds()) {
            Element flashcardElement = createFlashcardElement(flashcardId, boxDataDocument);
            element.appendChild(flashcardElement);
        }
        return element;
    }

    public static void deleteCategory(String categoryName, Profile profile) {
        File flashcardInfoFile = AppEnv.getFlashcardInfoFile(profile);
        XMLUtil.forEachSubElement(flashcardInfoFile, element -> {
            if (Objects.equals(element.getAttribute(ATTRIBUTE_CATEGORY_NAME), categoryName)) {
                int id = Integer.parseInt(element.getAttribute(ATTRIBUTE_ID));
                deleteFlashcardStats(id, profile);
                deleteBoxData(id, profile);
            }
        });
        XMLUtil.removeSubElements(flashcardInfoFile, element -> Objects.equals(element.getAttribute(ATTRIBUTE_CATEGORY_NAME), categoryName));
    }

    private static void deleteFlashcardStats(int id, Profile profile) {
        File flashcardStatsFile = AppEnv.getFlashcardStatsFile(profile);
        XMLUtil.removeSubElements(flashcardStatsFile, element -> Objects.equals(element.getAttribute(ATTRIBUTE_ID), String.valueOf(id)));
    }

    private static void deleteBoxData(int id, Profile profile) {
        File boxDataFile = AppEnv.getBoxesFile(profile);
        Document boxDataDocument = XMLUtil.loadDocumentFromFile(boxDataFile);
        if (boxDataDocument == null) {
            LOG.info("Cannot delete flashcard box data, document is null.");
            return;
        }

        Element root = XMLUtil.getRootElement(boxDataDocument);
        if (root == null) {
            LOG.info("Cannot delete flashcard box data, root is null.");
            return;
        }

        for (Element boxElement : XMLUtil.getElements(root.getChildNodes())) {
            for (Element flashcardElement : XMLUtil.getElements(boxElement.getChildNodes())) {
                if (flashcardElement.getAttribute(ATTRIBUTE_ID).equals(String.valueOf(id))) {
                    boxElement.removeChild(flashcardElement);
                }
            }
        }
        XMLUtil.saveDocument(boxDataDocument, boxDataFile, XMLUtil.DEFAULT_INDENT);
    }

    public static void addNewFlashcardsToBoxes(Flashcard[] flashcards, Profile profile) {
        if (profile == null) {
            throw new NullPointerException("Cannot save flashcard data - profile not determined.");
        }
        File boxData = AppEnv.getBoxesFile(profile);
        Document boxDataDocument = XMLUtil.loadDocumentFromFile(boxData);
        if (boxDataDocument == null) {
            LOG.info("Could not load document from xml file, new document created.");
            boxDataDocument = XMLUtil.createEmptyDocument();
        }

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
        if (flashcard.getCategoryLastAnswered() != null) {
            element.setAttribute(ATTRIBUTE_CATEGORY_LAST_ANSWERED, String.valueOf(flashcard.getCategoryLastAnswered()));
        }
        if (flashcard.getCategoryLastAnsweredCorrectly() != null) {
            element.setAttribute(ATTRIBUTE_CATEGORY_LAST_ANSWERED_CORRECTLY, String.valueOf(flashcard.getCategoryLastAnsweredCorrectly()));
        }
        if (flashcard.getBoxLastAnswered() != null) {
            element.setAttribute(ATTRIBUTE_BOX_LAST_ANSWERED, String.valueOf(flashcard.getBoxLastAnswered()));
        }
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
