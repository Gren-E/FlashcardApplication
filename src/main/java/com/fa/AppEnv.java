package com.fa;

import com.fa.data.Profile;
import com.fa.io.XMLService;
import com.fa.util.XMLUtil;
import org.apache.log4j.Logger;

import java.io.File;

public class AppEnv {

    private static final Logger LOG = Logger.getLogger(AppEnv.class);

    private static File dataDirectory;

    public static void setDataDirectory(File file) {
        dataDirectory = file;
    }

    public static File getDataDirectory() {
        return dataDirectory;
    }

    public static File getImageDirectory() {
        return new File(getDataDirectory(), "image");
    }

    public static File getFontDirectory() {
        return new File(getDataDirectory(), "fonts");
    }

    public static File getProfileDirectory(Profile profile) {
        return new File(getDataDirectory(), "Profile_" + profile.getId());
    }

    public static File getFlashcardInfoFile(Profile profile) {
        return getXmlFile("FlashcardsInfo.xml", profile, XMLService.ELEMENT_FLASHCARDS);
    }

    public static File getFlashcardStatsFile(Profile profile) {
        return getXmlFile("FlashcardsStats.xml", profile, XMLService.ELEMENT_FLASHCARDS);
    }

    public static File getDailyActivityFile(Profile profile) {
        return getXmlFile("DailyActivity.xml", profile, XMLService.ELEMENT_DAYS);
    }

    public static File getBoxesFile(Profile profile) {
        return getXmlFile("Boxes.xml", profile, XMLService.ELEMENT_BOXES);
    }

    public static File getProfileFile() {
        return getXmlFile("Profiles.xml", null, XMLService.ELEMENT_PROFILES);
    }

    private static synchronized File getXmlFile(String fileName, Profile profile, String rootTag) {
        File directory = (profile == null) ? dataDirectory : getProfileDirectory(profile);
        File file = new File(directory, fileName);
        boolean fileExists = XMLUtil.createIfNotExisting(file, rootTag);
        if (!fileExists) {
            LOG.error("Could not create file: " + fileName);
            return null;
        }

        return file;
    }

}
