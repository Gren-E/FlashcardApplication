package com.fa.io;

import com.fa.AppEnv;
import com.fa.data.fc.Flashcard;
import com.fa.data.fc.FlashcardStats;
import com.fa.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XMLReader extends XMLService {

    public static int getLastId() {
        File flashcardDataFile = AppEnv.getFlashcardInfoFile();
        Document document = XMLUtil.loadDocumentFromFile(flashcardDataFile);
        if (document == null) {
            return 0;
        }
        Element root = XMLUtil.getRootElement(document);
        NodeList children = root.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child instanceof Element childElement) {
                return Integer.parseInt(childElement.getAttribute(ATTRIBUTE_ID));
            }
        }
        return 0;
    }

    public static Set<String> getCategories() {
        Set<String> categories = new HashSet<>();
        File flashcardDataFile = AppEnv.getFlashcardInfoFile();
        Document document = XMLUtil.loadDocumentFromFile(flashcardDataFile);
        if (document == null) {
            return null;
        }
        Element root = XMLUtil.getRootElement(document);
        NodeList children = root.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child instanceof Element childElement) {
                categories.add(childElement.getAttribute(ATTRIBUTE_CATEGORY_NAME));
            }
        }

        return categories;
    }

    public static Flashcard[] loadFlashcards() {
        File flashcardDataFile = AppEnv.getFlashcardInfoFile();
        Document document = XMLUtil.loadDocumentFromFile(flashcardDataFile);
        if (document == null) {
            return new Flashcard[0];
        }
        Element root = XMLUtil.getRootElement(document);
        NodeList children = root.getChildNodes();
        Element[] elements = XMLUtil.getElements(children);
        List<Flashcard> flashcards = new ArrayList<>();
        for (Element element : elements) {
            Flashcard flashcard = createFlashcard(element);
            flashcards.add(flashcard);
        }

        Map<Integer, FlashcardStats> flashcardStats = loadFlashcardsStats();
        for (Flashcard flashcard : flashcards) {
            if (flashcardStats.containsKey(flashcard.getId())) {
                FlashcardStats stats = flashcardStats.get(flashcard.getId());
                flashcard.setTotalAnswers(stats.getTotalAnswers());
                flashcard.setCorrectAnswers(stats.getCorrectAnswers());
                flashcard.setLastAnswered(stats.getLastAnsweredDate());
            }
        }


        return flashcards.toArray(new Flashcard[0]);
    }

    private static Map<Integer, FlashcardStats> loadFlashcardsStats() {
        Map<Integer, FlashcardStats> result = new HashMap<>();

        File flashcardDataFile = AppEnv.getFlashcardStatsFile();
        Document document = XMLUtil.loadDocumentFromFile(flashcardDataFile);
        if (document == null) {
            return result;
        }
        Element root = XMLUtil.getRootElement(document);
        NodeList children = root.getChildNodes();
        Element[] elements = XMLUtil.getElements(children);
        for (Element element : elements) {
            FlashcardStats stats = new FlashcardStats();
            int id = Integer.parseInt(element.getAttribute(ATTRIBUTE_ID));
            stats.setTotalAnswers(Integer.parseInt(element.getAttribute(ATTRIBUTE_ANSWERED)));
            stats.setCorrectAnswers(Integer.parseInt(element.getAttribute(ATTRIBUTE_ANSWERED_CORRECTLY)));
            if (element.hasAttribute(ATTRIBUTE_LAST_ANSWERED)) {
                String lastAnswered = element.getAttribute(ATTRIBUTE_LAST_ANSWERED);
                LocalDate date = LocalDate.parse(lastAnswered);
                stats.setLastAnswered(date);
            }
            result.put(id, stats);
        }

        return result;
    }

    public static Flashcard createFlashcard(Element element) {
        Flashcard result = new Flashcard();
        result.setId(Integer.parseInt(element.getAttribute(ATTRIBUTE_ID)));
        result.setFileOrdinal(Integer.parseInt(element.getAttribute(ATTRIBUTE_FILE_ORDINAL)));
        result.setCategoryName(element.getAttribute(ATTRIBUTE_CATEGORY_NAME));
        result.setSourceFile(new File(element.getAttribute(ATTRIBUTE_SOURCE_FILE)));
        return result;
    }



}
