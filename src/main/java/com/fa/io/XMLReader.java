package com.fa.io;

import com.fa.AppEnv;
import com.fa.data.Box;
import com.fa.data.DailyActivity;
import com.fa.data.Profile;
import com.fa.data.fc.Flashcard;
import com.fa.data.fc.FlashcardStats;
import com.fa.util.XMLUtil;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class XMLReader extends XMLService {

    private static final Logger LOG = Logger.getLogger(XMLReader.class);

    public static int getLastProfileId() {
        File profileFile = AppEnv.getProfileFile();
        return getLastId(profileFile);
    }

    public static int getLastFlashcardId(Profile profile) {
        File flashcardDataFile = AppEnv.getFlashcardInfoFile(profile);
        return getLastId(flashcardDataFile);
    }

    private static int getLastId(File file) {
        Document document = XMLUtil.loadDocumentFromFile(file);
        if (document == null) {
            return 0;
        }

        Element root = XMLUtil.getRootElement(document);
        Element[] elements = XMLUtil.getElementsInReverse(root.getChildNodes());
        return (elements.length == 0) ? 0 : Integer.parseInt(elements[0].getAttribute(ATTRIBUTE_ID));
    }

    public static Profile[] loadProfiles() {
        File profileFile = AppEnv.getProfileFile();
        Document profileDocument = XMLUtil.loadDocumentFromFile(profileFile);
        if (profileDocument == null) {
            return new Profile[0];
        }

        Element root = XMLUtil.getRootElement(profileDocument);
        List<Profile> profiles = new ArrayList<>();
        for (Element element : XMLUtil.getElements(root.getChildNodes())) {
            Profile profile = loadProfile(element);
            profiles.add(profile);
        }

        return profiles.toArray(new Profile[0]);
    }

    private static Profile loadProfile(Element element) {
        Profile result = new Profile(Integer.parseInt(element.getAttribute(ATTRIBUTE_ID)));
        result.setName(element.getAttribute(ATTRIBUTE_NAME));
        result.setDailyGoal(Integer.parseInt(element.getAttribute(ATTRIBUTE_DAILY_GOAL)));
        result.setDailyRelearningGoal(Integer.parseInt(element.getAttribute(ATTRIBUTE_DAILY_RELEARNING_GOAL)));
        return result;
    }

    public static Set<String> loadCategories(Profile profile) {
        Set<String> categories = new TreeSet<>();
        File flashcardDataFile = AppEnv.getFlashcardInfoFile(profile);
        Document flashcardDataDocument = XMLUtil.loadDocumentFromFile(flashcardDataFile);
        if (flashcardDataDocument == null) {
            LOG.error("Cannot load xml document from file: " + flashcardDataFile.toString());
            return categories;
        }

        Element root = XMLUtil.getRootElement(flashcardDataDocument);
        for (Element element : XMLUtil.getElements(root.getChildNodes())) {
            categories.add(element.getAttribute(ATTRIBUTE_CATEGORY_NAME));
        }

        return categories;
    }

    public static Box[] loadBoxes(Profile profile) {
        File boxFile = AppEnv.getBoxesFile(profile);
        Document boxDocument = XMLUtil.loadDocumentFromFile(boxFile);
        if (boxDocument == null) {
            return new Box[0];
        }

        Element root = XMLUtil.getRootElement(boxDocument);
        List<Box> boxes = new ArrayList<>();
        for (Element element : XMLUtil.getElements(root.getChildNodes())) {
            Box box = loadBox(element);
            boxes.add(box);
        }

        return boxes.toArray(new Box[0]);
    }

    private static Box loadBox(Element boxElement) {
        Box result = new Box(Integer.parseInt(boxElement.getAttribute(ATTRIBUTE_ID)));
        result.setDuration(Integer.parseInt(boxElement.getAttribute(ATTRIBUTE_DURATION)));
        for (Element flashcardElement : XMLUtil.getElements(boxElement.getChildNodes())) {
            if (ELEMENT_FLASHCARD.equals(flashcardElement.getTagName())) {
                result.addLastFlashcard(Integer.parseInt(flashcardElement.getAttribute(ATTRIBUTE_ID)));
            }
        }
        return result;
    }

    public static Flashcard[] loadFlashcards(Profile profile) {
        if (profile == null) {
            LOG.warn("Profile not set for method - loadFlashcards()");
            return new Flashcard[0];
        }
        File flashcardDataFile = AppEnv.getFlashcardInfoFile(profile);
        Document flashcardDataDocument = XMLUtil.loadDocumentFromFile(flashcardDataFile);
        if (flashcardDataDocument == null) {
            return new Flashcard[0];
        }

        Element root = XMLUtil.getRootElement(flashcardDataDocument);
        List<Flashcard> flashcards = new ArrayList<>();
        for (Element element : XMLUtil.getElements(root.getChildNodes())) {
            Flashcard flashcard = loadFlashcard(element);
            flashcards.add(flashcard);
        }

        Map<Integer, FlashcardStats> flashcardStats = loadFlashcardsStats(profile);
        for (Flashcard flashcard : flashcards) {
            if (flashcardStats.containsKey(flashcard.getId())) {
                FlashcardStats stats = flashcardStats.get(flashcard.getId());
                flashcard.setTotalAnswers(stats.getTotalAnswers());
                flashcard.setCorrectAnswers(stats.getCorrectAnswers());
                flashcard.setCategoryLastAnswered(stats.getCategoryLastAnswered());
                flashcard.setCategoryLastAnsweredCorrectly(stats.getCategoryLastAnsweredCorrectly());
                flashcard.setBoxLastAnswered(stats.getBoxLastAnswered());
            }
        }

        return flashcards.toArray(new Flashcard[0]);
    }

    private static Flashcard loadFlashcard(Element element) {
        Flashcard result = new Flashcard();
        result.setId(Integer.parseInt(element.getAttribute(ATTRIBUTE_ID)));
        result.setFileOrdinal(Integer.parseInt(element.getAttribute(ATTRIBUTE_FILE_ORDINAL)));
        result.setCategoryName(element.getAttribute(ATTRIBUTE_CATEGORY_NAME));
        result.setSourceFile(new File(element.getAttribute(ATTRIBUTE_SOURCE_FILE)));
        return result;
    }

    private static Map<Integer, FlashcardStats> loadFlashcardsStats(Profile profile) {
        Map<Integer, FlashcardStats> result = new HashMap<>();
        File flashcardDataFile = AppEnv.getFlashcardStatsFile(profile);
        Document flashcardDataDocument = XMLUtil.loadDocumentFromFile(flashcardDataFile);
        if (flashcardDataDocument == null) {
            return result;
        }

        Element root = XMLUtil.getRootElement(flashcardDataDocument);
        for (Element element : XMLUtil.getElements(root.getChildNodes())) {
            FlashcardStats stats = new FlashcardStats();
            int id = Integer.parseInt(element.getAttribute(ATTRIBUTE_ID));
            stats.setTotalAnswers(Integer.parseInt(element.getAttribute(ATTRIBUTE_ANSWERED)));
            stats.setCorrectAnswers(Integer.parseInt(element.getAttribute(ATTRIBUTE_ANSWERED_CORRECTLY)));

            stats.setCategoryLastAnswered(parseDateFromAttribute(element, ATTRIBUTE_CATEGORY_LAST_ANSWERED));
            stats.setCategoryLastAnsweredCorrectly(parseDateFromAttribute(element, ATTRIBUTE_CATEGORY_LAST_ANSWERED_CORRECTLY));
            stats.setBoxLastAnswered(parseDateFromAttribute(element, ATTRIBUTE_BOX_LAST_ANSWERED));

            result.put(id, stats);
        }

        return result;
    }

    private static LocalDate parseDateFromAttribute(Element element, String attribute) {
        if (element.hasAttribute(attribute)) {
            String attributeValue = element.getAttribute(attribute);
            return LocalDate.parse(attributeValue);
        }

        return null;
    }

    public static DailyActivity loadDailyActivity(LocalDate date, Profile profile) {
        String dateAsString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DailyActivity result = new DailyActivity(date);

        File dailyActivityFile = AppEnv.getDailyActivityFile(profile);
        Document dailyActivityDocument = XMLUtil.loadDocumentFromFile(dailyActivityFile);
        if (dailyActivityDocument == null) {
            return result;
        }

        Element root = XMLUtil.getRootElement(dailyActivityDocument);
        for (Element element : XMLUtil.getElements(root.getChildNodes())) {
            if (dateAsString.equals(element.getAttribute(ATTRIBUTE_DATE))) {
                result.setTotalAnswers(Integer.parseInt(element.getAttribute(ATTRIBUTE_ANSWERED)));
                result.setCorrectAnswers(Integer.parseInt(element.getAttribute(ATTRIBUTE_ANSWERED_CORRECTLY)));
                result.setBoxTotalAnswers(Integer.parseInt(element.getAttribute(ATTRIBUTE_BOX_ANSWERED)));
                result.setBoxCorrectAnswers(Integer.parseInt(element.getAttribute(ATTRIBUTE_BOX_ANSWERED_CORRECTLY)));
                break;
            }
        }

        return result;
    }

}
