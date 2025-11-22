package com.fa.io;

import com.fa.data.BoxManager;
import com.fa.data.DailyActivity;
import com.fa.data.Profile;
import com.fa.data.fc.Flashcard;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataManager {

    private static Map<Integer, Profile> allProfiles;
    private static Map<Integer, Flashcard> allFlashcards;

    private static Set<String> allCategories;
    private static final Map<LocalDate, DailyActivity> dailyActivities = new HashMap<>();

    private static Profile currentProfile;

    public static void setCurrentProfile(Profile profile) {
        currentProfile = profile;
        reloadCurrentProfile();
        BoxManager.reload();
    }

    public static Profile getCurrentProfile() {
        return currentProfile;
    }

    public static synchronized Profile[] getAllProfiles() {
        allProfiles = new HashMap<>();
        for (Profile profile : XMLReader.loadProfiles()) {
            allProfiles.put(profile.getId(), profile);
        }

        return allProfiles.values().toArray(new Profile[0]);
    }

    public static synchronized Profile getProfile(int id) {
        if (allProfiles == null) {
            getAllProfiles();
        }

        return allProfiles.get(id);
    }

    public static synchronized void reloadCurrentProfile() {
        loadCategories();
        loadFlashcards();
        dailyActivities.clear();
    }

    public static synchronized String[] getAllCategoriesNames() {
        if (allCategories == null) {
            loadCategories();
        }

        return allCategories.toArray(new String[0]);
    }

    public static synchronized Flashcard[] getAllFlashcards() {
        if (allFlashcards == null) {
            loadFlashcards();
        }

        return allFlashcards.values().toArray(new Flashcard[0]);
    }

    public static synchronized Flashcard getFlashcard(Integer id) {
        if (allFlashcards == null) {
            loadFlashcards();
        }

        return allFlashcards.get(id);
    }

    private static synchronized void loadCategories() {
        allCategories = XMLReader.loadCategories(getCurrentProfile());
    }

    private static synchronized void loadFlashcards() {
        Flashcard[] flashcards = XMLReader.loadFlashcards(getCurrentProfile());
        allFlashcards = new HashMap<>();
        for (Flashcard flashcard : flashcards) {
            allFlashcards.put(flashcard.getId(), flashcard);
        }
    }

    public static Flashcard[] getAllFlashcardsFromCategory(String categoryName) {
        Flashcard[] allFlashcards = getAllFlashcards();
        return Arrays.stream(allFlashcards).filter((flashcard -> categoryName.equals(flashcard.getCategoryName()))).toArray(Flashcard[]::new);
    }

    public static synchronized DailyActivity getDailyActivity(LocalDate date) {
        if (dailyActivities.containsKey(date)) {
            return dailyActivities.get(date);
        }

        DailyActivity result = XMLReader.loadDailyActivity(date, currentProfile);
        dailyActivities.put(date, result);
        return result;
    }

    public static int getDailyActivityStreak() {
         LocalDate date = LocalDate.now();
         DailyActivity dailyActivity = getDailyActivity(date);
         int streak = dailyActivity.isDailyGoalAchieved() ? 1 : 0;

         date = date.minusDays(1);
         dailyActivity = getDailyActivity(date);
         while (dailyActivity.isDailyGoalAchieved()) {
             streak++;
             date = date.minusDays(1);
             dailyActivity = getDailyActivity(date);
         }

         return streak;
    }

}
